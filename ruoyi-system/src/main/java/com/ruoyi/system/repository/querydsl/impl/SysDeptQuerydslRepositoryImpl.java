package com.ruoyi.system.repository.querydsl.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.QBean;
import com.querydsl.r2dbc.R2DBCQueryFactory;
import com.ruoyi.system.entity.SysDept;
import com.ruoyi.system.query.SysDeptQuery;
import com.ruoyi.system.repository.querydsl.SysDeptQuerydslRepository;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import static com.querydsl.core.types.Projections.bean;
import static com.ruoyi.system.entity.querydsl.QSysDept.sysDept;

@Repository
public class SysDeptQuerydslRepositoryImpl implements SysDeptQuerydslRepository {

    private final QBean<SysDept> bean = bean(SysDept.class, sysDept.all());

    @Resource
    private R2DBCQueryFactory queryFactory;

    @Override
    public Flux<SysDept> selectDeptList(SysDeptQuery query) {
        BooleanBuilder predicate = new BooleanBuilder();
        predicate.and(sysDept.delFlag.eq("0"));

        if (StringUtils.isNotBlank(query.getDeptName())) {
            predicate.and(sysDept.deptName.contains(query.getDeptName()));
        }
        if (StringUtils.isNotBlank(query.getStatus())) {
            predicate.and(sysDept.status.eq(query.getStatus()));
        }

        return queryFactory.select(bean)
                .from(sysDept)
                .where(predicate)
                .orderBy(sysDept.parentId.asc(), sysDept.orderNum.asc())
                .fetch();
    }

}
