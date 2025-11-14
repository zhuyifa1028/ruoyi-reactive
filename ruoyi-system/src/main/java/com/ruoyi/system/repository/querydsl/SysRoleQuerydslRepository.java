package com.ruoyi.system.repository.querydsl;

import com.ruoyi.system.entity.SysRole;
import com.ruoyi.system.query.SysRoleQuery;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SysRoleQuerydslRepository {

    Flux<SysRole> selectRoleList(SysRoleQuery query);

    Mono<Long> selectRoleCount(SysRoleQuery query);

}
