package com.github.yingzhong.framework.webflux.interceptor;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 响应式请求体装饰器（用于缓存请求体以便多次读取）
 */
public class CachedBodyWebFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        // 仅对需要的请求缓存 body，比如 POST/PUT/DELETE
        if (request.getMethod() == HttpMethod.GET || request.getHeaders().getContentLength() == 0) {
            return chain.filter(exchange);
        }

        return DataBufferUtils.join(request.getBody())
            .flatMap(dataBuffer -> {

                byte[] bytes = new byte[dataBuffer.readableByteCount()];
                dataBuffer.read(bytes);
                DataBufferUtils.release(dataBuffer);

                // 包装 request
                ServerHttpRequest decoratedRequest = new ServerHttpRequestDecorator(request) {
                    @Override
                    public Flux<DataBuffer> getBody() {
                        return Flux.defer(() -> {
                            DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
                            return Mono.just(buffer);
                        });
                    }
                };

                return chain.filter(exchange.mutate().request(decoratedRequest).build());
            });
    }
}
