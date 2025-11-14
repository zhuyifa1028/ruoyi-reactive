package com.ruoyi.system.repository.querydsl.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.r2dbc.R2DBCQueryFactory;
import com.ruoyi.system.entity.SysDict;
import com.ruoyi.system.query.SysDictQuery;
import com.ruoyi.system.repository.querydsl.SysDictQuerydslRepository;
import com.ruoyi.system.utils.QuerydslExecutionUtils;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.ruoyi.system.entity.querydsl.QSysDict.sysDict;

@Repository
public class SysDictQuerydslRepositoryImpl implements SysDictQuerydslRepository {

    @Resource
    private R2DBCQueryFactory queryFactory;

    @Override
    public Flux<SysDict> selectDictList(SysDictQuery query) {
        Predicate predicate = buildPredicate(query);

        return queryFactory.selectFrom(sysDict)
                .where(predicate)
                .orderBy(sysDict.dictSort.asc())
                .limit(query.limit())
                .offset(query.offset())
                .fetch();
    }

    private Predicate buildPredicate(SysDictQuery query) {
        BooleanBuilder predicate = new BooleanBuilder();

        if (StringUtils.isNotBlank(query.getDictType())) {
            predicate.and(sysDict.dictType.eq(query.getDictType()));
        }
        if (StringUtils.isNotBlank(query.getDictLabel())) {
            predicate.and(sysDict.dictLabel.contains(query.getDictLabel()));
        }
        if (StringUtils.isNotBlank(query.getDictValue())) {
            predicate.and(sysDict.dictValue.contains(query.getDictValue()));
        }
        if (StringUtils.isNotBlank(query.getStatus())) {
            predicate.and(sysDict.status.eq(query.getStatus()));
        }

        // 日期范围
        QuerydslExecutionUtils.dateRange(predicate, sysDict.createTime, query.getStartTime(), query.getEndTime());

        return predicate;
    }

    @Override
    public Mono<Long> selectDictCount(SysDictQuery query) {
        Predicate predicate = buildPredicate(query);

        return queryFactory.selectFrom(sysDict)
                .where(predicate)
                .fetchCount();
    }

}
