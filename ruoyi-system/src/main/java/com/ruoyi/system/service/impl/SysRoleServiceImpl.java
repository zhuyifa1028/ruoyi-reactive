package com.ruoyi.system.service.impl;

import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.framework.security.ReactiveSecurityUtils;
import com.ruoyi.system.converter.SysRoleConverter;
import com.ruoyi.system.domain.SysRoleDept;
import com.ruoyi.system.domain.SysRoleMenu;
import com.ruoyi.system.domain.SysUserRole;
import com.ruoyi.system.dto.SysRoleDTO;
import com.ruoyi.system.mapper.SysRoleMapper;
import com.ruoyi.system.query.SysRoleQuery;
import com.ruoyi.system.repository.SysRoleDeptRepository;
import com.ruoyi.system.repository.SysRoleMenuRepository;
import com.ruoyi.system.repository.SysRoleRepository;
import com.ruoyi.system.repository.SysUserRoleRepository;
import com.ruoyi.system.service.SysRoleService;
import com.ruoyi.system.vo.SysRoleVO;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.support.ReactivePageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 角色表 业务处理
 *
 * @author bugout
 * @version 2025-11-14
 */
@Service
public class SysRoleServiceImpl implements SysRoleService {

    @Resource
    private SysRoleConverter sysRoleConverter;
    @Resource
    private SysRoleRepository sysRoleRepository;

    @Resource
    private SysRoleDeptRepository sysRoleDeptRepository;
    @Resource
    private SysRoleMenuRepository sysRoleMenuRepository;

    @Resource
    private SysUserRoleRepository sysUserRoleRepository;

    @Resource
    private SysRoleMapper roleMapper;

    /**
     * 根据条件分页查询角色列表
     */
    @Override
    public Mono<Page<SysRoleVO>> selectRoleList(SysRoleQuery query) {
        return sysRoleRepository.selectRoleList(query)
                .map(sysRoleConverter::toSysRoleVO)
                .collectList()
                .flatMap(voList -> {
                    Mono<Long> count = sysRoleRepository.selectRoleCount(query);
                    return ReactivePageableExecutionUtils.getPage(voList, query.pageable(), count);
                })
                .defaultIfEmpty(Page.empty(query.pageable()));
    }

    /**
     * 通过角色ID查询角色信息
     */
    @Override
    public Mono<SysRoleVO> selectRoleById(Long roleId) {
        return sysRoleRepository.findById(roleId)
                .switchIfEmpty(Mono.error(new ServiceException("角色不存在")))
                .map(sysRoleConverter::toSysRoleVO);
    }

    /**
     * 新增角色
     */
    @Transactional
    @Override
    public Mono<Void> insertRole(SysRoleDTO dto) {
        return this.checkRoleNameUnique(dto)
                .then(this.checkRoleKeyUnique(dto))
                .then(Mono.defer(() -> {
                    // 保存角色信息
                    return sysRoleRepository.save(sysRoleConverter.toSysRole(dto))
                            .map(com.ruoyi.system.entity.SysRole::getRoleId)
                            // 新增角色菜单
                            .flatMap(roleId -> this.insertRoleMenu(roleId, dto.getMenuIds()));
                }))
                .then();
    }

    /**
     * 校验角色名称是否唯一
     */
    private Mono<Void> checkRoleNameUnique(SysRoleDTO dto) {
        return sysRoleRepository.findByRoleName(dto.getRoleName())
                .flatMap(post -> {
                    if (ObjectUtils.notEqual(post.getRoleId(), dto.getRoleId())) {
                        return Mono.error(new ServiceException("角色名称已存在"));
                    }
                    return Mono.empty();
                })
                .then();
    }

    /**
     * 校验角色权限是否唯一
     */
    private Mono<Void> checkRoleKeyUnique(SysRoleDTO dto) {
        return sysRoleRepository.findByRoleKey(dto.getRoleKey())
                .flatMap(post -> {
                    if (ObjectUtils.notEqual(post.getRoleId(), dto.getRoleId())) {
                        return Mono.error(new ServiceException("角色权限已存在"));
                    }
                    return Mono.empty();
                })
                .then();
    }

