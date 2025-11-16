package com.ruoyi.framework.security.filter;

import com.ruoyi.framework.web.ReactiveRequestContextHolder;
import com.ruoyi.framework.web.service.TokenService;
import jakarta.annotation.Resource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * token过滤器 验证token有效性
 *
 * @author ruoyi
 */
@Component
public class JwtAuthenticationTokenFilter implements WebFilter {

    @Resource
    private TokenService tokenService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        var unauthenticated = UsernamePasswordAuthenticationToken.unauthenticated(null, null);

        var filter = chain.filter(exchange).contextWrite(ReactiveRequestContextHolder.withRequestContext(exchange));

        // 从请求中获取登录用户
        return tokenService.getLoginUser(exchange)
                .flatMap(loginUser -> {
                    // 验证token有效性
                    return tokenService.verifyToken(loginUser)
                            .map(verifyToken -> {
                                if (verifyToken) {
                                    return UsernamePasswordAuthenticationToken.authenticated(loginUser, null, loginUser.getAuthorities());
                                }
                                return unauthenticated;
                            });
                })
                .defaultIfEmpty(unauthenticated)
                .flatMap(authentication -> {
                    if (authentication.isAuthenticated()) {
                        // 将认证信息设置到上下文
                        return filter.contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
                    }
                    return filter;
                });
    }

}
