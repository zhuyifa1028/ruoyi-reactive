package com.ruoyi.system.repository.querydsl;

import com.ruoyi.system.entity.SysDept;
import com.ruoyi.system.query.SysDeptQuery;
import reactor.core.publisher.Flux;

public interface SysDeptQuerydslRepository {

    Flux<SysDept> selectDeptList(SysDeptQuery query);

}
