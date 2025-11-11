package com.ruoyi.system.repository;

import com.ruoyi.system.entity.SysConfig;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

/**
 * 配置表 数据层
 *
 * @author bugout
 * @version 2025-11-11
 */
public interface SysConfigRepository extends R2dbcRepository<SysConfig, Long> {

    Mono<SysConfig> findByConfigKey(String configKey);

}
