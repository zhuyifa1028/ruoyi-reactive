package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.TreeselectUtils;
import com.ruoyi.system.dto.SysUserDTO;
import com.ruoyi.system.query.SysDeptQuery;
import com.ruoyi.system.query.SysUserQuery;
import com.ruoyi.system.service.SysDeptService;
import com.ruoyi.system.service.SysPostService;
import com.ruoyi.system.service.SysRoleService;
import com.ruoyi.system.service.SysUserService;
import com.ruoyi.system.vo.SysDeptVO;
import com.ruoyi.system.vo.SysUserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

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

    @Resource
    private SysPostService postService;

    @Operation(summary = "根据条件分页查询用户列表")
    @PreAuthorize("@ss.hasPermi('system:user:list')")
    @GetMapping("/list")
    public Mono<TableDataInfo> list(SysUserQuery query) {
        return sysUserService.selectUserList(query)
                .flatMap(page -> {
                    TableDataInfo info = new TableDataInfo();
                    info.setCode(HttpStatus.SUCCESS);
                    info.setMsg("查询成功");
                    info.setRows(page.getContent());
                    info.setTotal(page.getTotalElements());
                    return Mono.just(info);
                });
    }

    @Operation(summary = "根据用户ID查询用户信息")
    @PreAuthorize("@ss.hasPermi('system:user:query')")
    @GetMapping("/{userId}")
    public Mono<R<SysUserVO>> getInfo(@PathVariable Long userId) {
        sysUserService.checkUserDataScope(userId);
        return sysUserService.selectUserById(userId)
                .map(R::ok);
    }

    @Operation(summary = "新增用户")
    @Log(title = "用户管理", businessType = BusinessType.INSERT)
    @PreAuthorize("@ss.hasPermi('system:user:add')")
    @PostMapping
    public Mono<R<Void>> add(@RequestBody @Validated SysUserDTO dto) {
        deptService.checkDeptDataScope(dto.getDeptId());
        roleService.checkRoleDataScope(dto.getRoleIds().toArray(new Long[0]));
        return sysUserService.insertUser(dto)
                .thenReturn(R.ok());
    }

    @Operation(summary = "修改用户")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @PutMapping
    public Mono<R<Void>> edit(@RequestBody @Validated SysUserDTO dto) {
        sysUserService.checkUserAllowed(new SysUser(dto.getUserId()));
        sysUserService.checkUserDataScope(dto.getUserId());
        deptService.checkDeptDataScope(dto.getDeptId());
        roleService.checkRoleDataScope(dto.getRoleIds().toArray(new Long[0]));
        return sysUserService.updateUser(dto)
                .thenReturn(R.ok());
    }

    /**
     * 删除用户
     */
    @PreAuthorize("@ss.hasPermi('system:user:remove')")
    @Log(title = "用户管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{userIds}")
    public AjaxResult remove(@PathVariable Long[] userIds) {
        if (ArrayUtils.contains(userIds, getUserId())) {
            return error("当前用户不能删除");
        }
        return toAjax(sysUserService.deleteUserByIds(userIds));
    }

    /**
     * 重置密码
     */
    @PreAuthorize("@ss.hasPermi('system:user:resetPwd')")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/resetPwd")
    public AjaxResult resetPwd(@RequestBody SysUser user) {
        sysUserService.checkUserAllowed(user);
        sysUserService.checkUserDataScope(user.getUserId());
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        user.setUpdateBy(getUsername());
        return toAjax(sysUserService.resetPwd(user));
    }

    /**
     * 状态修改
     */
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody SysUser user) {
        sysUserService.checkUserAllowed(user);
        sysUserService.checkUserDataScope(user.getUserId());
        user.setUpdateBy(getUsername());
        return toAjax(sysUserService.updateUserStatus(user));
    }

    /**
     * 根据用户编号获取授权角色
     */
    @PreAuthorize("@ss.hasPermi('system:user:query')")
    @GetMapping("/authRole/{userId}")
    public AjaxResult authRole(@PathVariable("userId") Long userId) {
        AjaxResult ajax = AjaxResult.success();
//        SysUser user = sysUserService.selectUserById(userId);
//        List<SysRole> roles = roleService.selectRolesByUserId(userId);
//        ajax.put("user", user);
//        ajax.put("roles", SysUser.isAdmin(userId) ? roles : roles.stream().filter(r -> !r.isAdmin()).collect(Collectors.toList()));
        return ajax;
    }

    /**
     * 用户授权角色
     */
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @Log(title = "用户管理", businessType = BusinessType.GRANT)
    @PutMapping("/authRole")
    public AjaxResult insertAuthRole(Long userId, List<Long> roleIds) {
        sysUserService.checkUserDataScope(userId);
        roleService.checkRoleDataScope(roleIds.toArray(new Long[0]));
        sysUserService.insertUserAuth(userId, roleIds);
        return success();
    }

    @Operation(summary = "获取部门下拉树列表")
    @PreAuthorize("@ss.hasPermi('system:user:list')")
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
