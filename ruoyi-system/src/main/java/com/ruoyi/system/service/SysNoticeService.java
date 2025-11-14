package com.ruoyi.system.service;

import com.ruoyi.system.dto.SysNoticeDTO;
import com.ruoyi.system.query.SysNoticeQuery;
import com.ruoyi.system.vo.SysNoticeVO;
import org.springframework.data.domain.Page;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 通告表 业务层
 *
 * @author bugout
 * @version 2025-11-14
 */
public interface SysNoticeService {

    /**
     * 查询通告列表
     */
    Mono<Page<SysNoticeVO>> selectNoticeList(SysNoticeQuery query);

    /**
     * 根据通告ID查询信息
     */
    Mono<SysNoticeVO> selectNoticeById(Long noticeId);

    /**
     * 新增通告
     */
    Mono<Void> insertNotice(SysNoticeDTO dto);

    /**
     * 修改通告
     */
    Mono<Void> updateNotice(SysNoticeDTO dto);

    /**
     * 批量删除通告
     */
    Mono<Void> deleteNoticeByIds(List<Long> noticeIds);

}
