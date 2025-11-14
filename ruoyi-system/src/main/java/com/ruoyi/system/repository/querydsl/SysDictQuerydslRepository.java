package com.ruoyi.system.repository.querydsl;

import com.ruoyi.system.entity.SysDict;
import com.ruoyi.system.query.SysDictQuery;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SysDictQuerydslRepository {

    Flux<SysDict> selectDictList(SysDictQuery query);

    Mono<Long> selectDictCount(SysDictQuery query);

}
