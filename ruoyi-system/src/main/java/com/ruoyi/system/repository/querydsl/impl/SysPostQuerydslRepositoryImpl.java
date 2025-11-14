package com.ruoyi.system.repository.querydsl.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.r2dbc.R2DBCQueryFactory;
import com.ruoyi.system.entity.SysPost;
import com.ruoyi.system.query.SysPostQuery;
import com.ruoyi.system.repository.querydsl.SysPostQuerydslRepository;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.ruoyi.system.entity.querydsl.QSysPost.sysPost;

@Repository
public class SysPostQuerydslRepositoryImpl implements SysPostQuerydslRepository {

    @Resource
    private R2DBCQueryFactory queryFactory;

    @Override
    public Flux<SysPost> selectPostList(SysPostQuery query) {
        Predicate predicate = buildPredicate(query);

        return queryFactory.selectFrom(sysPost)
                .where(predicate)
                .orderBy(sysPost.createTime.desc())
                .limit(query.limit())
                .offset(query.offset())
                .fetch();
    }

    private Predicate buildPredicate(SysPostQuery query) {
        BooleanBuilder predicate = new BooleanBuilder();

        if (StringUtils.isNotBlank(query.getPostCode())) {
            predicate.and(sysPost.postCode.contains(query.getPostCode()));
        }
        if (StringUtils.isNotBlank(query.getPostName())) {
            predicate.and(sysPost.postName.contains(query.getPostName()));
        }
        if (StringUtils.isNotBlank(query.getStatus())) {
            predicate.and(sysPost.status.eq(query.getStatus()));
        }

        return predicate;
    }

    @Override
    public Mono<Long> selectPostCount(SysPostQuery query) {
        Predicate predicate = buildPredicate(query);

        return queryFactory.selectFrom(sysPost)
                .where(predicate)
                .fetchCount();
    }
}
