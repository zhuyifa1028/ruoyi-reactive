package com.ruoyi.framework.web.service;

import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.exception.user.UserPasswordNotMatchException;
import com.ruoyi.common.exception.user.UserPasswordRetryLimitExceedException;
import com.ruoyi.framework.redis.ReactiveRedisUtils;
import com.ruoyi.framework.security.ReactiveSecurityUtils;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 * 登录密码方法
 *
 * @author ruoyi
 */
@Component
public class SysPasswordService {

    @Resource
    private ReactiveRedisUtils<Integer> reactiveRedisUtils;

    @Value(value = "${user.password.maxRetryCount}")
    private int maxRetryCount;

    @Value(value = "${user.password.lockTime}")
    private int lockTime;

    /**
     * 登录账户密码错误次数缓存键名
     *
     * @param username 用户名
     * @return 缓存键key
     */
    private String getCacheKey(String username) {
        return CacheConstants.PWD_ERR_CNT_KEY + username;
    }

    public Mono<Void> validate(SysUser user) {
        return ReactiveSecurityUtils.getAuthentication()
                .flatMap(authentication -> {
                    String username = authentication.getName();
                    String password = authentication.getCredentials().toString();

                    return reactiveRedisUtils.getCacheObject(getCacheKey(username))
                            .defaultIfEmpty(0)
                            .flatMap(retryCount -> {
                                if (retryCount >= maxRetryCount) {
                                    return Mono.error(new UserPasswordRetryLimitExceedException(maxRetryCount, lockTime));
                                }

                                if (ReactiveSecurityUtils.matchesPassword(password, user.getPassword())) {
                                    return clearLoginRecordCache(username);
                                } else {
                                    retryCount = retryCount + 1;
                                    return reactiveRedisUtils.setCacheObject(getCacheKey(username), retryCount, Duration.ofMinutes(lockTime))
                                            .then(Mono.error(new UserPasswordNotMatchException()));
                                }
                            });
                });
    }

    public Mono<Void> clearLoginRecordCache(String loginName) {
        return reactiveRedisUtils.hasKey(getCacheKey(loginName))
                .flatMap(hasKey -> {
                    if (hasKey) {
                        return reactiveRedisUtils.deleteObject(getCacheKey(loginName));
                    }
                    return Mono.empty();
                })
                .then();
    }
}
