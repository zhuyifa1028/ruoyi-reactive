package com.ruoyi.framework.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * 响应式安全工具类
 *
 * @author bugout
 * @version 2025-11-11
 */
@Component
public class ReactiveSecurityUtils {

    private static PasswordEncoder passwordEncoder;

    public ReactiveSecurityUtils(PasswordEncoder passwordEncoder) {
        ReactiveSecurityUtils.passwordEncoder = passwordEncoder;
    }

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

    /**
     * 生成BCryptPasswordEncoder密码
     *
     * @param password 密码
     * @return 加密字符串
     */
    public static String encryptPassword(String password) {
        return passwordEncoder.encode(password);
    }

    /**
     * 判断密码是否相同
     *
     * @param rawPassword     真实密码
     * @param encodedPassword 加密后字符
     * @return 结果
     */
    public static boolean matchesPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

}
