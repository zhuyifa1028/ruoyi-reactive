package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.entity.SysMenu;
import com.ruoyi.system.domain.vo.RouterVo;
import com.ruoyi.system.dto.SysMenuDTO;
import com.ruoyi.system.query.SysMenuQuery;
import com.ruoyi.system.vo.SysMenuVO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;

/**
 * 菜单表 业务层
 *
 * @author bugout
 * @version 2025-11-14
 */
public interface SysMenuService {

    /**
     * 查询菜单列表
     */
    Flux<SysMenuVO> selectMenuList(SysMenuQuery query);

    /**
     * 根据菜单ID查询信息
     */
    Mono<SysMenuVO> selectMenuById(Long menuId);

    /**
     * 新增菜单
     */
    Mono<Void> insertMenu(SysMenuDTO dto);

    /**
     * 修改菜单
     */
    Mono<Void> updateMenu(SysMenuDTO dto);

    /**
     * 删除菜单
     */
    Mono<Void> deleteMenuById(Long menuId);

    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    Set<String> selectMenuPermsByUserId(Long userId);

    /**
     * 根据角色ID查询权限
     *
     * @param roleId 角色ID
     * @return 权限列表
     */
    Set<String> selectMenuPermsByRoleId(Long roleId);

    /**
     * 根据用户ID查询菜单树信息
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    List<SysMenu> selectMenuTreeByUserId(Long userId);

    /**
     * 根据角色ID查询菜单树信息
     *
     * @param roleId 角色ID
     * @return 选中菜单列表
     */
    Flux<Long> selectMenuListByRoleId(Long roleId);

    /**
     * 构建前端路由所需要的菜单
     *
     * @param menus 菜单列表
     * @return 路由列表
     */
    List<RouterVo> buildMenus(List<SysMenu> menus);

}
