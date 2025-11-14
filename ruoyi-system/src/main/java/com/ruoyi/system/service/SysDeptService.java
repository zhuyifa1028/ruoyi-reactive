package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.system.dto.SysDeptDTO;
import com.ruoyi.system.query.SysDeptQuery;
import com.ruoyi.system.vo.SysDeptVO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 部门表 业务层
 *
 * @author bugout
 * @version 2025-11-12
 */
public interface SysDeptService {

    /**
     * 查询部门列表
     */
    Flux<SysDeptVO> selectDeptList(SysDeptQuery query);

    /**
     * 查询部门列表（排除指定节点）
     */
    Flux<SysDeptVO> selectDeptListExclude(Long deptId);

    /**
     * 根据部门ID查询信息
     */
    Mono<SysDeptVO> selectDeptById(Long deptId);

    /**
     * 新增部门
     */
    Mono<Void> insertDept(SysDeptDTO dto);

    /**
     * 修改部门
     */
    Mono<Void> updateDept(SysDeptDTO dto);

    /**
     * 删除部门
     */
    Mono<Void> deleteDeptById(Long deptId);

    List<SysDept> selectDeptList(SysDept dept);

    /**
     * 根据角色ID查询选中部门列表
     */
    Flux<Long> selectDeptListByRoleId(Long roleId);

    /**
     * 校验部门是否有数据权限
     *
     * @param deptId 部门id
     */
    void checkDeptDataScope(Long deptId);

}
