package com.ruoyi.framework.security.filter;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.web.ReactiveRequestContextHolder;
import com.ruoyi.framework.web.service.TokenService;
import jakarta.annotation.Resource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
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
        return tokenService.getLoginUser(exchange)
                .flatMap(loginUser -> {
                    if (StringUtils.isNotNull(loginUser)) {
                        tokenService.verifyToken(loginUser);
                        var authentication = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
                        var context = new SecurityContextImpl(authentication);
                        return chain.filter(exchange)
                                .contextWrite(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(context)))
                                .contextWrite(ReactiveRequestContextHolder.withRequestContext(exchange));
                    }
                    return Mono.empty();
                })
                .then(chain.filter(exchange).contextWrite(context -> context.put(ServerWebExchange.class, exchange)));
    }

}
