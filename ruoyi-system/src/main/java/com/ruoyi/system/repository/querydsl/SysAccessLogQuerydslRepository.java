package com.ruoyi.system.repository.querydsl;

import com.ruoyi.system.entity.SysAccessLog;
import com.ruoyi.system.query.SysAccessLogQuery;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SysAccessLogQuerydslRepository {

    Flux<SysAccessLog> selectListByQuery(SysAccessLogQuery query);

    Mono<Long> selectCountByQuery(SysAccessLogQuery query);

}
