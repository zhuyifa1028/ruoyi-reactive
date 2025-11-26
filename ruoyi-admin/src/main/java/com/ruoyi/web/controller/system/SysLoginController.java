package com.ruoyi.web.controller.system;

import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysMenu;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginBody;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.system.service.SysConfigService;
import com.ruoyi.system.service.SysMenuService;
import com.ruoyi.system.service.impl.SysLoginService;
import com.ruoyi.system.service.impl.SysPermissionService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 登录验证
 *
 * @author ruoyi
 */
@RestController
public class SysLoginController {

    @Resource
    private SysLoginService loginService;

    @Resource
    private SysMenuService menuService;

    @Resource
    private SysPermissionService permissionService;

    @Resource
    private TokenService tokenService;

    @Resource
    private SysConfigService sysConfigService;

    /**
     * 登录方法
     *
     * @param loginBody 登录信息
     * @return 结果
     */
    @PostMapping("/login")
    public Mono<AjaxResult> login(@RequestBody LoginBody loginBody, ServerWebExchange exchange) {
        // 生成令牌
        return loginService.login(loginBody, exchange)
                .flatMap(token -> {
                    AjaxResult ajax = AjaxResult.success();
                    ajax.put(Constants.TOKEN, token);
                    return Mono.just(ajax);
                });
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("getInfo")
    public Mono<AjaxResult> getInfo(ServerWebExchange exchange) {
        return tokenService.getLoginUser(exchange)
                .flatMap(loginUser -> {
                    SysUser user = loginUser.getUser();
                    // 角色集合
                    Set<String> roles = permissionService.getRolePermission(user);
                    // 权限集合
                    Set<String> permissions = permissionService.getMenuPermission(user);
                    if (!loginUser.getPermissions().equals(permissions)) {
                        loginUser.setPermissions(permissions);
                        tokenService.refreshToken(loginUser);
                    }

                    Mono<Boolean> passwordIsModify = initPasswordIsModify(user.getPwdUpdateDate());
                    Mono<Boolean> passwordIsExpiration = passwordIsExpiration(user.getPwdUpdateDate());

                    return Mono.zip(passwordIsModify, passwordIsExpiration)
                            .map(tuple -> {
                                AjaxResult ajax = AjaxResult.success();
                                ajax.put("user", user);
                                ajax.put("roles", roles);
                                ajax.put("permissions", permissions);
                                ajax.put("isDefaultModifyPwd", tuple.getT1());
                                ajax.put("isPasswordExpired", tuple.getT2());
                                return ajax;
                            });
                });
    }

    /**
     * 获取路由信息
     *
     * @return 路由信息
     */
    @GetMapping("getRouters")
    public Mono<AjaxResult> getRouters(ServerWebExchange exchange) {
        return tokenService.getLoginUser(exchange)
                .map(loginUser -> {
                    Long userId = loginUser.getUserId();
                    List<SysMenu> menus = menuService.selectMenuTreeByUserId(userId);
                    return AjaxResult.success(menuService.buildMenus(menus));
                });
    }

    // 检查初始密码是否提醒修改
    public Mono<Boolean> initPasswordIsModify(Date pwdUpdateDate) {
        return sysConfigService.selectConfigByKey("sys.account.initPasswordModify")
                .map(Integer::parseInt)
                .map(initPasswordModify -> initPasswordModify == 1 && pwdUpdateDate == null)
                .defaultIfEmpty(Boolean.FALSE);
    }

    // 检查密码是否过期
    public Mono<Boolean> passwordIsExpiration(Date pwdUpdateDate) {
        return sysConfigService.selectConfigByKey("sys.account.passwordValidateDays")
                .map(Integer::parseInt)
                .map(passwordValidateDays -> {
                    if (passwordValidateDays > 0) {
                        if (StringUtils.isNull(pwdUpdateDate)) {
                            // 如果从未修改过初始密码，直接提醒过期
                            return true;
                        }
                        Date nowDate = DateUtils.getNowDate();
                        return DateUtils.differentDaysByMillisecond(nowDate, pwdUpdateDate) > passwordValidateDays;
                    }
                    return false;
                })
                .defaultIfEmpty(Boolean.FALSE);
    }
}
