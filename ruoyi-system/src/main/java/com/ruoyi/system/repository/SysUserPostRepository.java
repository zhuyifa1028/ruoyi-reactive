package com.ruoyi.system.repository;

import com.ruoyi.system.domain.SysUserPost;
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
public interface SysUserPostRepository extends R2dbcRepository<SysUserPost, Long> {

    Mono<Void> deleteByUserId(Long userId);

    Mono<Void> deleteByUserIdIn(List<Long> userIds);

    @Query("SELECT post_id FROM sys_user_post WHERE user_id = :userId")
    Flux<Long> findPostIdsByUserId(Long userId);

}
