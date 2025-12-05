package com.github.yingzhong.framework.webflux.interceptor;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.dromara.common.json.utils.JsonUtils;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 响应式Web调用时间统计过滤器
 *
 * @author yingzhong
 * @version 2025-12-05
 */
@Slf4j
public class PlusInvokeWebFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String url = request.getMethod() + " " + request.getURI().getPath();

        // 创建计时器并放入上下文
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        return preHandle(request, url)
            .then(chain.filter(exchange))
            .doFinally(__ -> afterCompletion(stopWatch, url));
    }

    private Mono<Void> preHandle(ServerHttpRequest request, String url) {
        // 打印请求参数
        if (isJsonRequest(request)) {
            return request.getBody()
                .map(buffer -> {
                    byte[] bytes = new byte[buffer.readableByteCount()];
                    buffer.read(bytes);
                    DataBufferUtils.release(buffer);
                    return new String(bytes, StandardCharsets.UTF_8);
                })
                .reduce(String::concat)
                .doOnNext(body -> log.info("[PLUS]开始请求 => URL[{}],参数类型[json],参数:[{}]", url, body))
                .then();
        } else {
            // 处理表单参数
            Map<String, String[]> parameterMap = new ConcurrentHashMap<>();
            request.getQueryParams().forEach((k, v) ->
                parameterMap.put(k, v.toArray(new String[0]))
            );
            if (MapUtil.isNotEmpty(parameterMap)) {
                String parameters = JsonUtils.toJsonString(parameterMap);
                log.info("[PLUS]开始请求 => URL[{}],参数类型[param],参数:[{}]", url, parameters);
            } else {
                log.info("[PLUS]开始请求 => URL[{}],无参数", url);
            }
            return Mono.empty();
        }
    }

    private void afterCompletion(StopWatch stopWatch, String url) {
        if (ObjectUtil.isNotNull(stopWatch)) {
            stopWatch.stop();
            log.info("[PLUS]结束请求 => URL[{}],耗时:[{}]毫秒", url, stopWatch.getDuration().toMillis());
        }
    }

    private boolean isJsonRequest(ServerHttpRequest request) {
        MediaType contentType = request.getHeaders().getContentType();
        if (Objects.nonNull(contentType)) {
            return contentType.includes(MediaType.APPLICATION_JSON);
        }
        return false;
    }

}
