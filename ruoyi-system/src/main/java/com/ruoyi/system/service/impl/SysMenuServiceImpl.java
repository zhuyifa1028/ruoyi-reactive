package com.ruoyi.system.service.impl;

import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.TreeSelect;
import com.ruoyi.common.core.domain.entity.SysMenu;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.security.ReactiveSecurityUtils;
import com.ruoyi.system.converter.SysMenuConverter;
import com.ruoyi.system.domain.vo.MetaVo;
import com.ruoyi.system.domain.vo.RouterVo;
import com.ruoyi.system.dto.SysMenuDTO;
import com.ruoyi.system.mapper.SysMenuMapper;
import com.ruoyi.system.mapper.SysRoleMapper;
import com.ruoyi.system.query.SysMenuQuery;
import com.ruoyi.system.repository.SysMenuRepository;
import com.ruoyi.system.service.SysMenuService;
import com.ruoyi.system.vo.SysMenuVO;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 菜单表 业务处理
 *
 * @author bugout
 * @version 2025-11-14
 */
@Service
public class SysMenuServiceImpl implements SysMenuService {

    @Resource
    private SysMenuConverter sysMenuConverter;
    @Resource
    private SysMenuRepository sysMenuRepository;

    @Resource
    private SysMenuMapper menuMapper;

    @Resource
    private SysRoleMapper roleMapper;

    /**
     * 根据用户查询系统菜单列表
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    @Override
    public List<SysMenu> selectMenuList(Long userId) {
        return selectMenuList(new SysMenu(), userId);
    }

    /**
     * 查询系统菜单列表
     *
     * @param menu 菜单信息
     * @return 菜单列表
     */
    @Override
    public List<SysMenu> selectMenuList(SysMenu menu, Long userId) {
        List<SysMenu> menuList;
        // 管理员显示所有菜单信息
        if (SysUser.isAdmin(userId)) {
            menuList = menuMapper.selectMenuList(menu);
        } else {
            menu.getParams().put("userId", userId);
            menuList = menuMapper.selectMenuListByUserId(menu);
        }
        return menuList;
    }

    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    @Override
    public Set<String> selectMenuPermsByUserId(Long userId) {
        List<String> perms = menuMapper.selectMenuPermsByUserId(userId);
        Set<String> permsSet = new HashSet<>();
        for (String perm : perms) {
            if (StringUtils.isNotEmpty(perm)) {
                permsSet.addAll(Arrays.asList(perm.trim().split(",")));
            }
        }
        return permsSet;
    }

    /**
     * 根据角色ID查询权限
     *
     * @param roleId 角色ID
     * @return 权限列表
     */
    @Override
    public Set<String> selectMenuPermsByRoleId(Long roleId) {
        List<String> perms = menuMapper.selectMenuPermsByRoleId(roleId);
        Set<String> permsSet = new HashSet<>();
        for (String perm : perms) {
            if (StringUtils.isNotEmpty(perm)) {
                permsSet.addAll(Arrays.asList(perm.trim().split(",")));
            }
        }
        return permsSet;
    }

    /**
     * 根据用户ID查询菜单
     *
     * @param userId 用户名称
     * @return 菜单列表
     */
    @Override
    public List<SysMenu> selectMenuTreeByUserId(Long userId) {
        List<SysMenu> menus;
        if (SecurityUtils.isAdmin(userId)) {
            menus = menuMapper.selectMenuTreeAll();
        } else {
            menus = menuMapper.selectMenuTreeByUserId(userId);
        }
        return getChildPerms(menus, 0);
    }

    /**
     * 根据角色ID查询菜单树信息
     *
     * @param roleId 角色ID
     * @return 选中菜单列表
     */
    @Override
    public List<Long> selectMenuListByRoleId(Long roleId) {
        SysRole role = roleMapper.selectRoleById(roleId);
        return menuMapper.selectMenuListByRoleId(roleId, role.isMenuCheckStrictly());
    }

