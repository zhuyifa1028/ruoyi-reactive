package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.TreeselectUtils;
import com.ruoyi.system.dto.SysMenuDTO;
import com.ruoyi.system.query.SysMenuQuery;
import com.ruoyi.system.service.SysMenuService;
import com.ruoyi.system.vo.SysMenuVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@Tag(name = "菜单表 接口")
@RestController
@RequestMapping("/system/menu")
public class SysMenuController extends BaseController {

    @Resource
    private SysMenuService sysMenuService;

    @Operation(summary = "查询菜单列表")
    @PreAuthorize("hasAuthority('system:menu:list')")
    @GetMapping("/list")
    public Mono<R<List<SysMenuVO>>> list(SysMenuQuery query) {
        return sysMenuService.selectMenuList(query)
                .collectList()
                .map(R::ok);
    }

    @Operation(summary = "根据菜单ID查询信息")
    @PreAuthorize("hasAuthority('system:menu:query')")
    @GetMapping(value = "/{menuId}")
    public Mono<R<SysMenuVO>> getInfo(@PathVariable Long menuId) {
        return sysMenuService.selectMenuById(menuId)
                .map(R::ok);
    }

    @Operation(summary = "新增菜单")
    @Log(title = "菜单管理", businessType = BusinessType.INSERT)
    @PreAuthorize("hasAuthority('system:menu:add')")
    @PostMapping
    public Mono<R<Void>> add(@RequestBody @Validated SysMenuDTO dto) {
        return sysMenuService.insertMenu(dto)
                .thenReturn(R.ok());
    }

    @Operation(summary = "修改菜单")
    @Log(title = "菜单管理", businessType = BusinessType.UPDATE)
    @PreAuthorize("hasAuthority('system:menu:edit')")
    @PutMapping
    public Mono<R<Void>> edit(@RequestBody @Validated SysMenuDTO dto) {
        return sysMenuService.updateMenu(dto)
                .thenReturn(R.ok());
    }

    @Operation(summary = "删除菜单")
    @Log(title = "菜单管理", businessType = BusinessType.DELETE)
    @PreAuthorize("hasAuthority('system:menu:remove')")
    @DeleteMapping("/{menuId}")
    public Mono<R<Void>> remove(@PathVariable Long menuId) {
        return sysMenuService.deleteMenuById(menuId)
                .thenReturn(R.ok());
    }

    @Operation(summary = "获取菜单下拉树列表")
    @GetMapping("/treeselect")
    public Mono<R<List<SysMenuVO>>> treeselect(SysMenuQuery query) {
        return sysMenuService.selectMenuList(query)
                .collectList()
                .map(list -> {
                    // 将扁平列表转换为树形结构
                    return R.ok(TreeselectUtils.build(list, SysMenuVO::getMenuId, SysMenuVO::getParentId, SysMenuVO::setChildren));
                });
    }

    @Operation(summary = "加载对应角色菜单列表树")
    @GetMapping(value = "/roleMenuTreeselect/{roleId}")
    public Mono<AjaxResult> roleMenuTreeselect(@PathVariable Long roleId) {
        return sysMenuService.selectMenuList(new SysMenuQuery())
                .collectList()
                .flatMap(list -> {
                    // 将扁平列表转换为树形结构
                    List<SysMenuVO> menus = TreeselectUtils.build(list, SysMenuVO::getMenuId, SysMenuVO::getParentId, SysMenuVO::setChildren);

                    return sysMenuService.selectMenuListByRoleId(roleId)
                            .collectList()
                            .flatMap(checkedKeys -> {
                                AjaxResult ajax = AjaxResult.success();
                                ajax.put("checkedKeys", checkedKeys);
                                ajax.put("menus", menus);
                                return Mono.just(ajax);
                            });
                });
    }

}