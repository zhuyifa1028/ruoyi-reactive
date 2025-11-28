package com.ruoyi.framework.aspectj;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.framework.manager.factory.AsyncFactory;
import com.ruoyi.framework.web.ReactiveRequestContextHolder;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
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
    private AsyncFactory asyncFactory;

    @Around("@annotation(com.ruoyi.common.annotation.Log)")
    public Mono<Object> around(ProceedingJoinPoint joinPoint) {
        Log logAnnotation = getLogAnnotation(joinPoint);

        long startTime = System.currentTimeMillis();

        return ReactiveRequestContextHolder.getExchange()
                .flatMap(exchange -> {
                    try {
                        Object proceed = joinPoint.proceed();

                        // 处理响应式返回值
                        if (proceed instanceof Mono<?> mono) {
                            return mono.flatMap(result ->
                                            asyncFactory.recordOperateInfo(exchange, joinPoint, logAnnotation, null, result, startTime).thenReturn(result)
                                    )
                                    .onErrorResume(e ->
                                            asyncFactory.recordOperateInfo(exchange, joinPoint, logAnnotation, e, null, startTime).then(Mono.error(e))
                                    );
                        } else if (proceed instanceof Flux<?> flux) {
                            return flux.collectList()
                                    .flatMap(result ->
                                            asyncFactory.recordOperateInfo(exchange, joinPoint, logAnnotation, null, result, startTime).thenReturn(result)
                                    )
                                    .onErrorResume(e ->
                                            asyncFactory.recordOperateInfo(exchange, joinPoint, logAnnotation, e, null, startTime).then(Mono.error(e))
                                    );
                        } else {
                            // 同步方法
                            return asyncFactory.recordOperateInfo(exchange, joinPoint, logAnnotation, null, proceed, startTime).thenReturn(proceed);
                        }
                    } catch (Throwable e) {
                        return asyncFactory.recordOperateInfo(exchange, joinPoint, logAnnotation, e, null, startTime).then(Mono.error(e));
                    }
                });
    }

    /**
     * 更安全的获取注解（自动处理代理类）
     */
    private Log getLogAnnotation(ProceedingJoinPoint joinPoint) {
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

}