    /**
     * 新增角色菜单
     */
    private Mono<Void> insertRoleMenu(Long roleId, List<Long> menuIds) {
        return Flux.fromIterable(menuIds)
                .map(menuId -> {
                    SysRoleMenu roleMenu = new SysRoleMenu();
                    roleMenu.setRoleId(roleId);
                    roleMenu.setMenuId(menuId);
                    return roleMenu;
                })
                .collectList()
                .flatMapMany(sysRoleMenuRepository::saveAll)
                .then();
    }

    /**
     * 修改角色
     */
    @Transactional
    @Override
    public Mono<Void> updateRole(SysRoleDTO dto) {
        return sysRoleRepository.findById(dto.getRoleId())
                .switchIfEmpty(Mono.error(new ServiceException("角色不存在")))
                .flatMap(role -> {
                    // 校验角色名称是否唯一
                    return this.checkRoleNameUnique(dto)
                            // 校验角色权限是否唯一
                            .then(this.checkRoleKeyUnique(dto))
                            .then(Mono.defer(() -> {
                                // 更新角色信息
                                sysRoleConverter.copyProperties(dto, role);
                                return sysRoleRepository.save(role)
                                        // 删除角色与菜单关联
                                        .then(sysRoleMenuRepository.deleteByRoleId(role.getRoleId()))
                                        // 新增角色菜单
                                        .then(this.insertRoleMenu(role.getRoleId(), dto.getMenuIds()));
                            }))
                            .then();
                })
                .then();
    }

    /**
     * 修改角色状态
     */
    @Override
    public Mono<Void> updateRoleStatus(SysRoleDTO dto) {
        return sysRoleRepository.findById(dto.getRoleId())
                .switchIfEmpty(Mono.error(new ServiceException("角色不存在")))
                .flatMap(role -> {
                    role.setStatus(dto.getStatus());
                    return sysRoleRepository.save(role);
                })
                .then();
    }

    /**
     * 修改角色数据权限
     */
    @Transactional
    @Override
    public Mono<Void> updateRoleDataScope(SysRoleDTO dto) {
        return sysRoleRepository.findById(dto.getRoleId())
                .switchIfEmpty(Mono.error(new ServiceException("角色不存在")))
                .flatMap(role -> {
                    // 修改角色信息
                    role.setDataScope(dto.getDataScope());
                    return sysRoleRepository.save(role)
                            // 删除角色与部门关联
                            .then(sysRoleDeptRepository.deleteByRoleId(dto.getRoleId()))
                            // 新增角色和部门信息（数据权限）
                            .then(Flux.fromIterable(dto.getDeptIds())
                                    .map(deptId -> {
                                        SysRoleDept roleDept = new SysRoleDept();
                                        roleDept.setRoleId(dto.getRoleId());
                                        roleDept.setDeptId(deptId);
                                        return roleDept;
                                    })
                                    .collectList()
                                    .flatMapMany(sysRoleDeptRepository::saveAll)
                                    .then()
                            );
                });
    }

    /**
     * 批量选择授权用户角色
     *
     * @param roleId  角色ID
     * @param userIds 需要授权的用户数据ID
     * @return 结果
     */
    @Override
    public Mono<Void> insertAuthUsers(Long roleId, Long[] userIds) {
        return sysRoleRepository.findById(roleId)
                .switchIfEmpty(Mono.error(new ServiceException("角色不存在")))
                .then(Flux.fromArray(userIds)
                        .map(userId -> {
                            SysUserRole userRole = new SysUserRole();
                            userRole.setUserId(userId);
                            userRole.setRoleId(roleId);
                            return userRole;
                        })
                        .collectList()
                        .flatMapMany(sysUserRoleRepository::saveAll)
                        .then()
                );
    }

