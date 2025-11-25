package com.ruoyi.system.utils;

import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.framework.redis.ReactiveRedisUtils;
import com.ruoyi.system.entity.SysDept;
import com.ruoyi.system.repository.SysDeptRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 * 字典工具类
 *
 * @author ruoyi
 */
@Component
public class DeptUtils {

    private static ReactiveRedisUtils<SysDept> reactiveRedisUtils;

    private static SysDeptRepository sysDeptRepository;

    public DeptUtils(ReactiveRedisUtils<SysDept> reactiveRedisUtils, SysDeptRepository sysDeptRepository) {
        DeptUtils.reactiveRedisUtils = reactiveRedisUtils;
        DeptUtils.sysDeptRepository = sysDeptRepository;
    }

    public static String getCacheKey(Long deptId) {
        return CacheConstants.SYS_DEPT_KEY + deptId;
    }

    public static Mono<String> getDeptName(Long deptId) {
        return reactiveRedisUtils.getCacheObject(getCacheKey(deptId))
                .switchIfEmpty(Mono.defer(() -> {
                    // 查询数据库
                    return sysDeptRepository.findById(deptId)
                            .flatMap(dept -> {
                                // 缓存到redis
                                return reactiveRedisUtils.setCacheObject(getCacheKey(deptId), dept, Duration.ofMinutes(30))
                                        .thenReturn(dept);
                            });
                }))
                .map(SysDept::getDeptName)
                .onErrorReturn("-");
    }

}
