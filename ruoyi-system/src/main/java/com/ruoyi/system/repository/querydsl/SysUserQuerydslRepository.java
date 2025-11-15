package com.ruoyi.system.repository.querydsl;

import com.ruoyi.system.entity.SysUser;
import com.ruoyi.system.query.SysUserQuery;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SysUserQuerydslRepository {

    Flux<SysUser> selectUserList(SysUserQuery query);

    Mono<Long> selectUserCount(SysUserQuery query);

}
