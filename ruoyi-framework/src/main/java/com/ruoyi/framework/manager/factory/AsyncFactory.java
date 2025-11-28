package com.ruoyi.framework.manager.factory;

import com.ruoyi.common.annotation.Log;
import org.aspectj.lang.JoinPoint;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 异步工厂（产生任务用）
 *
 * @author ruoyi
 */
public interface AsyncFactory {

    /**
     * 记录访问信息
     */
    Mono<Void> recordAccessInfo(ServerWebExchange exchange, String username, String status, String message, Object... args);

    /**
     * 记录操作信息
     */
    Mono<Void> recordOperateInfo(ServerWebExchange exchange, JoinPoint joinPoint, Log controllerLog, Throwable e, Object jsonResult, long startTime);

}
