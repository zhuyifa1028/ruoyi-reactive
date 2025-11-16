package com.ruoyi.framework.web.service;

import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.security.ReactiveSecurityUtils;
import com.ruoyi.framework.security.context.PermissionContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Mono;

import java.util.Set;

/**
 * RuoYi首创 自定义权限实现，ss取自SpringSecurity首字母
 *
 * @author ruoyi
 */
@SuppressWarnings("unused")
@Service("ss")
public class PermissionService {
    /**
     * 验证用户是否具备某权限
     *
     * @param permission 权限字符串
     * @return 用户是否具备某权限
     */
    public Mono<Boolean> hasPermi(String permission) {
        if (StringUtils.isEmpty(permission)) {
            return Mono.just(false);
        }
        return ReactiveSecurityUtils.getLoginUser()
                .map(loginUser -> {
                    if (StringUtils.isNull(loginUser) || CollectionUtils.isEmpty(loginUser.getPermissions())) {
                        return false;
                    }
                    PermissionContextHolder.setContext(permission);
                    return hasPermissions(loginUser.getPermissions(), permission);
                });
    }

    /**
     * 判断用户是否拥有某个角色
     *
     * @param role 角色字符串
     * @return 用户是否具备某角色
     */
    public Mono<Boolean> hasRole(String role) {
        if (StringUtils.isEmpty(role)) {
            return Mono.just(false);
        }
        return ReactiveSecurityUtils.getLoginUser()
                .map(loginUser -> {
                    if (StringUtils.isNull(loginUser) || CollectionUtils.isEmpty(loginUser.getUser().getRoles())) {
                        return false;
                    }
                    for (SysRole sysRole : loginUser.getUser().getRoles()) {
                        String roleKey = sysRole.getRoleKey();
                        if (Constants.SUPER_ADMIN.equals(roleKey) || roleKey.equals(StringUtils.trim(role))) {
                            return true;
                        }
                    }
                    return false;

                });

    }

    /**
     * 判断是否包含权限
     *
     * @param permissions 权限列表
     * @param permission  权限字符串
     * @return 用户是否具备某权限
     */
    private boolean hasPermissions(Set<String> permissions, String permission) {
        return permissions.contains(Constants.ALL_PERMISSION) || permissions.contains(StringUtils.trim(permission));
    }
}
