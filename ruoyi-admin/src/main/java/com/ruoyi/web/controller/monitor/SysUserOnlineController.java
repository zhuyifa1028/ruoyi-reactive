package com.ruoyi.web.controller.monitor;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.redis.ReactiveRedisUtils;
import com.ruoyi.framework.security.LoginUser;
import com.ruoyi.system.service.ISysUserOnlineService;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Collections;

/**
 * 在线用户监控
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/monitor/online")
public class SysUserOnlineController extends BaseController {

    @Resource
    private ISysUserOnlineService userOnlineService;

    @Resource
    private ReactiveRedisUtils<LoginUser> reactiveRedisUtils;

    @PreAuthorize("hasAuthority('monitor:online:list')")
    @GetMapping("/list")
    public Mono<TableDataInfo> list(String ipaddr, String userName) {
        return reactiveRedisUtils.keys(CacheConstants.LOGIN_TOKEN_KEY + "*")
                .flatMap(key -> {
                    // 登录用户
                    return reactiveRedisUtils.getCacheObject(key)
                            .map(user -> {
                                if (StringUtils.isNotEmpty(ipaddr) && StringUtils.isNotEmpty(userName)) {
                                    return userOnlineService.selectOnlineByInfo(ipaddr, userName, user);
                                } else if (StringUtils.isNotEmpty(ipaddr)) {
                                    return userOnlineService.selectOnlineByIpaddr(ipaddr, user);
                                } else if (StringUtils.isNotEmpty(userName) && StringUtils.isNotNull(user.getUser())) {
                                    return userOnlineService.selectOnlineByUserName(userName, user);
                                } else {
                                    return userOnlineService.loginUserToUserOnline(user);
                                }
                            });
                })
                .collectList()
                .map(userOnlineList -> {
                    Collections.reverse(userOnlineList);
                    userOnlineList.removeAll(Collections.singleton(null));
                    return getDataTable(userOnlineList);
                });
    }

    /**
     * 强退用户
     */
    @PreAuthorize("hasAuthority('monitor:online:forceLogout')")
    @Log(title = "在线用户", businessType = BusinessType.FORCE)
    @DeleteMapping("/{tokenId}")
    public Mono<AjaxResult> forceLogout(@PathVariable String tokenId) {
        return reactiveRedisUtils.deleteObject(CacheConstants.LOGIN_TOKEN_KEY + tokenId)
                .thenReturn(success());
    }

}
