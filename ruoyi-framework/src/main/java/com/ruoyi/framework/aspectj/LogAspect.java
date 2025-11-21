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

    @SuppressWarnings("ReactiveStreamsUnusedPublisher")
    @Around("@annotation(com.ruoyi.common.annotation.Log)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Log controllerLog = getAnnotationLog(joinPoint);
        long startTime = System.currentTimeMillis();

        try {
            Object proceed = joinPoint.proceed();

            // 处理响应式返回值
            if (proceed instanceof Mono<?> mono) {
                return mono.flatMap(result -> {
                            handleLog(joinPoint, controllerLog, null, result, startTime);
                            return Mono.just(result);
                        })
                        .onErrorResume(e -> {
                            handleLog(joinPoint, controllerLog, e, null, startTime);
                            return Mono.error(e);
                        });
            } else if (proceed instanceof Flux<?> flux) {
                return flux.collectList()
                        .flatMapMany(results -> {
                            handleLog(joinPoint, controllerLog, null, results, startTime);
                            return Flux.fromIterable(results);
                        })
                        .onErrorResume(e -> {
                            handleLog(joinPoint, controllerLog, e, null, startTime);
                            return Flux.error(e);
                        });
            } else {
                // 同步方法
                handleLog(joinPoint, controllerLog, null, proceed, startTime);
                return proceed;
            }
        } catch (Throwable e) {
            handleLog(joinPoint, controllerLog, e, null, startTime);
            throw e;
        }
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

    protected void handleLog(final JoinPoint joinPoint, Log controllerLog, final Throwable e, Object jsonResult, long startTime) {
        try {
            long costTime = System.currentTimeMillis() - startTime;

            ReactiveRequestContextHolder.getExchange()
                    .subscribe(exchange -> {
                        // 保存数据库
                        AsyncManager.me().execute(AsyncFactory.recordOper(joinPoint, controllerLog, e, jsonResult, exchange, costTime));
                    });
        } catch (Exception exp) {
            // 记录本地异常日志
            log.error("异常信息:", exp);
        }
    }

}