    /**
     * 批量取消授权用户角色
     *
     * @param roleId  角色ID
     * @param userIds 需要取消授权的用户数据ID
     * @return 结果
     */
    @Override
    public Mono<Void> deleteAuthUsers(Long roleId, Long[] userIds) {
        if (ArrayUtils.isNotEmpty(userIds)) {
            return sysUserRoleRepository.deleteByRoleIdAndUserIdIn(roleId, userIds);
        }
        return Mono.empty();
    }

    /**
     * 批量删除角色
     */
    @Transactional
    @Override
    public Mono<Void> deleteRoleByIds(List<Long> roleIds) {
        return sysRoleRepository.findAllById(roleIds)
                .flatMap(role -> {
                    checkRoleAllowed(new SysRole(role.getRoleId()));
                    checkRoleDataScope(role.getRoleId());
                    // 检查角色是否已分配
                    return sysUserRoleRepository.countByRoleId(role.getRoleId())
                            .flatMap(countUser -> {
                                if (countUser > 0) {
                                    return Mono.error(new ServiceException(String.format("角色[%s]已分配，不能删除", role.getRoleName())));
                                }
                                return Mono.empty();
                            });
                })
                // 删除角色与菜单关联
                .then(sysRoleMenuRepository.deleteByRoleIdIn(roleIds))
                // 删除角色与部门关联
                .then(sysRoleDeptRepository.deleteByRoleIdIn(roleIds))
                // 删除角色信息
                .then(sysRoleRepository.deleteAllById(roleIds))
                .then();
    }

    /**
     * 根据用户ID查询角色
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    @Override
    public List<SysRole> selectRolesByUserId(Long userId) {
        List<SysRole> userRoles = roleMapper.selectRolePermissionByUserId(userId);
        List<SysRole> roles = SpringUtils.getAopProxy(this).selectRoleList(new SysRole());
        for (SysRole role : roles) {
            for (SysRole userRole : userRoles) {
                if (role.getRoleId().longValue() == userRole.getRoleId().longValue()) {
                    role.setFlag(true);
                    break;
                }
            }
        }
        return roles;
    }

    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    @Override
    public Set<String> selectRolePermissionByUserId(Long userId) {
        List<SysRole> perms = roleMapper.selectRolePermissionByUserId(userId);
        Set<String> permsSet = new HashSet<>();
        for (SysRole perm : perms) {
            if (StringUtils.isNotNull(perm)) {
                permsSet.addAll(Arrays.asList(perm.getRoleKey().trim().split(",")));
            }
        }
        return permsSet;
    }

    /**
     * 校验角色是否允许操作
     *
     * @param role 角色信息
     */
    @Override
    public void checkRoleAllowed(SysRole role) {
        if (StringUtils.isNotNull(role.getRoleId()) && role.isAdmin()) {
            throw new ServiceException("不允许操作超级管理员角色");
        }
    }

    /**
     * 校验角色是否有数据权限
     *
     * @param roleIds 角色id
     */
    @Override
    public void checkRoleDataScope(Long... roleIds) {
        ReactiveSecurityUtils.getUserId()
                .filter(SysUser::isNotAdmin)
                .subscribe(userId -> {
                    // 校验角色是否有数据权限
                    Flux.fromArray(roleIds)
                            .flatMap(roleId -> {
                                SysRole role = new SysRole();
                                role.setRoleId(roleId);
                                List<SysRole> roles = SpringUtils.getAopProxy(this).selectRoleList(role);
                                if (StringUtils.isEmpty(roles)) {
                                    return Flux.error(new ServiceException("没有权限访问角色数据！"));
                                }
                                return Mono.empty();
                            })
                            .subscribe();
                });
    }

    /**
     * 根据条件分页查询角色数据
     *
     * @param role 角色信息
     * @return 角色数据集合信息
     */
    @Override
    @DataScope(deptAlias = "d")
    public List<SysRole> selectRoleList(SysRole role) {
        return roleMapper.selectRoleList(role);
    }

}
