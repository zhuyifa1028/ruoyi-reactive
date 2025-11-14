package com.ruoyi.system.service.impl;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.system.converter.SysNoticeConverter;
import com.ruoyi.system.dto.SysNoticeDTO;
import com.ruoyi.system.entity.SysNotice;
import com.ruoyi.system.query.SysNoticeQuery;
import com.ruoyi.system.repository.SysNoticeRepository;
import com.ruoyi.system.service.SysNoticeService;
import com.ruoyi.system.vo.SysNoticeVO;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.support.ReactivePageableExecutionUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 通告表 业务实现
 *
 * @author bugout
 * @version 2025-11-14
 */
@Service
public class SysNoticeServiceImpl implements SysNoticeService {

    @Resource
    private SysNoticeConverter sysNoticeConverter;
    @Resource
    private SysNoticeRepository sysNoticeRepository;

    /**
     * 查询通告列表
     */
    @Override
    public Mono<Page<SysNoticeVO>> selectNoticeList(SysNoticeQuery query) {
        return sysNoticeRepository.selectNoticeList(query)
                .map(sysNoticeConverter::toSysNoticeVO)
                .collectList()
                .flatMap(list -> {
                    Mono<Long> count = sysNoticeRepository.selectNoticeCount(query);
                    return ReactivePageableExecutionUtils.getPage(list, query.pageable(), count);
                })
                .defaultIfEmpty(Page.empty(query.pageable()));
    }

    /**
     * 根据通告ID查询信息
     */
    @Override
    public Mono<SysNoticeVO> selectNoticeById(Long noticeId) {
        return sysNoticeRepository.findById(noticeId)
                .switchIfEmpty(Mono.error(new ServiceException("通告不存在")))
                .map(sysNoticeConverter::toSysNoticeVO);
    }

    /**
     * 新增通告
     */
    @Override
    public Mono<Void> insertNotice(SysNoticeDTO dto) {
        SysNotice notice = sysNoticeConverter.toSysNotice(dto);
        return sysNoticeRepository.save(notice)
                .then();
    }

    /**
     * 修改通告
     */
    @Override
    public Mono<Void> updateNotice(SysNoticeDTO dto) {
        return sysNoticeRepository.findById(dto.getNoticeId())
                .switchIfEmpty(Mono.error(new ServiceException("通告不存在")))
                .flatMap(notice -> {
                    sysNoticeConverter.copyProperties(dto, notice);
                    return sysNoticeRepository.save(notice);
                })
                .then();
    }

    /**
     * 批量删除通告
     */
    @Override
    public Mono<Void> deleteNoticeByIds(List<Long> noticeIds) {
        return sysNoticeRepository.deleteAllById(noticeIds);
    }

}
