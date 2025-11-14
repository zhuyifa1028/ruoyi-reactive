package com.ruoyi.system.repository;

import com.ruoyi.system.entity.SysDict;
import com.ruoyi.system.repository.querydsl.SysDictQuerydslRepository;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 字典表 数据层
 *
 * @author bugout
 * @version 2025-11-13
 */
public interface SysDictRepository extends R2dbcRepository<SysDict, Long>, SysDictQuerydslRepository {

    @Query("SELECT * FROM sys_dict_data WHERE status = '0' ORDER BY dict_sort")
    Flux<SysDict> findAllNormal();

    @Query("SELECT * FROM sys_dict_data WHERE status = '0' AND dict_type = :dictType ORDER BY dict_sort")
    Flux<SysDict> findAllByDictType(String dictType);

    Mono<SysDict> findByDictTypeAndDictValue(String dictType, String dictValue);

}
