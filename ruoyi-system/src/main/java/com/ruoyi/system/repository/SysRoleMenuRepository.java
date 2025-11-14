package com.ruoyi.system.repository;

import com.ruoyi.system.domain.SysRoleMenu;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 角色菜单表 数据层
 *
 * @author bugout
 * @version 2025-11-14
 */
public interface SysRoleMenuRepository extends R2dbcRepository<SysRoleMenu, Long> {

    Flux<SysRoleMenu> findAllByRoleId(Long roleId);

    Mono<Void> deleteByRoleId(Long roleId);

    Mono<Void> deleteByRoleIdIn(List<Long> roleIds);

}
