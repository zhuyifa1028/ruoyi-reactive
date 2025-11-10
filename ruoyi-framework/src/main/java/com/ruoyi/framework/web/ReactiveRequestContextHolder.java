package com.ruoyi.framework.web;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;
import reactor.util.context.ContextView;

public class ReactiveRequestContextHolder {

    private static final Class<?> REQUEST_CONTEXT_KEY = SecurityContext.class;

    public static Context withRequestContext(ServerWebExchange exchange) {
        return Context.of(REQUEST_CONTEXT_KEY, exchange);
    }

    private static boolean hasRequestContext(ContextView context) {
        return context.hasKey(REQUEST_CONTEXT_KEY);
    }

    private static ServerWebExchange getRequestContext(ContextView context) {
        return context.<ServerWebExchange>get(REQUEST_CONTEXT_KEY);
    }

    public static Mono<ServerWebExchange> getExchange() {
        return Mono.deferContextual(Mono::just)
                .filter(ReactiveRequestContextHolder::hasRequestContext)
                .map(ReactiveRequestContextHolder::getRequestContext);
    }

    public static Mono<ServerHttpRequest> getRequest() {
        return getExchange().map(ServerWebExchange::getRequest);
    }

}
