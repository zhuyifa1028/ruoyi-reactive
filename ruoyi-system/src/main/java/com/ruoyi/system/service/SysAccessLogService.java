package com.ruoyi.system.service;

import com.ruoyi.system.query.SysAccessLogQuery;
import com.ruoyi.system.vo.SysAccessLogVO;
import org.springframework.data.domain.Page;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 访问日志表 业务层
 *
 * @author bugout
 * @version 2025-11-28
 */
public interface SysAccessLogService {

    /**
     * 根据条件分页查询访问日志
     */
    Mono<Page<SysAccessLogVO>> selectAccessLogList(SysAccessLogQuery query);

    /**
     * 批量删除访问日志
     */
    Mono<Void> deleteAccessLogByIds(List<Long> logIds);

    /**
     * 清空访问日志
     */
    Mono<Void> clearAccessLog();

}
