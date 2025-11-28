package com.ruoyi.system.repository.querydsl.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.r2dbc.R2DBCQueryFactory;
import com.ruoyi.system.entity.SysAccessLog;
import com.ruoyi.system.query.SysAccessLogQuery;
import com.ruoyi.system.repository.querydsl.SysAccessLogQuerydslRepository;
import com.ruoyi.system.utils.QuerydslExecutionUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.ruoyi.system.entity.querydsl.QSysAccessLog.sysLogininfor;

@Repository
public class SysAccessLogQuerydslRepositoryImpl implements SysAccessLogQuerydslRepository {

    @Resource
    private R2DBCQueryFactory queryFactory;

    @Override
    public Flux<SysAccessLog> selectListByQuery(SysAccessLogQuery query) {
        Predicate predicate = buildPredicate(query);

        return queryFactory.selectFrom(sysLogininfor)
                .where(predicate)
                .orderBy(sysLogininfor.accessTime.desc())
                .limit(query.limit())
                .offset(query.offset())
                .fetch();
    }

    private Predicate buildPredicate(SysAccessLogQuery query) {
        BooleanBuilder predicate = new BooleanBuilder();

        if (StringUtils.hasText(query.getIpaddr())) {
            predicate.and(sysLogininfor.ipaddr.contains(query.getIpaddr()));
        }
        if (StringUtils.hasText(query.getUserName())) {
            predicate.and(sysLogininfor.userName.contains(query.getUserName()));
        }
        if (StringUtils.hasText(query.getStatus())) {
            predicate.and(sysLogininfor.status.eq(query.getStatus()));
        }

        QuerydslExecutionUtils.dateRange(predicate, sysLogininfor.accessTime, query.getStartTime(), query.getEndTime());

        return predicate;
    }

    @Override
    public Mono<Long> selectCountByQuery(SysAccessLogQuery query) {
        Predicate predicate = buildPredicate(query);

        return queryFactory.selectFrom(sysLogininfor)
                .where(predicate)
                .fetchCount();
    }
}
