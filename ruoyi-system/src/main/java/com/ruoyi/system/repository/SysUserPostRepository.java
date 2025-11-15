package com.ruoyi.system.repository;

import com.ruoyi.system.domain.SysUserPost;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

/**
 * 用户角色表 数据层
 *
 * @author bugout
 * @version 2025-11-14
 */
public interface SysUserPostRepository extends R2dbcRepository<SysUserPost, Long> {

    Mono<Void> deleteByUserId(Long userId);

}
