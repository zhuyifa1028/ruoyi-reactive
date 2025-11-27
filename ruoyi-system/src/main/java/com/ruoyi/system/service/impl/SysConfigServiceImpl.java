package com.ruoyi.system.service.impl;

import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.redis.ReactiveRedisUtils;
import com.ruoyi.system.converter.SysConfigConverter;
import com.ruoyi.system.dto.SysConfigDTO;
import com.ruoyi.system.query.SysConfigQuery;
import com.ruoyi.system.repository.SysConfigRepository;
import com.ruoyi.system.service.SysConfigService;
import com.ruoyi.system.vo.SysConfigVO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.support.ReactivePageableExecutionUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 配置表 业务实现
 *
 * @author bugout
 * @version 2025-11-11
 */
@Slf4j
@Service
public class SysConfigServiceImpl implements SysConfigService, ApplicationRunner {

    @Resource
    private SysConfigConverter sysConfigConverter;
    @Resource
    private SysConfigRepository sysConfigRepository;

    @Resource
    private ReactiveRedisUtils<String> reactiveRedisUtils;

    /**
     * 项目启动时，初始化配置到缓存
     */
    @Override
    public void run(ApplicationArguments args) {
        this.resetConfigCache()
                .doOnSuccess(__ -> log.info("初始化配置到缓存成功"))
                .doOnError(e -> log.error("初始化配置到缓存失败", e))
                .subscribe();
    }

    /**
     * 获取配置列表
     */
    @Override
    public Mono<Page<SysConfigVO>> selectConfigList(SysConfigQuery query) {
        return sysConfigRepository.selectConfigList(query)
                .map(sysConfigConverter::toSysConfigVO)
                .collectList()
                .flatMap(list -> {
                    Mono<Long> count = sysConfigRepository.selectConfigCount(query);
                    return ReactivePageableExecutionUtils.getPage(list, query.pageable(), count);
                });
    }

    /**
     * 根据配置ID获取详细信息
     */
    @Override
    public Mono<SysConfigVO> selectConfigById(Long configId) {
        return sysConfigRepository.findById(configId)
                .switchIfEmpty(Mono.error(new ServiceException("配置不存在")))
                .map(sysConfigConverter::toSysConfigVO);
    }

    /**
     * 根据配置键名查询配置值
     */
    @Override
    public Mono<String> selectConfigByKey(String configKey) {
        // 先从redis取
        return reactiveRedisUtils.getCacheObject(getCacheKey(configKey))
                .switchIfEmpty(
                        // 查询数据库
                        sysConfigRepository.findByConfigKey(configKey)
                                .flatMap(config -> {
                                    // 缓存到redis
                                    return reactiveRedisUtils.setCacheObject(getCacheKey(configKey), config.getConfigValue())
                                            .thenReturn(config.getConfigValue());
                                })
                );
    }

    /**
     * 获取验证码开关
     */
    @Override
    public Mono<Boolean> selectCaptchaEnabled() {
        return this.selectConfigByKey("sys.account.captchaEnabled")
                .map(BooleanUtils::toBoolean)
                .defaultIfEmpty(Boolean.FALSE);
    }

    /**
     * 新增配置
     */
    @Override
    public Mono<Boolean> insertConfig(SysConfigDTO dto) {
        // 检查参数键名
        return this.checkConfigKeyUnique(dto)
                // 保存到数据库
                .then(sysConfigRepository.save(sysConfigConverter.toSysConfig(dto)))
                // 缓存到redis
                .then(reactiveRedisUtils.setCacheObject(getCacheKey(dto.getConfigKey()), dto.getConfigValue()));
    }

    /**
     * 校验参数键名是否唯一
     */
    private Mono<Void> checkConfigKeyUnique(SysConfigDTO dto) {
        return sysConfigRepository.findByConfigKey(dto.getConfigKey())
                .flatMap(info -> {
                    if (ObjectUtils.notEqual(info.getConfigId(), dto.getConfigId())) {
                        return Mono.error(new ServiceException(String.format("参数【%1$s】已存在 ", dto.getConfigKey())));
                    }
                    return Mono.empty();
                });
    }

    /**
     * 修改配置
     */
    @Override
    public Mono<Void> updateConfig(SysConfigDTO dto) {
        // 检查参数键名
        return this.checkConfigKeyUnique(dto)
                .then(sysConfigRepository.findById(dto.getConfigId()))
                .switchIfEmpty(Mono.error(new ServiceException("配置不存在")))
                .flatMap(config -> {
                    String oldConfigKey = config.getConfigKey();

                    // 保存到数据库
                    return sysConfigRepository.save(sysConfigConverter.toSysConfig(dto, config))
                            // 如果configKey发生变化，需要删除旧缓存
                            .then(ObjectUtils.notEqual(oldConfigKey, dto.getConfigKey()) ? reactiveRedisUtils.deleteObject(getCacheKey(oldConfigKey)) : Mono.empty())
                            // 缓存到redis
                            .then(reactiveRedisUtils.setCacheObject(getCacheKey(dto.getConfigKey()), dto.getConfigValue()));
                })
                .then();
    }

    /**
     * 批量删除配置
     */
    @Override
    public Mono<Void> deleteConfigByIds(List<Long> configIds) {
        return sysConfigRepository.findAllById(configIds)
                .flatMap(config -> {
                    if (StringUtils.equals(UserConstants.YES, config.getConfigType())) {
                        return Mono.error(new ServiceException(String.format("内置参数【%1$s】不能删除 ", config.getConfigKey())));
                    }
                    // 删除配置
                    return sysConfigRepository.delete(config)
                            // 删除redis缓存
                            .then(reactiveRedisUtils.deleteObject(getCacheKey(config.getConfigKey())));
                })
                .then();
    }

    /**
     * 重置参数缓存数据
     */
    @Override
    public Mono<Void> resetConfigCache() {
        // 获取缓存key
        return reactiveRedisUtils.keys(CacheConstants.SYS_CONFIG_KEY + "*")
                // 批量删除缓存
                .transform(keys -> reactiveRedisUtils.deleteObject(keys))
                // 查询所有配置
                .thenMany(sysConfigRepository.findAll())
                // 缓存到redis
                .flatMap(config -> reactiveRedisUtils.setCacheObject(getCacheKey(config.getConfigKey()), config.getConfigValue()))
                // 结束
                .then();
    }

    /**
     * 设置cache key
     */
    private String getCacheKey(String configKey) {
        return CacheConstants.SYS_CONFIG_KEY + configKey;
    }

}
