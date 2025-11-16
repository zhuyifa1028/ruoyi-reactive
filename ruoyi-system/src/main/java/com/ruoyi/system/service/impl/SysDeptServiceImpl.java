package com.ruoyi.system.service.impl;

import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.framework.security.ReactiveSecurityUtils;
import com.ruoyi.system.converter.SysDeptConverter;
import com.ruoyi.system.domain.SysRoleDept;
import com.ruoyi.system.dto.SysDeptDTO;
import com.ruoyi.system.mapper.SysDeptMapper;
import com.ruoyi.system.query.SysDeptQuery;
import com.ruoyi.system.repository.SysDeptRepository;
import com.ruoyi.system.repository.SysRoleDeptRepository;
import com.ruoyi.system.service.SysDeptService;
import com.ruoyi.system.vo.SysDeptVO;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * 部门管理 服务实现
 *
 * @author ruoyi
 */
@Service
public class SysDeptServiceImpl implements SysDeptService {

    @Resource
    private SysDeptConverter sysDeptConverter;
    @Resource
    private SysDeptRepository sysDeptRepository;

    @Resource
    private SysRoleDeptRepository sysRoleDeptRepository;

    @Resource
    private SysDeptMapper deptMapper;

    /**
     * 查询部门列表
     */
    @Override
    public Flux<SysDeptVO> selectDeptList(SysDeptQuery query) {
        return sysDeptRepository.selectDeptList(query)
                .map(sysDeptConverter::toSysDeptVO);
    }

    /**
     * 查询部门列表（排除指定节点）
     */
    @Override
    public Flux<SysDeptVO> selectDeptListExclude(Long deptId) {
        return sysDeptRepository.selectDeptList(new SysDeptQuery())
                .filter(dept -> {
                    // 条件1：排除当前部门
                    if (Objects.equals(dept.getDeptId(), deptId)) {
                        return false;
                    }
                    // 条件2：排除祖先ID包含当前部门ID的部门
                    Set<String> set = org.springframework.util.StringUtils.commaDelimitedListToSet(dept.getAncestors());
                    return BooleanUtils.isFalse(set.contains(deptId + ""));
                })
                .map(sysDeptConverter::toSysDeptVO);
    }

    /**
     * 根据部门ID查询信息
     */
    @Override
    public Mono<SysDeptVO> selectDeptById(Long deptId) {
        return this.checkDeptAuthority(deptId)
                .then(sysDeptRepository.findById(deptId).switchIfEmpty(Mono.error(new ServiceException("部门不存在"))))
                .map(sysDeptConverter::toSysDeptVO);
    }

    /**
     * 新增部门
     */
    @Override
    public Mono<Void> insertDept(SysDeptDTO dto) {
        return this.checkDeptNameUnique(dto)
                .then(sysDeptRepository.findById(dto.getParentId()).switchIfEmpty(Mono.error(new ServiceException("上级部门不存在"))))
                .flatMap(parent -> {
                    // 如果父节点不为正常状态,则不允许新增子节点
                    if (ObjectUtils.notEqual(parent.getStatus(), UserConstants.DEPT_NORMAL)) {
                        return Mono.error(new ServiceException("上级部门停用，不允许新增"));
                    }

                    com.ruoyi.system.entity.SysDept dept = sysDeptConverter.toSysDept(dto);
                    dept.setAncestors(parent.getAncestors() + "," + parent.getDeptId());
                    return sysDeptRepository.save(dept);
                })
                .then();
    }

    /**
     * 校验部门名称是否唯一
     */
    private Mono<Void> checkDeptNameUnique(SysDeptDTO dto) {
        return sysDeptRepository.selectDeptNameUnique(dto.getDeptName(), dto.getParentId())
                .flatMap(dept -> {
                    if (ObjectUtils.notEqual(dept.getDeptId(), dto.getDeptId())) {
                        return Mono.error(new ServiceException(String.format("部门名称【%1$s】已存在 ", dto.getDeptName())));
                    }
                    return Mono.empty();
                });
    }

