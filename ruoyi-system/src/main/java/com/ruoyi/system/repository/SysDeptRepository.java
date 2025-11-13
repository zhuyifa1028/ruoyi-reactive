package com.ruoyi.system.repository;

import com.ruoyi.system.entity.SysDept;
import com.ruoyi.system.repository.querydsl.SysDeptQuerydslRepository;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;

/**
 * 部门表 数据层
 *
 * @author bugout
 * @version 2025-11-12
 */
public interface SysDeptRepository extends R2dbcRepository<SysDept, Long>, SysDeptQuerydslRepository {

    @Query("SELECT * FROM sys_dept WHERE FIND_IN_SET(:deptId, ancestors)")
    Flux<SysDept> selectChildrenDeptById(Long deptId);

    @Query("SELECT * FROM sys_dept WHERE del_flag = 0 AND dept_name= :deptName AND parent_id = :parentId LIMIT 1")
    Mono<SysDept> selectDeptNameUnique(String deptName, Long parentId);

    @Query("SELECT count(*) FROM sys_dept WHERE del_flag = 0 AND status = 0 AND FIND_IN_SET(:deptId, ancestors)")
    Mono<Long> countNormalChildrenByDeptId(Long deptId);

    @Query("SELECT count(*) FROM sys_dept WHERE del_flag = 0 AND parent_id = :deptId")
    Mono<Long> countChildByDeptId(Long deptId);

    @Query("SELECT count(*) FROM sys_user WHERE del_flag = 0 AND dept_id = :deptId")
    Mono<Long> countUserByDeptId(Long deptId);

    @Query("UPDATE sys_dept SET status = '0' WHERE dept_id IN (:deptIds)")
    Mono<Void> updateParentNormal(Set<String> deptIds);

    @Query("UPDATE sys_dept SET del_flag = 2 WHERE dept_id = :deptId")
    Mono<Void> deleteByDeptId(Long deptId);

}
