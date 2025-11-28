package com.ruoyi.system.repository;

import com.ruoyi.system.entity.SysAccessLog;
import com.ruoyi.system.repository.querydsl.SysAccessLogQuerydslRepository;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

/**
 * 访问日志表 数据层
 *
 * @author bugout
 * @version 2025-11-28
 */
public interface SysAccessLogRepository extends R2dbcRepository<SysAccessLog, Long>, SysAccessLogQuerydslRepository {

}
