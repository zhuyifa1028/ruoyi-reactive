package com.ruoyi.system.repository.querydsl;

import com.ruoyi.system.entity.SysPost;
import com.ruoyi.system.query.SysPostQuery;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SysPostQuerydslRepository {

    Flux<SysPost> selectPostList(SysPostQuery query);

    Mono<Long> selectPostCount(SysPostQuery query);

}
