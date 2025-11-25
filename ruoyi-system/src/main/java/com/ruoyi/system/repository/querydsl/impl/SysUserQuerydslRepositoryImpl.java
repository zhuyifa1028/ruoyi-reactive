package com.ruoyi.system.repository.querydsl.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.r2dbc.R2DBCQueryFactory;
import com.ruoyi.system.entity.SysUser;
import com.ruoyi.system.query.SysUserQuery;
import com.ruoyi.system.repository.querydsl.SysUserQuerydslRepository;
import com.ruoyi.system.utils.QuerydslExecutionUtils;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.ruoyi.system.entity.querydsl.QSysUser.sysUser;

@Repository
public class SysUserQuerydslRepositoryImpl implements SysUserQuerydslRepository {

    @Resource
    private R2DBCQueryFactory queryFactory;

    @Override
    public Flux<SysUser> selectUserList(SysUserQuery query) {
        Predicate predicate = buildPredicate(query);

        return queryFactory.selectFrom(sysUser)
                .where(predicate)
                .orderBy(sysUser.createTime.desc())
                .limit(query.limit())
                .offset(query.offset())
                .fetch();
    }

    private Predicate buildPredicate(SysUserQuery query) {
        BooleanBuilder predicate = new BooleanBuilder();
        predicate.and(sysUser.delFlag.eq("0"));

        if (StringUtils.isNotBlank(query.getUserName())) {
            predicate.and(sysUser.userName.contains(query.getUserName()));
        }
        if (ObjectUtils.allNotNull(query.getDeptId())) {
            predicate.and(sysUser.deptId.eq(query.getDeptId()));
        }
        if (StringUtils.isNotBlank(query.getUserName())) {
            predicate.and(sysUser.userName.contains(query.getUserName()));
        }
        if (StringUtils.isNotBlank(query.getPhonenumber())) {
            predicate.and(sysUser.phonenumber.contains(query.getPhonenumber()));
        }
        if (StringUtils.isNotBlank(query.getStatus())) {
            predicate.and(sysUser.status.eq(query.getStatus()));
        }

        // 日期范围
        QuerydslExecutionUtils.dateRange(predicate, sysUser.createTime, query.getStartTime(), query.getEndTime());

        return predicate;
    }

    @Override
    public Mono<Long> selectUserCount(SysUserQuery query) {
        Predicate predicate = buildPredicate(query);

        return queryFactory.selectFrom(sysUser)
                .where(predicate)
                .fetchCount();
    }
}
