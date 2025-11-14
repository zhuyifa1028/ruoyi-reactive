package com.ruoyi.system.repository.querydsl.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.r2dbc.R2DBCQueryFactory;
import com.ruoyi.system.entity.SysMenu;
import com.ruoyi.system.query.SysMenuQuery;
import com.ruoyi.system.repository.querydsl.SysMenuQuerydslRepository;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import static com.ruoyi.system.entity.querydsl.QSysMenu.sysMenu;
import static com.ruoyi.system.entity.querydsl.QSysRoleMenu.sysRoleMenu;
import static com.ruoyi.system.entity.querydsl.QSysUserRole.sysUserRole;

@Repository
public class SysMenuQuerydslRepositoryImpl implements SysMenuQuerydslRepository {

    @Resource
    private R2DBCQueryFactory queryFactory;

    @Override
    public Flux<SysMenu> selectMenuList(SysMenuQuery query) {
        BooleanBuilder predicate = buildPredicate(query);

        return queryFactory.selectFrom(sysMenu)
                .where(predicate)
                .orderBy(sysMenu.parentId.asc(), sysMenu.orderNum.asc())
                .fetch();
    }

    private BooleanBuilder buildPredicate(SysMenuQuery query) {
        BooleanBuilder predicate = new BooleanBuilder();

        if (StringUtils.isNotBlank(query.getMenuName())) {
            predicate.and(sysMenu.menuName.contains(query.getMenuName()));
        }
        if (StringUtils.isNotBlank(query.getStatus())) {
            predicate.and(sysMenu.status.eq(query.getStatus()));
        }

        return predicate;
    }

    @Override
    public Flux<SysMenu> selectMenuListByUserId(Long userId, SysMenuQuery query) {
        BooleanBuilder predicate = buildPredicate(query);
        // 用户ID
        predicate.and(sysUserRole.userId.eq(userId));

        return queryFactory.selectDistinct(sysMenu)
                .leftJoin(sysRoleMenu).on(sysRoleMenu.menuId.eq(sysMenu.menuId))
                .leftJoin(sysUserRole).on(sysUserRole.roleId.eq(sysRoleMenu.roleId))
                .where(predicate)
                .orderBy(sysMenu.parentId.asc(), sysMenu.orderNum.asc())
                .fetch();
    }

}
