package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.system.dto.SysRoleDTO;
import com.ruoyi.system.query.SysRoleQuery;
import com.ruoyi.system.vo.SysRoleVO;
import org.springframework.data.domain.Page;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;

/**
 * 角色表 业务层
 *
 * @author bugout
 * @version 2025-11-14
 */
public interface SysRoleService {

    /**
     * 根据条件分页查询角色列表
     */
    Mono<Page<SysRoleVO>> selectRoleList(SysRoleQuery query);

    /**
     * 通过角色ID查询角色信息
     */
    Mono<SysRoleVO> selectRoleById(Long roleId);

    /**
     * 新增角色
     */
    Mono<Void> insertRole(SysRoleDTO dto);

    /**
     * 修改角色
     */
    Mono<Void> updateRole(SysRoleDTO dto);

    /**
     * 修改角色状态
     */
    Mono<Void> updateRoleStatus(SysRoleDTO dto);

    /**
     * 修改角色数据权限
     */
    Mono<Void> updateRoleDataScope(SysRoleDTO dto);

    /**
     * 批量授权用户角色
     */
    Mono<Void> insertAuthUsers(Long roleId, Long[] userIds);

    /**
     * 批量取消授权用户角色
     */
    Mono<Void> deleteAuthUsers(Long roleId, Long[] userIds);

    /**
     * 批量删除角色
     */
    Mono<Void> deleteRoleByIds(List<Long> roleIds);

    /**
     * 根据用户ID查询角色列表
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    List<SysRole> selectRolesByUserId(Long userId);

    /**
     * 根据用户ID查询角色权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    Set<String> selectRolePermissionByUserId(Long userId);

    /**
     * 校验角色是否允许操作
     */
    void checkRoleAllowed(SysRole role);

    /**
     * 校验角色是否有数据权限
     */
    void checkRoleDataScope(Long... roleIds);

    List<SysRole> selectRoleList(SysRole role);

}
