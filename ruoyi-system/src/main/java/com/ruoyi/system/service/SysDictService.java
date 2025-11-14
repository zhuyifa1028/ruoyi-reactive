package com.ruoyi.system.service;

import com.ruoyi.system.dto.SysDictDTO;
import com.ruoyi.system.query.SysDictQuery;
import com.ruoyi.system.vo.SysDictVO;
import org.springframework.data.domain.Page;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 字典表 业务层
 *
 * @author bugout
 * @version 2025-11-13
 */
public interface SysDictService {

    /**
     * 根据条件分页查询字典列表
     */
    Mono<Page<SysDictVO>> selectDictList(SysDictQuery query);

    /**
     * 根据字典类型查询字典列表
     */
    Flux<SysDictVO> selectDictListByType(String dictType);

    /**
     * 根据字典ID查询信息
     */
    Mono<SysDictVO> selectDictById(Long dictCode);

    /**
     * 新增字典
     */
    Mono<Void> insertDict(SysDictDTO dto);

    /**
     * 修改字典
     */
    Mono<Void> updateDict(SysDictDTO dto);

    /**
     * 批量删除字典
     */
    Mono<Void> deleteDictByIds(List<Long> dictCodes);

    /**
     * 刷新字典缓存
     */
    Mono<Void> resetDictCache();

}
