package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.TreeselectUtils;
import com.ruoyi.system.dto.SysRoleDTO;
import com.ruoyi.system.query.SysDeptQuery;
import com.ruoyi.system.query.SysRoleQuery;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.system.service.SysDeptService;
import com.ruoyi.system.service.SysRoleService;
import com.ruoyi.system.vo.SysDeptVO;
import com.ruoyi.system.vo.SysRoleVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@Tag(name = "角色表 接口")
@RestController
@RequestMapping("/system/role")
public class SysRoleController extends BaseController {

    @Resource
    private SysRoleService sysRoleService;

    @Resource
    private ISysUserService userService;

    @Resource
    private SysDeptService deptService;

    @Operation(summary = "根据条件分页查询角色列表")
    @PreAuthorize("@ss.hasPermi('system:role:list')")
    @GetMapping("/list")
    public Mono<TableDataInfo> list(SysRoleQuery query) {
        return sysRoleService.selectRoleList(query)
                .flatMap(page -> {
                    TableDataInfo info = new TableDataInfo();
                    info.setCode(HttpStatus.SUCCESS);
                    info.setMsg("查询成功");
                    info.setRows(page.getContent());
                    info.setTotal(page.getTotalElements());
                    return Mono.just(info);
                });
    }

    @Operation(summary = "通过角色ID查询角色信息")
    @PreAuthorize("@ss.hasPermi('system:role:query')")
    @GetMapping(value = "/{roleId}")
    public Mono<R<SysRoleVO>> getInfo(@PathVariable Long roleId) {
        sysRoleService.checkRoleDataScope(roleId);
        return sysRoleService.selectRoleById(roleId)
                .map(R::ok);
    }

    @Operation(summary = "新增角色")
    @Log(title = "角色管理", businessType = BusinessType.INSERT)
    @PreAuthorize("@ss.hasPermi('system:role:add')")
    @PostMapping
    public Mono<R<Void>> add(@RequestBody @Validated SysRoleDTO dto) {
        return sysRoleService.insertRole(dto)
                .thenReturn(R.ok());
    }

    @Operation(summary = "修改角色")
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @PutMapping
    public Mono<R<Void>> edit(@RequestBody @Validated SysRoleDTO dto) {
        sysRoleService.checkRoleAllowed(new SysRole(dto.getRoleId()));
        sysRoleService.checkRoleDataScope(dto.getRoleId());
        return sysRoleService.updateRole(dto)
                .thenReturn(R.ok());
    }

    @Operation(summary = "修改角色状态")
    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public Mono<R<Void>> changeStatus(@RequestBody SysRoleDTO dto) {
        sysRoleService.checkRoleAllowed(new SysRole(dto.getRoleId()));
        sysRoleService.checkRoleDataScope(dto.getRoleId());
        return sysRoleService.updateRoleStatus(dto)
                .thenReturn(R.ok());
    }

    @Operation(summary = "获取对应角色部门树列表")
    @PreAuthorize("@ss.hasPermi('system:role:query')")
    @GetMapping(value = "/deptTree/{roleId}")
    public Mono<AjaxResult> deptTree(@PathVariable("roleId") Long roleId) {
        return deptService.selectDeptList(new SysDeptQuery())
                .collectList()
                .flatMap(list -> {
                    // 将扁平列表转换为树形结构
                    List<SysDeptVO> depts = TreeselectUtils.build(list, SysDeptVO::getDeptId, SysDeptVO::getParentId, SysDeptVO::setChildren);

                    return deptService.selectDeptListByRoleId(roleId)
                            .collectList()
                            .flatMap(checkedKeys -> {
                                AjaxResult ajax = AjaxResult.success();
                                ajax.put("checkedKeys", checkedKeys);
                                ajax.put("depts", depts);
                                return Mono.just(ajax);
                            });
                });
    }

    @Operation(summary = "修改角色数据权限")
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @PutMapping("/dataScope")
    public Mono<R<Void>> dataScope(@RequestBody SysRoleDTO dto) {
        sysRoleService.checkRoleAllowed(new SysRole(dto.getRoleId()));
        sysRoleService.checkRoleDataScope(dto.getRoleId());
        return sysRoleService.updateRoleDataScope(dto)
                .thenReturn(R.ok());
    }

    @Operation(summary = "根据条件分页查询已分配用户角色列表")
    @PreAuthorize("@ss.hasPermi('system:role:list')")
    @GetMapping("/authUser/allocatedList")
    public TableDataInfo allocatedList(SysUser user) {
        startPage();
        List<SysUser> list = userService.selectAllocatedList(user);
        return getDataTable(list);
    }

    @Operation(summary = "根据条件分页查询未分配用户角色列表")
    @PreAuthorize("@ss.hasPermi('system:role:list')")
    @GetMapping("/authUser/unallocatedList")
    public TableDataInfo unallocatedList(SysUser user) {
        startPage();
        List<SysUser> list = userService.selectUnallocatedList(user);
        return getDataTable(list);
    }

    @Operation(summary = "批量授权用户角色")
    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @Log(title = "角色管理", businessType = BusinessType.GRANT)
    @PutMapping("/authUser/selectAll")
    public Mono<R<Void>> selectAuthUserAll(Long roleId, Long[] userIds) {
        sysRoleService.checkRoleDataScope(roleId);
        return sysRoleService.insertAuthUsers(roleId, userIds)
                .thenReturn(R.ok());
    }

    @Operation(summary = "批量取消授权用户角色")
    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @Log(title = "角色管理", businessType = BusinessType.GRANT)
    @PutMapping("/authUser/cancelAll")
    public Mono<R<Void>> cancelAuthUserAll(Long roleId, Long[] userIds) {
        return sysRoleService.deleteAuthUsers(roleId, userIds)
                .thenReturn(R.ok());
    }

    @Operation(summary = "批量删除角色")
    @Log(title = "角色管理", businessType = BusinessType.DELETE)
    @PreAuthorize("@ss.hasPermi('system:role:remove')")
    @DeleteMapping("/{roleIds}")
    public Mono<R<Void>> remove(@PathVariable List<Long> roleIds) {
        return sysRoleService.deleteRoleByIds(roleIds)
                .thenReturn(R.ok());
    }

    @Operation(summary = "获取角色选择框列表")
    @PreAuthorize("@ss.hasPermi('system:role:query')")
    @GetMapping("/optionselect")
    public AjaxResult optionselect() {
        return success(sysRoleService.selectRoleList(new SysRole()));
    }

}
