package com.ruoyi.system.service.impl;

import com.ruoyi.system.converter.SysAccessLogConverter;
import com.ruoyi.system.query.SysAccessLogQuery;
import com.ruoyi.system.repository.SysAccessLogRepository;
import com.ruoyi.system.service.SysAccessLogService;
import com.ruoyi.system.vo.SysAccessLogVO;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.support.ReactivePageableExecutionUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 访问日志表 业务实现
 *
 * @author bugout
 * @version 2025-11-28
 */
@Service
public class SysAccessLogServiceImpl implements SysAccessLogService {

    @Resource
    private SysAccessLogConverter sysAccessLogConverter;
    @Resource
    private SysAccessLogRepository sysAccessLogRepository;

    /**
     * 查询系统访问日志集合
     *
     * @param query 访问日志对象
     * @return 登录记录集合
     */
    @Override
    public Mono<Page<SysAccessLogVO>> selectAccessLogList(SysAccessLogQuery query) {
        return sysAccessLogRepository.selectListByQuery(query)
                .map(sysAccessLogConverter::toSysAccessLog)
                .collectList()
                .flatMap(list -> {
                    Mono<Long> count = sysAccessLogRepository.selectCountByQuery(query);
                    return ReactivePageableExecutionUtils.getPage(list, query.pageable(), count);
                });
    }

    /**
     * 批量删除访问日志
     */
    @Override
    public Mono<Void> deleteAccessLogByIds(List<Long> logIds) {
        return sysAccessLogRepository.deleteAllById(logIds);
    }

    /**
     * 清空系统访问日志
     */
    @Override
    public Mono<Void> clearAccessLog() {
        return sysAccessLogRepository.deleteAll();
    }

}
