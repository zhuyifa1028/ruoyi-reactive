package com.ruoyi.system.repository.querydsl.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.r2dbc.R2DBCQueryFactory;
import com.ruoyi.system.entity.SysNotice;
import com.ruoyi.system.query.SysNoticeQuery;
import com.ruoyi.system.repository.querydsl.SysNoticeQuerydslRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.ruoyi.system.entity.querydsl.QSysNotice.sysNotice;

@Repository
public class SysNoticeQuerydslRepositoryImpl implements SysNoticeQuerydslRepository {

    @Resource
    private R2DBCQueryFactory queryFactory;

    @Override
    public Flux<SysNotice> selectNoticeList(SysNoticeQuery query) {
        Predicate predicate = buildPredicate(query);

        return queryFactory.selectFrom(sysNotice)
                .where(predicate)
                .orderBy(sysNotice.createTime.desc())
                .limit(query.limit())
                .offset(query.offset())
                .fetch();
    }

    private Predicate buildPredicate(SysNoticeQuery query) {
        BooleanBuilder predicate = new BooleanBuilder();

        if (StringUtils.hasText(query.getNoticeType())) {
            predicate.and(sysNotice.noticeType.eq(query.getNoticeType()));
        }
        if (StringUtils.hasText(query.getNoticeTitle())) {
            predicate.and(sysNotice.noticeTitle.contains(query.getNoticeTitle()));
        }
        if (StringUtils.hasText(query.getCreateBy())) {
            predicate.and(sysNotice.createBy.contains(query.getCreateBy()));
        }

        return predicate;
    }

    @Override
    public Mono<Long> selectNoticeCount(SysNoticeQuery query) {
        Predicate predicate = buildPredicate(query);

        return queryFactory.selectFrom(sysNotice)
                .where(predicate)
                .fetchCount();
    }
}
