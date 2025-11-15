package com.ruoyi.system.utils;

import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.framework.redis.ReactiveRedisUtils;
import com.ruoyi.system.entity.SysDict;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 字典工具类
 *
 * @author ruoyi
 */
@Component
public class DictUtils {

    private static ReactiveRedisUtils<List<SysDict>> reactiveRedisUtils;

    public DictUtils(ReactiveRedisUtils<List<SysDict>> reactiveRedisUtils) {
        DictUtils.reactiveRedisUtils = reactiveRedisUtils;
    }

    /**
     * 设置字典缓存
     *
     * @param key       参数键
     * @param dictDatas 字典数据列表
     */
    public static Mono<Boolean> setDictCache(String key, List<SysDict> dictDatas) {
        return reactiveRedisUtils.setCacheObject(getCacheKey(key), dictDatas);
    }

    /**
     * 获取字典缓存
     *
     * @param key 参数键
     * @return dictDatas 字典数据列表
     */
    public static Flux<SysDict> getDictCache(String key) {
        return reactiveRedisUtils.getCacheObject(getCacheKey(key))
                .flatMapMany(Flux::fromIterable);
    }

    /**
     * 清空字典缓存
     */
    public static Mono<Long> clearDictCache() {
        Flux<String> keys = reactiveRedisUtils.keys(CacheConstants.SYS_DICT_KEY + "*");
        return reactiveRedisUtils.deleteObject(keys);
    }

    /**
     * 设置cache key
     *
     * @param configKey 参数键
     * @return 缓存键key
     */
    public static String getCacheKey(String configKey) {
        return CacheConstants.SYS_DICT_KEY + configKey;
    }
}
