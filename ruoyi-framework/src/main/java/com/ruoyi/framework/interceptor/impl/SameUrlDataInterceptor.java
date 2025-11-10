package com.ruoyi.framework.interceptor.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.interceptor.RepeatSubmitInterceptor;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 判断请求url和数据是否和上一次相同，
 * 如果和上次相同，则是重复提交表单。 有效时间为10秒内。
 *
 * @author ruoyi
 */
@Component
public class SameUrlDataInterceptor extends RepeatSubmitInterceptor {
    private static final Logger log = LoggerFactory.getLogger(SameUrlDataInterceptor.class);
    public final String REPEAT_PARAMS = "repeatParams";

    public final String REPEAT_TIME = "repeatTime";

    // 令牌自定义标识
    @Value("${token.header}")
    private String header;

    @Resource
    private RedisCache redisCache;

    @Override
    public boolean isRepeatSubmit(ServerWebExchange exchange, RepeatSubmit annotation) {
        AtomicReference<String> nowParams = new AtomicReference<>("");

        ServerHttpRequest request = exchange.getRequest();
        // 请求体
        request.getBody()
                .reduce(new StringBuilder(), (sb, dataBuffer) -> {
                    byte[] bytes = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(bytes);
                    sb.append(new String(bytes, StandardCharsets.UTF_8));
                    return sb;
                })
                .defaultIfEmpty(new StringBuilder())
                .flatMap(sb -> {
                    nowParams.set(sb.toString());
                    return Mono.empty();
                })
                .subscribe();

        // body参数为空，获取Parameter的数据
        if (StringUtils.isEmpty(nowParams.get())) {
            try {
                nowParams.set(objectMapper.writeValueAsString(request.getQueryParams()));
            } catch (JsonProcessingException e) {
                log.error("请求参数转Json字符串异常", e);
            }
        }

        Map<String, Object> nowDataMap = new HashMap<>();
        nowDataMap.put(REPEAT_PARAMS, nowParams);
        nowDataMap.put(REPEAT_TIME, System.currentTimeMillis());

        // 请求地址（作为存放cache的key值）
        String url = request.getURI().getPath();

        // 唯一值（没有消息头则使用请求地址）
        String submitKey = StringUtils.trimToEmpty(request.getHeaders().getFirst(header));

        // 唯一标识（指定key + url + 消息头）
        String cacheRepeatKey = CacheConstants.REPEAT_SUBMIT_KEY + url + submitKey;

        Map<String, Map<String, Object>> cacheObject = redisCache.getCacheObject(cacheRepeatKey);
        if (cacheObject != null && cacheObject.containsKey(url)) {
            Map<String, Object> preDataMap = cacheObject.get(url);
            if (compareParams(nowDataMap, preDataMap) && compareTime(nowDataMap, preDataMap, annotation.interval())) {
                return true;
            }
        }

        Map<String, Map<String, Object>> cacheMap = new HashMap<>();
        cacheMap.put(url, nowDataMap);
        redisCache.setCacheObject(cacheRepeatKey, cacheMap, annotation.interval(), TimeUnit.MILLISECONDS);
        return false;
    }

    /**
     * 判断参数是否相同
     */
    private boolean compareParams(Map<String, Object> nowMap, Map<String, Object> preMap) {
        String nowParams = (String) nowMap.get(REPEAT_PARAMS);
        String preParams = (String) preMap.get(REPEAT_PARAMS);
        return nowParams.equals(preParams);
    }

    /**
     * 判断两次间隔时间
     */
    private boolean compareTime(Map<String, Object> nowMap, Map<String, Object> preMap, int interval) {
        long time1 = (Long) nowMap.get(REPEAT_TIME);
        long time2 = (Long) preMap.get(REPEAT_TIME);
        return (time1 - time2) < interval;
    }
}
