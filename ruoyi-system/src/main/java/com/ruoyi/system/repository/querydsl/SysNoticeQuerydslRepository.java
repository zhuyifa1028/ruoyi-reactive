package com.ruoyi.system.repository.querydsl;

import com.ruoyi.system.entity.SysNotice;
import com.ruoyi.system.query.SysNoticeQuery;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SysNoticeQuerydslRepository {

    Flux<SysNotice> selectNoticeList(SysNoticeQuery query);

    Mono<Long> selectNoticeCount(SysNoticeQuery query);

}
