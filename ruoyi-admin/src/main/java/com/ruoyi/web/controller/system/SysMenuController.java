package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.domain.entity.SysMenu;
import com.ruoyi.common.enums.BusinessType;
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
    private SysMenuService menuService;

    @Operation(summary = "查询菜单列表")
    @PreAuthorize("@ss.hasPermi('system:menu:list')")
    @GetMapping("/list")
    public Mono<R<List<SysMenuVO>>> list(SysMenuQuery query) {
        return menuService.selectMenuList(query)
                .collectList()
                .map(R::ok);
    }

    @Operation(summary = "根据菜单ID查询信息")
    @PreAuthorize("@ss.hasPermi('system:menu:query')")
    @GetMapping(value = "/{menuId}")
    public Mono<R<SysMenuVO>> getInfo(@PathVariable Long menuId) {
        return menuService.selectMenuById(menuId)
                .map(R::ok);
    }

    @Operation(summary = "新增菜单")
    @Log(title = "菜单管理", businessType = BusinessType.INSERT)
    @PreAuthorize("@ss.hasPermi('system:menu:add')")
    @PostMapping
    public Mono<R<Void>> add(@RequestBody @Validated SysMenuDTO dto) {
        return menuService.insertMenu(dto)
                .thenReturn(R.ok());
    }

    @Operation(summary = "修改菜单")
    @Log(title = "菜单管理", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('system:menu:edit')")
    @PutMapping
    public Mono<R<Void>> edit(@RequestBody @Validated SysMenuDTO dto) {
        return menuService.updateMenu(dto)
                .thenReturn(R.ok());
    }

    @Operation(summary = "删除菜单")
    @Log(title = "菜单管理", businessType = BusinessType.DELETE)
    @PreAuthorize("@ss.hasPermi('system:menu:remove')")
    @DeleteMapping("/{menuId}")
    public Mono<R<Void>> remove(@PathVariable Long menuId) {
        return menuService.deleteMenuById(menuId)
                .thenReturn(R.ok());
    }


    /**
     * 获取菜单下拉树列表
     */
    @GetMapping("/treeselect")
    public AjaxResult treeselect(SysMenu menu) {
        List<SysMenu> menus = menuService.selectMenuList(menu, getUserId());
        return success(menuService.buildMenuTreeSelect(menus));
    }

    /**
     * 加载对应角色菜单列表树
     */
    @GetMapping(value = "/roleMenuTreeselect/{roleId}")
    public AjaxResult roleMenuTreeselect(@PathVariable("roleId") Long roleId) {
        List<SysMenu> menus = menuService.selectMenuList(getUserId());
        AjaxResult ajax = AjaxResult.success();
        ajax.put("checkedKeys", menuService.selectMenuListByRoleId(roleId));
        ajax.put("menus", menuService.buildMenuTreeSelect(menus));
        return ajax;
    }

}