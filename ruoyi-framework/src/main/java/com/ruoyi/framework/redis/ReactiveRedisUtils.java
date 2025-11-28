package com.ruoyi.framework.redis;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 响应式redis工具类
 *
 * @author bugout
 * @version 2025-11-15
 **/
@SuppressWarnings("unused")
@Slf4j
@Component
public class ReactiveRedisUtils<T> {

    @Resource
    private ReactiveRedisOperations<String, T> reactiveRedisOperations;

    /**
     * 获得缓存的基本对象列表
     *
     * @param pattern 字符串前缀
     * @return 对象列表
     */
    public Flux<String> keys(String pattern) {
        return reactiveRedisOperations.keys(pattern);
    }

    /**
     * 基于 Redis SCAN 命令的响应式扫描方法（替代 KEYS，避免阻塞 Redis）
     */
    public Flux<String> scan(String pattern) {
        // 构建 SCAN 选项：匹配模式+批次大小（count 建议 50-200，平衡效率与 Redis 压力）
        ScanOptions options = ScanOptions.scanOptions()
                .match(pattern)
                .count(100)
                .build();

        return reactiveRedisOperations.scan(options)
                .onErrorResume(e -> {
                    log.error("Redis SCAN 命令执行失败，pattern: {}", pattern, e);
                    return Flux.empty();
                });
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key   缓存的键值
     * @param value 缓存的值
     */
    public Mono<Boolean> setCacheObject(String key, T value) {
        return reactiveRedisOperations.opsForValue().set(key, value);
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key     缓存的键值
     * @param value   缓存的值
     * @param timeout 时间
     */
    public Mono<Boolean> setCacheObject(String key, T value, Duration timeout) {
        return reactiveRedisOperations.opsForValue().set(key, value, timeout);
    }

    /**
     * 设置有效时间
     *
     * @param key     Redis键
     * @param timeout 超时时间
     * @return true=设置成功；false=设置失败
     */
    public Mono<Boolean> expire(String key, long timeout) {
        return expire(key, Duration.ofSeconds(timeout));
    }

    /**
     * 设置有效时间
     *
     * @param key     Redis键
     * @param timeout 超时时间
     * @return true=设置成功；false=设置失败
     */
    public Mono<Boolean> expire(String key, Duration timeout) {
        return reactiveRedisOperations.expire(key, timeout);
    }

    /**
     * 获取有效时间
     *
     * @param key Redis键
     * @return 有效时间
     */
    public Mono<Duration> getExpire(String key) {
        return reactiveRedisOperations.getExpire(key);
    }

    /**
     * 判断 key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public Mono<Boolean> hasKey(String key) {
        return reactiveRedisOperations.hasKey(key);
    }

    /**
     * 获得缓存的基本对象。
     *
     * @param key 缓存键值
     * @return 缓存键值对应的数据
     */
    public Mono<T> getCacheObject(String key) {
        ReactiveValueOperations<String, T> opsForValue = reactiveRedisOperations.opsForValue();
        return opsForValue.get(key);
    }

    /**
     * 删除单个对象
     *
     */
    public Mono<Long> deleteObject(String key) {
        return reactiveRedisOperations.delete(key);
    }

    /**
     * 删除集合对象
     *
     */
    public Mono<Long> deleteObject(Publisher<String> keys) {
        return reactiveRedisOperations.delete(keys);
    }

    /**
     * 缓存List数据
     *
     * @param key      缓存的键值
     * @param dataList 待缓存的List数据
     * @return 缓存的对象
     */
    public Mono<Long> setCacheList(String key, List<T> dataList) {
        return reactiveRedisOperations.opsForList().rightPushAll(key, dataList);
    }

    /**
     * 获得缓存的list对象
     *
     * @param key 缓存的键值
     * @return 缓存键值对应的数据
     */
    public Flux<T> getCacheList(String key) {
        return reactiveRedisOperations.opsForList().range(key, 0, -1);
    }

    /**
     * 获得缓存的set
     */
    public Flux<T> getCacheSet(String key) {
        return reactiveRedisOperations.opsForSet().members(key);
    }

    /**
     * 缓存Map
     */
    public Mono<Boolean> setCacheMap(String key, Map<String, T> dataMap) {
        if (Objects.nonNull(dataMap)) {
            return reactiveRedisOperations.opsForHash().putAll(key, dataMap);
        }
        return Mono.empty();
    }

    /**
     * 获得缓存的Map
     *
     */
    public Flux<Map.Entry<String, T>> getCacheMap(String key) {
        ReactiveHashOperations<String, String, T> opsForHash = reactiveRedisOperations.opsForHash();
        return opsForHash.entries(key);
    }

    /**
     * 往Hash中存入数据
     *
     * @param key   Redis键
     * @param hKey  Hash键
     * @param value 值
     */
    public Mono<Boolean> setCacheMapValue(String key, String hKey, T value) {
        ReactiveHashOperations<String, String, T> opsForHash = reactiveRedisOperations.opsForHash();
        return opsForHash.put(key, hKey, value);
    }

    /**
     * 获取Hash中的数据
     *
     * @param key  Redis键
     * @param hKey Hash键
     * @return Hash中的对象
     */
    public Mono<T> getCacheMapValue(String key, String hKey) {
        ReactiveHashOperations<String, String, T> opsForHash = reactiveRedisOperations.opsForHash();
        return opsForHash.get(key, hKey);
    }

    /**
     * 获取多个Hash中的数据
     *
     * @param key   Redis键
     * @param hKeys Hash键集合
     * @return Hash对象集合
     */
    public Mono<List<T>> getMultiCacheMapValue(String key, Collection<String> hKeys) {
        ReactiveHashOperations<String, String, T> opsForHash = reactiveRedisOperations.opsForHash();
        return opsForHash.multiGet(key, hKeys);
    }

    /**
     * 删除Hash中的某条数据
     *
     * @param key  Redis键
     * @param hKey Hash键
     * @return 是否成功
     */
    public Mono<Long> deleteCacheMapValue(String key, String hKey) {
        return reactiveRedisOperations.opsForHash().remove(key, hKey);
    }

}
