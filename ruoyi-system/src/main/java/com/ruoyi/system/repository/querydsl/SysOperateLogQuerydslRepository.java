package com.ruoyi.system.repository.querydsl;

import com.ruoyi.system.entity.SysOperateLog;
import com.ruoyi.system.query.SysOperateLogQuery;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SysOperateLogQuerydslRepository {

    Flux<SysOperateLog> selectListByQuery(SysOperateLogQuery query);

    Mono<Long> selectCountByQuery(SysOperateLogQuery query);

}
