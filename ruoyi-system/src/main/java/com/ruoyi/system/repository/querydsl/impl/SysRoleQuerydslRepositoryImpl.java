package com.ruoyi.system.repository.querydsl.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.r2dbc.R2DBCQueryFactory;
import com.ruoyi.system.entity.SysRole;
import com.ruoyi.system.query.SysRoleQuery;
import com.ruoyi.system.repository.querydsl.SysRoleQuerydslRepository;
import com.ruoyi.system.utils.QuerydslExecutionUtils;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.ruoyi.system.entity.querydsl.QSysRole.sysRole;

@Repository
public class SysRoleQuerydslRepositoryImpl implements SysRoleQuerydslRepository {

    @Resource
    private R2DBCQueryFactory queryFactory;

    @Override
    public Flux<SysRole> selectRoleList(SysRoleQuery query) {
        Predicate predicate = buildPredicate(query);

        return queryFactory.selectFrom(sysRole)
                .where(predicate)
                .orderBy(sysRole.roleSort.asc())
                .limit(query.limit())
                .offset(query.offset())
                .fetch();
    }

    private Predicate buildPredicate(SysRoleQuery query) {
        BooleanBuilder predicate = new BooleanBuilder();
        predicate.and(sysRole.delFlag.eq("0"));

        if (StringUtils.isNotBlank(query.getRoleName())) {
            predicate.and(sysRole.roleName.contains(query.getRoleName()));
        }
        if (StringUtils.isNotBlank(query.getRoleKey())) {
            predicate.and(sysRole.roleKey.contains(query.getRoleKey()));
        }
        if (StringUtils.isNotBlank(query.getStatus())) {
            predicate.and(sysRole.status.eq(query.getStatus()));
        }

        // 日期范围
        QuerydslExecutionUtils.dateRange(predicate, sysRole.createTime, query.getStartTime(), query.getEndTime());

        return predicate;
    }

    @Override
    public Mono<Long> selectRoleCount(SysRoleQuery query) {
        Predicate predicate = buildPredicate(query);

        return queryFactory.selectFrom(sysRole)
                .where(predicate)
                .fetchCount();
    }
}
