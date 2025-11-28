package com.ruoyi.framework.manager.factory;

import com.ruoyi.common.annotation.Log;
import org.aspectj.lang.JoinPoint;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.TimerTask;

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
     * 操作日志记录
     */
    TimerTask recordOper(final JoinPoint joinPoint, Log controllerLog, final Throwable e, Object jsonResult, ServerWebExchange exchange, long costTime);

}
