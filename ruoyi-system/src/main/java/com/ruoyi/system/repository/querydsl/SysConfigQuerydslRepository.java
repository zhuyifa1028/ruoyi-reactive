package com.ruoyi.system.repository.querydsl;

import com.ruoyi.system.entity.SysConfig;
import com.ruoyi.system.query.SysConfigQuery;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SysConfigQuerydslRepository {

    Flux<SysConfig> selectConfigList(SysConfigQuery query);

    Mono<Long> selectConfigCount(SysConfigQuery query);

}
