package com.ruoyi.system.repository;

import com.ruoyi.system.entity.SysPost;
import com.ruoyi.system.repository.querydsl.SysPostQuerydslRepository;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

/**
 * 岗位表 数据层
 *
 * @author bugout
 * @version 2025-11-14
 */
public interface SysPostRepository extends R2dbcRepository<SysPost, Long>, SysPostQuerydslRepository {

    Mono<SysPost> findByPostCode(String postCode);

    Mono<SysPost> findByPostName(String postName);

    @Query("SELECT count(1) FROM sys_user_post WHERE post_id = :postId")
    Mono<Long> countUserByPostId(Long postId);

}
