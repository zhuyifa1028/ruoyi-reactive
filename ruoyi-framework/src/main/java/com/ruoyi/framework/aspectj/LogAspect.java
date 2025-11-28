package com.ruoyi.framework.aspectj;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.framework.manager.AsyncManager;
import com.ruoyi.framework.manager.factory.AsyncFactory;
import com.ruoyi.framework.web.ReactiveRequestContextHolder;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.reflect.Method;

/**
 * 操作日志记录处理
 *
 * @author ruoyi
 */
@Slf4j
@Aspect
@Component
public class LogAspect {

    @Resource
    private AsyncFactory AsyncFactory;

    @Around("@annotation(com.ruoyi.common.annotation.Log)")
    public Mono<Object> around(ProceedingJoinPoint joinPoint) {
        Log controllerLog = getAnnotationLog(joinPoint);
        long startTime = System.currentTimeMillis();

        return ReactiveRequestContextHolder.getExchange()
                .flatMap(exchange -> {
                    try {
                        Object proceed = joinPoint.proceed();

                        // 处理响应式返回值
                        if (proceed instanceof Mono<?> mono) {
                            return mono.flatMap(result -> {
                                        handleLog(joinPoint, controllerLog, null, result, startTime, exchange);
                                        return Mono.just(result);
                                    })
                                    .onErrorResume(e -> {
                                        handleLog(joinPoint, controllerLog, e, null, startTime, exchange);
                                        return Mono.error(e);
                                    });
                        } else if (proceed instanceof Flux<?> flux) {
                            return flux.collectList()
                                    .flatMap(results -> {
                                        handleLog(joinPoint, controllerLog, null, results, startTime, exchange);
                                        return Mono.just(results);
                                    })
                                    .onErrorResume(e -> {
                                        handleLog(joinPoint, controllerLog, e, null, startTime, exchange);
                                        return Mono.error(e);
                                    });
                        } else {
                            // 同步方法
                            handleLog(joinPoint, controllerLog, null, proceed, startTime, exchange);
                            return Mono.just(proceed);
                        }
                    } catch (Throwable e) {
                        handleLog(joinPoint, controllerLog, e, null, startTime, exchange);
                        return Mono.error(e);
                    }
                });
    }

    /**
     * 更安全的获取注解（自动处理代理类）
     */
    private Log getAnnotationLog(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Log logAnn = method.getAnnotation(Log.class);
        if (logAnn != null) {
            return logAnn;
        }

        // 处理代理类的方法
        try {
            Method realMethod = joinPoint.getTarget()
                    .getClass()
                    .getMethod(signature.getName(), method.getParameterTypes());
            return realMethod.getAnnotation(Log.class);
        } catch (Exception ignored) {
        }

        return null;
    }

    protected void handleLog(final JoinPoint joinPoint, Log controllerLog, final Throwable e, Object jsonResult, long startTime, ServerWebExchange exchange) {
        try {
            long costTime = System.currentTimeMillis() - startTime;

            // 保存数据库
            AsyncManager.me().execute(AsyncFactory.recordOper(joinPoint, controllerLog, e, jsonResult, exchange, costTime));
        } catch (Exception exp) {
            // 记录本地异常日志
            log.error("异常信息:", exp);
        }
    }

}
