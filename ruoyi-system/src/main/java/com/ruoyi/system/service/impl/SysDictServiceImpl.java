package com.ruoyi.system.service.impl;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.system.converter.SysDictConverter;
import com.ruoyi.system.dto.SysDictDTO;
import com.ruoyi.system.entity.SysDict;
import com.ruoyi.system.query.SysDictQuery;
import com.ruoyi.system.repository.SysDictRepository;
import com.ruoyi.system.service.SysDictService;
import com.ruoyi.system.utils.DictUtils;
import com.ruoyi.system.vo.SysDictVO;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.support.ReactivePageableExecutionUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 字典表 业务实现
 *
 * @author bugout
 * @version 2025-11-13
 */
@Service
public class SysDictServiceImpl implements SysDictService, ApplicationRunner {

    @Resource
    private SysDictConverter sysDictConverter;
    @Resource
    private SysDictRepository sysDictRepository;

    /**
     * 项目启动时，初始化字典到缓存
     */
    @Override
    public void run(ApplicationArguments args) {
        this.resetDictCache().subscribe();
    }

    /**
     * 根据条件分页查询字典列表
     */
    @Override
    public Mono<Page<SysDictVO>> selectDictList(SysDictQuery query) {
        return sysDictRepository.selectDictList(query)
                .map(sysDictConverter::toSysDictVO)
                .collectList()
                .flatMap(list -> {
                    Mono<Long> count = sysDictRepository.selectDictCount(query);
                    return ReactivePageableExecutionUtils.getPage(list, query.pageable(), count);
                })
                .defaultIfEmpty(Page.empty(query.pageable()));
    }

    /**
     * 根据字典类型查询字典列表
     */
    @Override
    public Flux<SysDictVO> selectDictListByType(String dictType) {
        return com.ruoyi.system.utils.DictUtils.getDictCache(dictType)
                .switchIfEmpty(
                        // 根据字典类型查询
                        sysDictRepository.findAllByDictType(dictType)
                                .collectList()
                                .flatMapMany(list -> {
                                    // 更新字典缓存
                                    com.ruoyi.system.utils.DictUtils.setDictCache(dictType, list);
                                    return Flux.fromIterable(list);
                                })
                )
                .map(sysDictConverter::toSysDictVO);
    }

    /**
     * 根据字典ID查询信息
     */
    @Override
    public Mono<SysDictVO> selectDictById(Long dictCode) {
        return sysDictRepository.findById(dictCode)
                .switchIfEmpty(Mono.error(new ServiceException("字典不存在")))
                .map(sysDictConverter::toSysDictVO);
    }

    /**
     * 新增字典
     */
    @Override
    public Mono<Void> insertDict(SysDictDTO dto) {
        return this.checkDictValueUnique(dto)
                .then(Mono.defer(() -> {
                    // 保存字典
                    SysDict dict = sysDictConverter.toSysDict(dto);
                    return sysDictRepository.save(dict);
                }))
                .thenMany(sysDictRepository.findAllByDictType(dto.getDictType()))
                .collectList()
                .doOnNext(dictList -> {
                    // 更新字典缓存
                    com.ruoyi.system.utils.DictUtils.setDictCache(dto.getDictType(), dictList);
                })
                .then();
    }

    /**
     * 校验字典值是否唯一
     */
    private Mono<Void> checkDictValueUnique(SysDictDTO dto) {
        return sysDictRepository.findByDictTypeAndDictValue(dto.getDictType(), dto.getDictValue())
                .flatMap(dict -> {
                    if (ObjectUtils.notEqual(dict.getDictCode(), dto.getDictCode())) {
                        return Mono.error(new ServiceException(String.format("字典值【%1$s】已存在 ", dto.getDictValue())));
                    }
                    return Mono.empty();
                })
                .then();
    }

    /**
     * 修改字典
     */
    @Override
    public Mono<Void> updateDict(SysDictDTO dto) {
        return this.checkDictValueUnique(dto)
                .then(sysDictRepository.findById(dto.getDictCode()))
                .switchIfEmpty(Mono.error(new ServiceException("字典不存在")))
                .flatMap(dict -> {
                    // 更新字典
                    sysDictConverter.copyProperties(dto, dict);
                    return sysDictRepository.save(dict);
                })
                .thenMany(sysDictRepository.findAllByDictType(dto.getDictType()))
                .collectList()
                .doOnNext(dictList -> {
                    // 更新字典缓存
                    com.ruoyi.system.utils.DictUtils.setDictCache(dto.getDictType(), dictList);
                })
                .then();
    }

    /**
     * 批量删除字典
     */
    @Override
    public Mono<Void> deleteDictByIds(List<Long> dictCodes) {
        return sysDictRepository.findAllById(dictCodes)
                .flatMap(dict -> {
                    // 删除字典
                    return sysDictRepository.delete(dict)
                            // 返回字典类型
                            .thenReturn(dict.getDictType());
                })
                // 字典类型去重
                .distinct()
                .flatMap(dictType -> {
                    // 根据字典类型查询
                    return sysDictRepository.findAllByDictType(dictType)
                            .collectList()
                            .doOnNext(dictList -> {
                                // 更新字典缓存
                                com.ruoyi.system.utils.DictUtils.setDictCache(dictType, dictList);
                            });
                })
                .then();
    }

    /**
     * 刷新字典缓存
     */
    @Override
    public Mono<Void> resetDictCache() {
        return DictUtils.clearDictCache()
                // 查询所有正常字典
                .thenMany(sysDictRepository.findAllNormal())
                // 分组
                .groupBy(SysDict::getDictType)
                // 写入缓存
                .flatMap(group ->
                        group.collectList().doOnNext(dictList -> DictUtils.setDictCache(group.key(), dictList))
                )
                .then();
    }

}
