package com.ruoyi.system.repository;

import com.ruoyi.system.entity.SysNotice;
import com.ruoyi.system.repository.querydsl.SysNoticeQuerydslRepository;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

/**
 * 通告表 数据层
 *
 * @author bugout
 * @version 2025-11-14
 */
public interface SysNoticeRepository extends R2dbcRepository<SysNotice, Long>, SysNoticeQuerydslRepository {

}