    /**
     * 修改部门
     */
    @Override
    public Mono<Void> updateDept(SysDeptDTO dto) {
        return this.checkDeptAuthority(dto.getDeptId())
                .then(this.checkDeptNameUnique(dto))
                .then(Mono.defer(() -> {
                    if (Objects.equals(dto.getDeptId(), dto.getParentId())) {
                        return Mono.error(new ServiceException("上级部门不能是自己"));
                    }
                    if (StringUtils.equals(UserConstants.DEPT_DISABLE, dto.getStatus())) {
                        return sysDeptRepository.countNormalChildrenByDeptId(dto.getDeptId())
                                .flatMap(countNormalChildren -> {
                                    if (countNormalChildren > 0) {
                                        return Mono.error(new ServiceException("该部门包含未停用的子部门"));
                                    }
                                    return Mono.empty();
                                });
                    }
                    return Mono.empty();
                }))
                .then(sysDeptRepository.findById(dto.getDeptId()).switchIfEmpty(Mono.error(new ServiceException("部门不存在"))))
                .zipWith(sysDeptRepository.findById(dto.getParentId()).switchIfEmpty(Mono.error(new ServiceException("上级部门不存在"))))
                .flatMap(tuple -> {
                    com.ruoyi.system.entity.SysDept parent = tuple.getT2();
                    com.ruoyi.system.entity.SysDept dept = tuple.getT1();

                    // 构建新的 ancestors
                    String newAncestors = parent.getAncestors() + "," + parent.getDeptId();
                    String oldAncestors = dept.getAncestors();

                    sysDeptConverter.copyProperties(dto, dept);
                    dept.setAncestors(newAncestors);

                    // 查询子部门并更新其 ancestors
                    return sysDeptRepository.selectChildrenDeptById(dto.getDeptId())
                            .map(child -> {
                                child.setAncestors(child.getAncestors().replaceFirst(oldAncestors, newAncestors));
                                return child;
                            })
                            .transform(children -> sysDeptRepository.saveAll(children))
                            // 更新当前部门
                            .then(sysDeptRepository.save(dept))
                            // 如果是启用状态，则启用上级部门
                            .flatMap(result -> {
                                if (Objects.equals(dept.getStatus(), UserConstants.DEPT_NORMAL)) {
                                    Set<String> set = org.springframework.util.StringUtils.commaDelimitedListToSet(dept.getAncestors());
                                    if (CollectionUtils.isNotEmpty(set)) {
                                        return sysDeptRepository.updateParentNormal(set);
                                    }
                                }
                                return Mono.empty();
                            });
                });
    }

    /**
     * 校验部门是否有数据权限
     */
    private Mono<Void> checkDeptAuthority(Long deptId) {
        return ReactiveSecurityUtils.getUserId()
                .filter(SysUser::isNotAdmin)
                .flatMap(userId -> {
                    if (SysUser.isNotAdmin(userId)) {
                        SysDept dept = new SysDept();
                        dept.setDeptId(deptId);
                        List<SysDept> depts = SpringUtils.getAopProxy(this).selectDeptList(dept);
                        if (StringUtils.isEmpty(depts)) {
                            return Mono.error(new ServiceException("没有权限访问部门数据！"));
                        }
                    }
                    return Mono.empty();
                });
    }

    /**
     * 删除部门
     */
    @Override
    public Mono<Void> deleteDeptById(Long deptId) {
        return sysDeptRepository.countChildByDeptId(deptId)
                .flatMap(countChild -> {
                    if (countChild > 0) {
                        return Mono.error(new ServiceException("存在下级部门，不允许删除"));
                    }
                    return Mono.empty();
                })
                .then(sysDeptRepository.countUserByDeptId(deptId))
                .flatMap(countUser -> {
                    if (countUser > 0) {
                        return Mono.error(new ServiceException("部门存在用户，不允许删除"));
                    }
                    return Mono.empty();
                })
                .then(this.checkDeptAuthority(deptId))
                .then(sysDeptRepository.deleteByDeptId(deptId));
    }

    @DataScope(deptAlias = "d")
    @Override
    public List<SysDept> selectDeptList(SysDept dept) {
        return deptMapper.selectDeptList(dept);
    }

    /**
     * 根据角色ID查询选中部门列表
     */
    @Override
    public Flux<Long> selectDeptListByRoleId(Long roleId) {
        return sysRoleDeptRepository.findAllByRoleId(roleId)
                .map(SysRoleDept::getDeptId);
    }

    /**
     * 校验部门是否有数据权限
     *
     * @param deptId 部门id
     */
    @Override
    public void checkDeptDataScope(Long deptId) {
        ReactiveSecurityUtils.getUserId()
                .filter(SysUser::isNotAdmin)
                .subscribe(__ -> {
                    if (StringUtils.isNotNull(deptId)) {
                        SysDept dept = new SysDept();
                        dept.setDeptId(deptId);
                        List<SysDept> depts = SpringUtils.getAopProxy(this).selectDeptList(dept);
                        if (StringUtils.isEmpty(depts)) {
                            throw new ServiceException("没有权限访问部门数据！");
                        }
                    }
                });
    }

}
