package com.ruoyi.system.service;

import com.ruoyi.system.dto.SysPostDTO;
import com.ruoyi.system.query.SysPostQuery;
import com.ruoyi.system.vo.SysPostVO;
import org.springframework.data.domain.Page;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 岗位表 业务层
 *
 * @author bugout
 * @version 2025-11-14
 */
public interface SysPostService {

    /**
     * 查询岗位列表
     */
    Mono<Page<SysPostVO>> selectPostList(SysPostQuery query);

    /**
     * 通过岗位ID查询岗位信息
     */
    Mono<SysPostVO> selectPostById(Long postId);

    /**
     * 新增岗位
     */
    Mono<Void> insertPost(SysPostDTO dto);

    /**
     * 修改岗位
     */
    Mono<Void> updatePost(SysPostDTO dto);

    /**
     * 批量删除岗位
     */
    Mono<Void> deletePostByIds(List<Long> postIds);

    /**
     * 查询所有岗位
     */
    Flux<SysPostVO> selectPostAll();

    /**
     * 根据用户ID获取岗位选择框列表
     */
    List<Long> selectPostListByUserId(Long userId);

}
