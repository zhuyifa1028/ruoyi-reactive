package com.ruoyi.system.service.impl;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.system.converter.SysPostConverter;
import com.ruoyi.system.dto.SysPostDTO;
import com.ruoyi.system.entity.SysPost;
import com.ruoyi.system.mapper.SysPostMapper;
import com.ruoyi.system.query.SysPostQuery;
import com.ruoyi.system.repository.SysPostRepository;
import com.ruoyi.system.service.SysPostService;
import com.ruoyi.system.vo.SysPostVO;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.support.ReactivePageableExecutionUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 岗位表 业务处理
 *
 * @author bugout
 * @version 2025-11-14
 */
@Service
public class SysPostServiceImpl implements SysPostService {

    @Resource
    private SysPostConverter sysPostConverter;
    @Resource
    private SysPostRepository sysPostRepository;

    @Resource
    private SysPostMapper sysPostMapper;

    /**
     * 查询岗位列表
     */
    @Override
    public Mono<Page<SysPostVO>> selectPostList(SysPostQuery query) {
        return sysPostRepository.selectPostList(query)
                .map(sysPostConverter::toSysPostVO)
                .collectList()
                .flatMap(voList -> {
                    Mono<Long> count = sysPostRepository.selectPostCount(query);
                    return ReactivePageableExecutionUtils.getPage(voList, query.pageable(), count);
                })
                .defaultIfEmpty(Page.empty(query.pageable()));
    }

    /**
     * 通过岗位ID查询岗位信息
     */
    @Override
    public Mono<SysPostVO> selectPostById(Long postId) {
        return sysPostRepository.findById(postId)
                .switchIfEmpty(Mono.error(new ServiceException("岗位不存在")))
                .map(sysPostConverter::toSysPostVO);
    }

    /**
     * 新增岗位
     */
    @Override
    public Mono<Void> insertPost(SysPostDTO dto) {
        return this.checkPostCodeUnique(dto)
                .then(this.checkPostNameUnique(dto))
                .then(Mono.defer(() -> {
                    SysPost post = sysPostConverter.toSysPost(dto);
                    return sysPostRepository.save(post);
                }))
                .then();
    }

    /**
     * 校验岗位编码是否唯一
     */
    private Mono<Void> checkPostCodeUnique(SysPostDTO dto) {
        return sysPostRepository.findByPostCode(dto.getPostCode())
                .flatMap(post -> {
                    if (ObjectUtils.notEqual(post.getPostId(), dto.getPostId())) {
                        return Mono.error(new ServiceException("岗位编码已存在"));
                    }
                    return Mono.empty();
                })
                .then();
    }

    /**
     * 校验岗位名称是否唯一
     */
    private Mono<Void> checkPostNameUnique(SysPostDTO dto) {
        return sysPostRepository.findByPostName(dto.getPostName())
                .flatMap(post -> {
                    if (ObjectUtils.notEqual(post.getPostId(), dto.getPostId())) {
                        return Mono.error(new ServiceException("岗位名称已存在"));
                    }
                    return Mono.empty();
                })
                .then();
    }

    /**
     * 修改岗位
     */
    @Override
    public Mono<Void> updatePost(SysPostDTO dto) {
        return sysPostRepository.findById(dto.getPostId())
                .switchIfEmpty(Mono.error(new ServiceException("岗位不存在")))
                .flatMap(post -> {
                    // 校验岗位编码是否唯一
                    return this.checkPostCodeUnique(dto)
                            // 校验岗位名称是否唯一
                            .then(this.checkPostNameUnique(dto))
                            // 更新岗位信息
                            .then(Mono.defer(() -> {
                                sysPostConverter.copyProperties(dto, post);
                                return sysPostRepository.save(post);
                            }))
                            .then();
                })
                .then();
    }

    /**
     * 批量删除岗位
     */
    @Override
    public Mono<Void> deletePostByIds(List<Long> postIds) {
        return sysPostRepository.findAllById(postIds)
                .flatMap(this::checkPostAllocated)
                .then(sysPostRepository.deleteAllById(postIds))
                .then();
    }

    /**
     * 校验岗位岗位是否已分配
     */
    private Mono<Void> checkPostAllocated(SysPost post) {
        return sysPostRepository.countUserByPostId(post.getPostId())
                .flatMap(countUser -> {
                    if (countUser > 0) {
                        return Mono.error(new ServiceException(String.format("岗位[%s]已分配，不能删除", post.getPostName())));
                    }
                    return Mono.empty();
                })
                .then();
    }

    /**
     * 查询所有岗位
     */
    @Override
    public Flux<SysPostVO> selectPostAll() {
        return sysPostRepository.findAll()
                .map(sysPostConverter::toSysPostVO);
    }

    /**
     * 根据用户ID获取岗位选择框列表
     */
    @Override
    public List<Long> selectPostListByUserId(Long userId) {
        return sysPostMapper.selectPostListByUserId(userId);
    }

}
