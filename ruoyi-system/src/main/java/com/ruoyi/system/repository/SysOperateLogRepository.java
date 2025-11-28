package com.ruoyi.system.repository;

import com.ruoyi.system.entity.SysOperateLog;
import com.ruoyi.system.repository.querydsl.SysOperateLogQuerydslRepository;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

/**
 * 操作日志表 数据层
 *
 * @author bugout
 * @version 2025-11-28
 */
public interface SysOperateLogRepository extends R2dbcRepository<SysOperateLog, Long>, SysOperateLogQuerydslRepository {

}
