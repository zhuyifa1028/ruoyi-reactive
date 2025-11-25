package com.ruoyi.system.repository;

import com.ruoyi.system.entity.SysUser;
import com.ruoyi.system.repository.querydsl.SysUserQuerydslRepository;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 用户表 数据层
 *
 * @author bugout
 * @version 2025-11-14
 */
public interface SysUserRepository extends R2dbcRepository<SysUser, Long>, SysUserQuerydslRepository {

    Mono<SysUser> findByUserName(String roleName);

    Mono<SysUser> findByPhonenumber(String phonenumber);

    Mono<SysUser> findByEmail(String email);

    @Query("UPDATE sys_user SET del_flag = '2' WHERE user_id IN (:userIds)")
    Mono<Void> deleteByUserIdIn(List<Long> userIds);

}
