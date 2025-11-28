package com.ruoyi.system.service;

import com.ruoyi.system.query.SysOperateLogQuery;
import com.ruoyi.system.vo.SysOperateLogVO;
import org.springframework.data.domain.Page;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 操作日志表 业务层
 *
 * @author bugout
 * @version 2025-11-28
 */
public interface SysOperateLogService {

    /**
     * 根据条件分页查询操作日志
     */
    Mono<Page<SysOperateLogVO>> selectOperateLogList(SysOperateLogQuery query);

    /**
     * 批量删除操作日志
     */
    Mono<Void> deleteOperateLogByIds(List<Long> operIds);

    /**
     * 清空操作日志
     */
    Mono<Void> clearOperateLog();

}
