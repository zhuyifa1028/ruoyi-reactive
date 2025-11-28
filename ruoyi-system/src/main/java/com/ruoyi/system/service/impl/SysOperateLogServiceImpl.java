package com.ruoyi.system.service.impl;

import com.ruoyi.system.converter.SysOperateLogConverter;
import com.ruoyi.system.query.SysOperateLogQuery;
import com.ruoyi.system.repository.SysOperateLogRepository;
import com.ruoyi.system.service.SysOperateLogService;
import com.ruoyi.system.vo.SysOperateLogVO;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.support.ReactivePageableExecutionUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 操作日志表 业务实现
 *
 * @author bugout
 * @version 2025-11-28
 */
@Service
public class SysOperateLogServiceImpl implements SysOperateLogService {

    @Resource
    private SysOperateLogConverter sysOperateLogConverter;
    @Resource
    private SysOperateLogRepository sysOperateLogRepository;

    /**
     * 根据条件分页查询操作日志
     */
    @Override
    public Mono<Page<SysOperateLogVO>> selectOperateLogList(SysOperateLogQuery query) {
        return sysOperateLogRepository.selectListByQuery(query)
                .map(sysOperateLogConverter::toSysOperateLog)
                .collectList()
                .flatMap(list -> {
                    Mono<Long> count = sysOperateLogRepository.selectCountByQuery(query);
                    return ReactivePageableExecutionUtils.getPage(list, query.pageable(), count);
                });
    }

    /**
     * 批量删除操作日志
     */
    @Override
    public Mono<Void> deleteOperateLogByIds(List<Long> operIds) {
        return sysOperateLogRepository.deleteAllById(operIds);
    }

    /**
     * 清空操作日志
     */
    @Override
    public Mono<Void> clearOperateLog() {
        return sysOperateLogRepository.deleteAll();
    }

}
