package com.ruoyi.system.repository.querydsl.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.r2dbc.R2DBCQueryFactory;
import com.ruoyi.system.entity.SysOperateLog;
import com.ruoyi.system.query.SysOperateLogQuery;
import com.ruoyi.system.repository.querydsl.SysOperateLogQuerydslRepository;
import com.ruoyi.system.utils.QuerydslExecutionUtils;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.ruoyi.system.entity.querydsl.QSysOperateLog.sysOperateLog;

@Repository
public class SysOperateLogQuerydslRepositoryImpl implements SysOperateLogQuerydslRepository {

    @Resource
    private R2DBCQueryFactory queryFactory;

    @Override
    public Flux<SysOperateLog> selectListByQuery(SysOperateLogQuery query) {
        Predicate predicate = buildPredicate(query);

        return queryFactory.selectFrom(sysOperateLog)
                .where(predicate)
                .orderBy(sysOperateLog.operTime.desc())
                .limit(query.limit())
                .offset(query.offset())
                .fetch();
    }

    private Predicate buildPredicate(SysOperateLogQuery query) {
        BooleanBuilder predicate = new BooleanBuilder();

        if (StringUtils.isNotBlank(query.getOperIp())) {
            predicate.and(sysOperateLog.operIp.contains(query.getOperIp()));
        }
        if (StringUtils.isNotBlank(query.getTitle())) {
            predicate.and(sysOperateLog.title.contains(query.getTitle()));
        }
        if (ObjectUtils.allNotNull(query.getBusinessType())) {
            predicate.and(sysOperateLog.businessType.eq(query.getBusinessType()));
        }
        if (ObjectUtils.allNotNull(query.getStatus())) {
            predicate.and(sysOperateLog.status.eq(query.getStatus()));
        }
        if (StringUtils.isNotBlank(query.getOperName())) {
            predicate.and(sysOperateLog.operName.contains(query.getOperName()));
        }

        QuerydslExecutionUtils.dateRange(predicate, sysOperateLog.operTime, query.getStartTime(), query.getEndTime());

        return predicate;
    }

    @Override
    public Mono<Long> selectCountByQuery(SysOperateLogQuery query) {
        Predicate predicate = buildPredicate(query);

        return queryFactory.selectFrom(sysOperateLog)
                .where(predicate)
                .fetchCount();
    }
}