    /**
     * 构建前端路由所需要的菜单
     *
     * @param menus 菜单列表
     * @return 路由列表
     */
    @Override
    public List<RouterVo> buildMenus(List<SysMenu> menus) {
        List<RouterVo> routers = new LinkedList<>();
        for (SysMenu menu : menus) {
            RouterVo router = new RouterVo();
            router.setHidden("1".equals(menu.getVisible()));
            router.setName(getRouteName(menu));
            router.setPath(getRouterPath(menu));
            router.setComponent(getComponent(menu));
            router.setQuery(menu.getQuery());
            router.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), StringUtils.equals("1", menu.getIsCache()), menu.getPath()));
            List<SysMenu> cMenus = menu.getChildren();
            if (StringUtils.isNotEmpty(cMenus) && UserConstants.TYPE_DIR.equals(menu.getMenuType())) {
                router.setAlwaysShow(true);
                router.setRedirect("noRedirect");
                router.setChildren(buildMenus(cMenus));
            } else if (isMenuFrame(menu)) {
                router.setMeta(null);
                List<RouterVo> childrenList = new ArrayList<>();
                RouterVo children = new RouterVo();
                children.setPath(menu.getPath());
                children.setComponent(menu.getComponent());
                children.setName(getRouteName(menu.getRouteName(), menu.getPath()));
                children.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), StringUtils.equals("1", menu.getIsCache()), menu.getPath()));
                children.setQuery(menu.getQuery());
                childrenList.add(children);
                router.setChildren(childrenList);
            } else if (menu.getParentId().intValue() == 0 && isInnerLink(menu)) {
                router.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon()));
                router.setPath("/");
                List<RouterVo> childrenList = new ArrayList<>();
                RouterVo children = new RouterVo();
                String routerPath = innerLinkReplaceEach(menu.getPath());
                children.setPath(routerPath);
                children.setComponent(UserConstants.INNER_LINK);
                children.setName(getRouteName(menu.getRouteName(), routerPath));
                children.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), menu.getPath()));
                childrenList.add(children);
                router.setChildren(childrenList);
            }
            routers.add(router);
        }
        return routers;
    }

    /**
     * 构建前端所需要树结构
     *
     * @param menus 菜单列表
     * @return 树结构列表
     */
    @Override
    public List<SysMenu> buildMenuTree(List<SysMenu> menus) {
        List<SysMenu> returnList = new ArrayList<>();
        List<Long> tempList = menus.stream().map(SysMenu::getMenuId).toList();
        for (SysMenu menu : menus) {
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(menu.getParentId())) {
                recursionFn(menus, menu);
                returnList.add(menu);
            }
        }
        if (returnList.isEmpty()) {
            returnList = menus;
        }
        return returnList;
    }

    /**
     * 构建前端所需要下拉树结构
     *
     * @param menus 菜单列表
     * @return 下拉树结构列表
     */
    @Override
    public List<TreeSelect> buildMenuTreeSelect(List<SysMenu> menus) {
        List<SysMenu> menuTrees = buildMenuTree(menus);
        return menuTrees.stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    /**
     * 获取路由名称
     *
     * @param menu 菜单信息
     * @return 路由名称
     */
    public String getRouteName(SysMenu menu) {
        // 非外链并且是一级目录（类型为目录）
        if (isMenuFrame(menu)) {
            return StringUtils.EMPTY;
        }
        return getRouteName(menu.getRouteName(), menu.getPath());
    }

    /**
     * 获取路由名称，如没有配置路由名称则取路由地址
     *
     * @param name 路由名称
     * @param path 路由地址
     * @return 路由名称（驼峰格式）
     */
    public String getRouteName(String name, String path) {
        String routerName = StringUtils.isNotEmpty(name) ? name : path;
        return StringUtils.capitalize(routerName);
    }

    /**
     * 获取路由地址
     *
     * @param menu 菜单信息
     * @return 路由地址
     */
    public String getRouterPath(SysMenu menu) {
        String routerPath = menu.getPath();
        // 内链打开外网方式
        if (menu.getParentId().intValue() != 0 && isInnerLink(menu)) {
            routerPath = innerLinkReplaceEach(routerPath);
        }
        // 非外链并且是一级目录（类型为目录）
        if (0 == menu.getParentId().intValue() && UserConstants.TYPE_DIR.equals(menu.getMenuType())
                && UserConstants.NO_FRAME.equals(menu.getIsFrame())) {
            routerPath = "/" + menu.getPath();
        }
        // 非外链并且是一级目录（类型为菜单）
        else if (isMenuFrame(menu)) {
            routerPath = "/";
        }
        return routerPath;
    }

    /**
     * 获取组件信息
     *
     * @param menu 菜单信息
     * @return 组件信息
     */
    public String getComponent(SysMenu menu) {
        String component = UserConstants.LAYOUT;
        if (StringUtils.isNotEmpty(menu.getComponent()) && !isMenuFrame(menu)) {
            component = menu.getComponent();
        } else if (StringUtils.isEmpty(menu.getComponent()) && menu.getParentId().intValue() != 0 && isInnerLink(menu)) {
            component = UserConstants.INNER_LINK;
        } else if (StringUtils.isEmpty(menu.getComponent()) && isParentView(menu)) {
            component = UserConstants.PARENT_VIEW;
        }
        return component;
    }

    /**
     * 是否为菜单内部跳转
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isMenuFrame(SysMenu menu) {
        return menu.getParentId().intValue() == 0 && UserConstants.TYPE_MENU.equals(menu.getMenuType())
                && menu.getIsFrame().equals(UserConstants.NO_FRAME);
    }

    /**
     * 是否为内链组件
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isInnerLink(SysMenu menu) {
        return menu.getIsFrame().equals(UserConstants.NO_FRAME) && StringUtils.ishttp(menu.getPath());
    }

    /**
     * 是否为parent_view组件
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isParentView(SysMenu menu) {
        return menu.getParentId().intValue() != 0 && UserConstants.TYPE_DIR.equals(menu.getMenuType());
    }

    /**
     * 根据父节点的ID获取所有子节点
     *
     * @param list     分类表
     * @param parentId 传入的父节点ID
     * @return String
     */
    public List<SysMenu> getChildPerms(List<SysMenu> list, int parentId) {
        List<SysMenu> returnList = new ArrayList<>();
        for (SysMenu t : list) {
            // 一、根据传入的某个父节点ID,遍历该父节点的所有子节点
            if (t.getParentId() == parentId) {
                recursionFn(list, t);
                returnList.add(t);
            }
        }
        return returnList;
    }

    /**
     * 递归列表
     *
     * @param list 分类表
     * @param t    子节点
     */
    private void recursionFn(List<SysMenu> list, SysMenu t) {
        // 得到子节点列表
        List<SysMenu> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysMenu tChild : childList) {
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<SysMenu> getChildList(List<SysMenu> list, SysMenu t) {
        List<SysMenu> tlist = new ArrayList<>();
        for (SysMenu n : list) {
            if (n.getParentId().longValue() == t.getMenuId().longValue()) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<SysMenu> list, SysMenu t) {
        return !getChildList(list, t).isEmpty();
    }

    /**
     * 内链域名特殊字符替换
     *
     * @return 替换后的内链域名
     */
    public String innerLinkReplaceEach(String path) {
        return StringUtils.replaceEach(path, new String[]{Constants.HTTP, Constants.HTTPS, Constants.WWW, ".", ":"},
                new String[]{"", "", "", "/", "/"});
    }

    /**
     * 查询菜单列表
     */
    @Override
    public Flux<SysMenuVO> selectMenuList(SysMenuQuery query) {
        return ReactiveSecurityUtils.getUserId()
                .flatMapMany(userId -> {
                    // 管理员显示所有菜单信息
                    if (SysUser.isAdmin(userId)) {
                        return sysMenuRepository.selectMenuList(query);
                    } else {
                        return sysMenuRepository.selectMenuListByUserId(userId, query);
                    }
                })
                .map(sysMenuConverter::toSysMenuVO);
    }

    /**
     * 根据菜单ID查询信息
     */
    @Override
    public Mono<SysMenuVO> selectMenuById(Long menuId) {
        return sysMenuRepository.findById(menuId)
                .switchIfEmpty(Mono.error(new ServiceException("菜单不存在")))
                .map(sysMenuConverter::toSysMenuVO);
    }

    /**
     * 新增菜单
     */
    @Override
    public Mono<Void> insertMenu(SysMenuDTO dto) {
        return this.checkMenuNameUnique(dto)
                .then(Mono.defer(() -> {
                    if (Objects.equals(dto.getIsFrame(), UserConstants.YES_FRAME) && !StringUtils.ishttp(dto.getPath())) {
                        return Mono.error(new ServiceException("地址必须以http(s)://开头"));
                    }
                    return Mono.empty();
                }))
                .then(Mono.defer(() -> {
                    com.ruoyi.system.entity.SysMenu menu = sysMenuConverter.toSysMenu(dto);
                    return sysMenuRepository.save(menu);
                }))
                .then();
    }

    /**
     * 校验菜单名称是否唯一
     */
    private Mono<Void> checkMenuNameUnique(SysMenuDTO dto) {
        return sysMenuRepository.selectMenuNameUnique(dto.getMenuName(), dto.getParentId())
                .flatMap(menu -> {
                    if (ObjectUtils.notEqual(menu.getMenuId(), dto.getMenuId())) {
                        return Mono.error(new ServiceException(String.format("菜单名称【%1$s】已存在 ", dto.getMenuName())));
                    }
                    return Mono.empty();
                });
    }

    /**
     * 修改菜单
     */
    @Override
    public Mono<Void> updateMenu(SysMenuDTO dto) {
        return this.checkMenuNameUnique(dto)
                .then(Mono.defer(() -> {
                    if (Objects.equals(dto.getMenuId(), dto.getParentId())) {
                        return Mono.error(new ServiceException("上级菜单不能是自己"));
                    }
                    if (Objects.equals(dto.getIsFrame(), UserConstants.YES_FRAME) && !StringUtils.ishttp(dto.getPath())) {
                        return Mono.error(new ServiceException("地址必须以http(s)://开头"));
                    }
                    return Mono.empty();
                }))
                .then(sysMenuRepository.findById(dto.getMenuId()))
                .switchIfEmpty(Mono.error(new ServiceException("菜单不存在")))
                .flatMap(menu -> {
                    sysMenuConverter.copyProperties(dto, menu);
                    return sysMenuRepository.save(menu);
                })
                .then();
    }

    /**
     * 删除菜单
     */
    @Override
    public Mono<Void> deleteMenuById(Long menuId) {
        return sysMenuRepository.countChildByMenuId(menuId)
                .flatMap(countChild -> {
                    if (countChild > 0) {
                        return Mono.error(new ServiceException("存在下级菜单，不允许删除"));
                    }
                    return Mono.empty();
                })
                .then(sysMenuRepository.countRoleByMenuId(menuId))
                .flatMap(countRole -> {
                    if (countRole > 0) {
                        return Mono.error(new ServiceException("菜单已分配，不允许删除"));
                    }
                    return Mono.empty();
                })
                .then(sysMenuRepository.deleteById(menuId));
    }

}
