package com.ruoyi.system.repository;

import com.ruoyi.system.domain.SysUserRole;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 用户角色表 数据层
 *
 * @author bugout
 * @version 2025-11-14
 */
public interface SysUserRoleRepository extends R2dbcRepository<SysUserRole, Long> {

    Mono<Long> countByRoleId(Long roleId);

    Mono<Void> deleteByRoleIdAndUserIdIn(Long roleId, Long[] userIds);

    Mono<Void> deleteByUserId(Long userId);

    Mono<Void> deleteByUserIdIn(List<Long> userIds);

    @Query("SELECT role_id FROM sys_user_role WHERE user_id = :userId")
    Flux<Long> findRoleIdsByUserId(Long userId);

}
