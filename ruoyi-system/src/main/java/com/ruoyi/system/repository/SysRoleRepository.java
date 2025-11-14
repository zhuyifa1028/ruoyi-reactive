package com.ruoyi.system.repository;

import com.ruoyi.system.entity.SysRole;
import com.ruoyi.system.repository.querydsl.SysRoleQuerydslRepository;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

/**
 * 角色表 数据层
 *
 * @author bugout
 * @version 2025-11-14
 */
public interface SysRoleRepository extends R2dbcRepository<SysRole, Long>, SysRoleQuerydslRepository {

    Mono<SysRole> findByRoleKey(String roleKey);

    Mono<SysRole> findByRoleName(String roleName);

}
