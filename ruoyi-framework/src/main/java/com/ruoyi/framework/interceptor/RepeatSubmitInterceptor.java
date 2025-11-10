package com.ruoyi.framework.interceptor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.domain.AjaxResult;
import jakarta.annotation.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.lang.reflect.Method;

/**
 * 防止重复提交拦截器
 *
 * @author ruoyi
 */
public abstract class RepeatSubmitInterceptor implements WebFilter {

    @Resource
    public ObjectMapper objectMapper;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        Object attribute = exchange.getAttribute(HandlerMapping.BEST_MATCHING_HANDLER_ATTRIBUTE);

        if (attribute instanceof HandlerMethod handlerMethod) {
            Method method = handlerMethod.getMethod();
            RepeatSubmit annotation = method.getAnnotation(RepeatSubmit.class);
            if (annotation != null) {
                if (this.isRepeatSubmit(exchange, annotation)) {
                    try {
                        byte[] bytes = objectMapper.writeValueAsBytes(AjaxResult.error(annotation.message()));

                        ServerHttpResponse response = exchange.getResponse();
                        response.setStatusCode(HttpStatus.OK);
                        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

                        return response.writeWith(Mono.just(response.bufferFactory().wrap(bytes)));
                    } catch (JsonProcessingException e) {
                        return Mono.error(e);
                    }
                }
            }
        }

        return chain.filter(exchange);
    }

    /**
     * 验证是否重复提交由子类实现具体的防重复提交的规则
     *
     * @param request    请求信息
     * @param annotation 防重复注解参数
     * @return 结果
     */
    public abstract boolean isRepeatSubmit(ServerWebExchange request, RepeatSubmit annotation);

}
