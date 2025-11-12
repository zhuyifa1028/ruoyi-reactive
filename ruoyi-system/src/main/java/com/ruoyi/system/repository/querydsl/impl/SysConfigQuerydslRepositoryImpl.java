package com.ruoyi.system.repository.querydsl.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.QBean;
import com.querydsl.r2dbc.R2DBCQueryFactory;
import com.ruoyi.system.entity.SysConfig;
import com.ruoyi.system.query.SysConfigQuery;
import com.ruoyi.system.repository.querydsl.SysConfigQuerydslRepository;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.LocalTime;

import static com.querydsl.core.types.Projections.bean;
import static com.ruoyi.system.entity.querydsl.QSysConfig.sysConfig;

@Repository
public class SysConfigQuerydslRepositoryImpl implements SysConfigQuerydslRepository {

    private final QBean<SysConfig> bean = bean(SysConfig.class, sysConfig.all());

    @Resource
    private R2DBCQueryFactory queryFactory;

    @Override
    public Flux<SysConfig> selectConfigList(SysConfigQuery query) {
        Predicate predicate = buildPredicate(query);

        return queryFactory.select(bean)
                .from(sysConfig)
                .where(predicate)
                .orderBy(sysConfig.createTime.desc())
                .limit(query.limit())
                .offset(query.offset())
                .fetch();
    }

    private Predicate buildPredicate(SysConfigQuery query) {
        BooleanBuilder predicate = new BooleanBuilder();

        if (StringUtils.hasText(query.getConfigType())) {
            predicate.and(sysConfig.configType.eq(query.getConfigType()));
        }
        if (StringUtils.hasText(query.getConfigName())) {
            predicate.and(sysConfig.configName.contains(query.getConfigName()));
        }
        if (StringUtils.hasText(query.getConfigKey())) {
            predicate.and(sysConfig.configKey.contains(query.getConfigKey()));
        }
        if (ObjectUtils.allNotNull(query.getStartTime())) {
            predicate.and(sysConfig.createTime.goe(LocalDateTime.of(query.getStartTime(), LocalTime.MIN)));
        }
        if (ObjectUtils.allNotNull(query.getEndTime())) {
            predicate.and(sysConfig.createTime.loe(LocalDateTime.of(query.getEndTime(), LocalTime.MAX)));
        }

        return predicate;
    }

    @Override
    public Mono<Long> selectConfigCount(SysConfigQuery query) {
        return queryFactory.from(sysConfig).fetchCount();
    }

}
