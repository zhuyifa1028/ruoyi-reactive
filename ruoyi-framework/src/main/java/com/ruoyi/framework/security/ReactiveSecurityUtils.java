package com.ruoyi.framework.security;

import com.ruoyi.common.core.domain.model.LoginUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import reactor.core.publisher.Mono;

/**
 * 响应式安全工具类
 *
 * @author bugout
 * @version 2025-11-11
 */
public class ReactiveSecurityUtils {

    /**
     * 获取Authentication
     */
    public static Mono<Authentication> getAuthentication() {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication);
    }

    /**
     * 获取用户
     */
    public static Mono<LoginUser> getLoginUser() {
        return getAuthentication()
                .map(Authentication::getPrincipal)
                .cast(LoginUser.class);
    }

    /**
     * 获取用户账户
     */
    public static Mono<String> getUsername() {
        return getLoginUser()
                .map(LoginUser::getUsername);
    }

    /**
     * 获取用户ID
     */
    public static Mono<Long> getUserId() {
        return getLoginUser()
                .map(LoginUser::getUserId);
    }

}
