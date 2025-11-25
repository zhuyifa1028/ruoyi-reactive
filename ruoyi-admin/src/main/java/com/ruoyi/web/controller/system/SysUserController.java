package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.TreeselectUtils;
import com.ruoyi.framework.security.ReactiveSecurityUtils;
import com.ruoyi.framework.webflux.model.P;
import com.ruoyi.framework.webflux.model.R;
import com.ruoyi.system.dto.SysUserDTO;
import com.ruoyi.system.query.SysDeptQuery;
import com.ruoyi.system.query.SysUserQuery;
import com.ruoyi.system.service.SysDeptService;
import com.ruoyi.system.service.SysRoleService;
import com.ruoyi.system.service.SysUserService;
import com.ruoyi.system.vo.SysDeptVO;
import com.ruoyi.system.vo.SysUserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "用户表 接口")
@RestController
@RequestMapping("/system/user")
public class SysUserController extends BaseController {

    @Resource
    private SysUserService sysUserService;

    @Resource
    private SysRoleService roleService;

    @Resource
    private SysDeptService deptService;

    @Operation(summary = "根据条件分页查询用户列表")
    @PreAuthorize("hasAuthority('system:user:list')")
    @GetMapping("/list")
    public Mono<R<List<SysUserVO>>> list(@ParameterObject SysUserQuery query) {
        return sysUserService.selectUserList(query)
                .map(P::ok);
    }

    @Operation(summary = "根据用户ID查询用户信息")
    @PreAuthorize("hasAuthority('system:user:query')")
    @GetMapping("/{userId}")
    public Mono<R<SysUserVO>> getInfo(@PathVariable Long userId) {
        return sysUserService.selectUserById(userId)
                .map(R::ok);
    }

    @Operation(summary = "新增用户")
    @Log(title = "用户管理", businessType = BusinessType.INSERT)
    @PreAuthorize("hasAuthority('system:user:add')")
    @PostMapping
    public Mono<R<Void>> add(@RequestBody @Validated SysUserDTO dto) {
        return sysUserService.insertUser(dto)
                .thenReturn(R.ok());
    }

    @Operation(summary = "修改用户")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PreAuthorize("hasAuthority('system:user:edit')")
    @PutMapping
    public Mono<R<Void>> edit(@RequestBody @Validated SysUserDTO dto) {
        return sysUserService.updateUser(dto)
                .thenReturn(R.ok());
    }

    @Operation(summary = "批量删除用户")
    @Log(title = "用户管理", businessType = BusinessType.DELETE)
    @PreAuthorize("hasAuthority('system:user:remove')")
    @DeleteMapping("/{userIds}")
    public Mono<R<Void>> remove(@PathVariable List<Long> userIds) {
        return sysUserService.deleteUserByIds(userIds)
                .thenReturn(R.ok());
    }

    /**
     * 重置密码
     */
    @PreAuthorize("hasAuthority('system:user:resetPwd')")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/resetPwd")
    public AjaxResult resetPwd(@RequestBody SysUser user) {
        sysUserService.checkUserAllowed(user);
        sysUserService.checkUserDataScope(user.getUserId());
        user.setPassword(ReactiveSecurityUtils.encryptPassword(user.getPassword()));
        return toAjax(sysUserService.resetPwd(user));
    }

    /**
     * 状态修改
     */
    @PreAuthorize("hasAuthority('system:user:edit')")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody SysUser user) {
        sysUserService.checkUserAllowed(user);
        sysUserService.checkUserDataScope(user.getUserId());
        return toAjax(sysUserService.updateUserStatus(user));
    }

    /**
     * 根据用户编号获取授权角色
     */
    @PreAuthorize("hasAuthority('system:user:query')")
    @GetMapping("/authRole/{userId}")
    public Mono<AjaxResult> authRole(@PathVariable("userId") Long userId) {
        AjaxResult ajax = AjaxResult.success();
        return sysUserService.selectUserById(userId)
                .map(user -> {
                    List<SysRole> roles = roleService.selectRolesByUserId(userId);
                    ajax.put("user", user);
                    ajax.put("roles", SysUser.isAdmin(userId) ? roles : roles.stream().filter(r -> !r.isAdmin()).collect(Collectors.toList()));
                    return ajax;
                });
    }

    /**
     * 用户授权角色
     */
    @PreAuthorize("hasAuthority('system:user:edit')")
    @Log(title = "用户管理", businessType = BusinessType.GRANT)
    @PutMapping("/authRole")
    public AjaxResult insertAuthRole(Long userId, List<Long> roleIds) {
        sysUserService.checkUserDataScope(userId);
        roleService.checkRoleDataScope(roleIds.toArray(new Long[0]));
        sysUserService.insertUserAuth(userId, roleIds);
        return success();
    }

    @Operation(summary = "获取部门下拉树列表")
    @PreAuthorize("hasAuthority('system:user:list')")
    @GetMapping("/deptTree")
    public Mono<R<List<SysDeptVO>>> deptTree(SysDeptQuery query) {
        return deptService.selectDeptList(query)
                .collectList()
                .map(list -> {
                    // 将扁平列表转换为树形结构
                    return R.ok(TreeselectUtils.build(list, SysDeptVO::getDeptId, SysDeptVO::getParentId, SysDeptVO::setChildren));
                });
    }
}
