package com.ruoyi.system.repository;

import com.ruoyi.system.entity.SysMenu;
import com.ruoyi.system.repository.querydsl.SysMenuQuerydslRepository;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

/**
 * 菜单表 数据层
 *
 * @author bugout
 * @version 2025-11-14
 */
public interface SysMenuRepository extends R2dbcRepository<SysMenu, Long>, SysMenuQuerydslRepository {

    @Query("SELECT count(*) FROM sys_menu WHERE parent_id = :menuId")
    Mono<Long> countChildByMenuId(Long menuId);

    @Query("SELECT count(*) FROM sys_role_menu WHERE menu_id = :menuId")
    Mono<Long> countRoleByMenuId(Long menuId);

    @Query("SELECT * FROM sys_menu WHERE menu_name = :menuName AND parent_id = :parentId LIMIT 1")
    Mono<SysMenu> selectMenuNameUnique(String menuName, Long parentId);

}
