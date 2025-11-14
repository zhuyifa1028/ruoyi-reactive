package com.ruoyi.system.repository;

import com.ruoyi.system.domain.SysRoleDept;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 角色部门表 数据层
 *
 * @author bugout
 * @version 2025-11-14
 */
public interface SysRoleDeptRepository extends R2dbcRepository<SysRoleDept, Long> {

    Flux<SysRoleDept> findAllByRoleId(Long roleId);

    Mono<Void> deleteByRoleId(Long roleId);

    Mono<Void> deleteByRoleIdIn(List<Long> roleIds);

}
