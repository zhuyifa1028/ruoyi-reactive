package com.ruoyi.system.service.impl;

import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.framework.security.ReactiveSecurityUtils;
import com.ruoyi.system.converter.SysUserConverter;
import com.ruoyi.system.domain.SysUserPost;
import com.ruoyi.system.domain.SysUserRole;
import com.ruoyi.system.dto.SysUserDTO;
import com.ruoyi.system.entity.SysPost;
import com.ruoyi.system.mapper.*;
import com.ruoyi.system.query.SysUserQuery;
import com.ruoyi.system.repository.SysUserPostRepository;
import com.ruoyi.system.repository.SysUserRepository;
import com.ruoyi.system.repository.SysUserRoleRepository;
import com.ruoyi.system.service.SysUserService;
import com.ruoyi.system.vo.SysUserVO;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.support.ReactivePageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户表 业务处理
 *
 * @author bugout
 * @version 2025-11-14
 */
@Service
public class SysUserServiceImpl implements SysUserService {

    @Resource
    private SysUserConverter sysUserConverter;
    @Resource
    private SysUserRepository sysUserRepository;

    @Resource
    private SysUserPostRepository sysUserPostRepository;
    @Resource
    private SysUserRoleRepository sysUserRoleRepository;

    @Resource
    private SysUserMapper userMapper;

    @Resource
    private SysRoleMapper roleMapper;

    @Resource
    private SysPostMapper postMapper;

    @Resource
    private SysUserRoleMapper userRoleMapper;

    @Resource
    private SysUserPostMapper userPostMapper;

    /**
     * 根据条件分页查询用户列表
     */
    @Override
    public Mono<Page<SysUserVO>> selectUserList(SysUserQuery query) {
        return sysUserRepository.selectUserList(query)
                .map(sysUserConverter::toSysUserVO)
                .collectList()
                .flatMap(list -> {
                    Mono<Long> count = sysUserRepository.selectUserCount(query);
                    return ReactivePageableExecutionUtils.getPage(list, query.pageable(), count);
                })
                .defaultIfEmpty(Page.empty(query.pageable()));
    }

    /**
     * 根据用户ID查询用户信息
     */
    @Override
    public Mono<SysUserVO> selectUserById(Long userId) {
        return sysUserRepository.findById(userId)
                .switchIfEmpty(Mono.error(new ServiceException("用户不存在")))
                .map(sysUserConverter::toSysUserVO);
    }

    /**
     * 新增用户
     */
    @Transactional
    @Override
    public Mono<Void> insertUser(SysUserDTO dto) {
        // 校验用户名称是否唯一
        return this.checkUserNameUnique(dto)
                // 校验手机号码是否唯一
                .then(this.checkPhoneUnique(dto))
                // 校验email是否唯一
                .then(this.checkEmailUnique(dto))
                .then(Mono.defer(() -> {
                    // 保存用户信息
                    com.ruoyi.system.entity.SysUser user = sysUserConverter.toSysUser(dto);
                    user.setPassword(ReactiveSecurityUtils.encryptPassword(user.getPassword()));
                    return sysUserRepository.save(user)
                            // 返回用户ID
                            .map(com.ruoyi.system.entity.SysUser::getUserId);
                }))
                .flatMap(userId -> {
                    // 新增用户与岗位关联
                    return this.insertUserPost(userId, dto.getPostIds())
                            // 新增用户与角色关联
                            .then(this.insertUserRole(userId, dto.getRoleIds()));
                });
    }

    /**
     * 校验用户名称是否唯一
     */
    private Mono<Void> checkUserNameUnique(SysUserDTO dto) {
        return sysUserRepository.findByUserName(dto.getUserName())
                .flatMap(info -> {
                    if (ObjectUtils.notEqual(info.getUserId(), dto.getUserId())) {
                        return Mono.error(new ServiceException(String.format("登录账号【%1$s】已存在 ", dto.getUserName())));
                    }
                    return Mono.empty();
                });
    }

    /**
     * 校验手机号码是否唯一
     */
    private Mono<Void> checkPhoneUnique(SysUserDTO dto) {
        if (org.apache.commons.lang3.StringUtils.isBlank(dto.getPhonenumber())) {
            return Mono.empty();
        }

        return sysUserRepository.findByPhonenumber(dto.getPhonenumber())
                .flatMap(info -> {
                    if (ObjectUtils.notEqual(info.getUserId(), dto.getUserId())) {
                        return Mono.error(new ServiceException(String.format("手机号码【%1$s】已存在 ", dto.getPhonenumber())));
                    }
                    return Mono.empty();
                });
    }

    /**
     * 校验email是否唯一
     */
    private Mono<Void> checkEmailUnique(SysUserDTO dto) {
        if (org.apache.commons.lang3.StringUtils.isBlank(dto.getEmail())) {
            return Mono.empty();
        }

        return sysUserRepository.findByEmail(dto.getEmail())
                .flatMap(info -> {
                    if (ObjectUtils.notEqual(info.getUserId(), dto.getUserId())) {
                        return Mono.error(new ServiceException(String.format("邮箱账号【%1$s】已存在 ", dto.getEmail())));
                    }
                    return Mono.empty();
                });
    }

    /**
     * 新增用户岗位信息
     */
    public Mono<Void> insertUserPost(Long userId, List<Long> postIds) {
        return Flux.fromIterable(postIds)
                .map(postId -> {
                    SysUserPost up = new SysUserPost();
                    up.setUserId(userId);
                    up.setPostId(postId);
                    return up;
                })
                .collectList()
                .flatMapMany(sysUserPostRepository::saveAll)
                .then();
    }

    /**
     * 新增用户角色信息
     */
    public Mono<Void> insertUserRole(Long userId, List<Long> roleIds) {
        return Flux.fromIterable(roleIds)
                .map(roleId -> {
                    SysUserRole up = new SysUserRole();
                    up.setUserId(userId);
                    up.setRoleId(roleId);
                    return up;
                })
                .collectList()
                .flatMapMany(sysUserRoleRepository::saveAll)
                .then();
    }

    /**
     * 修改用户
     */
    @Transactional
    @Override
    public Mono<Void> updateUser(SysUserDTO dto) {
        return sysUserRepository.findById(dto.getUserId())
                .switchIfEmpty(Mono.error(new ServiceException("用户不存在")))
                .flatMap(user -> {
                    // 校验用户名称是否唯一
                    return this.checkUserNameUnique(dto)
                            // 校验手机号码是否唯一
                            .then(this.checkPhoneUnique(dto))
                            // 校验email是否唯一
                            .then(this.checkEmailUnique(dto))
                            .then(Mono.defer(() -> {
                                // 保存用户信息
                                sysUserConverter.copyProperties(dto, user);
                                user.setPassword(ReactiveSecurityUtils.encryptPassword(dto.getPassword()));
                                return sysUserRepository.save(user);
                            }))
                            .then(Mono.defer(() -> {
                                // 删除用户与角色关联
                                return sysUserPostRepository.deleteByUserId(dto.getUserId())
                                        // 新增用户与岗位关联
                                        .then(this.insertUserPost(dto.getUserId(), dto.getPostIds()))
                                        // 删除用户与岗位关联
                                        .then(sysUserRoleRepository.deleteByUserId(dto.getUserId()))
                                        // 新增用户与角色关联
                                        .then(this.insertUserRole(dto.getUserId(), dto.getRoleIds()));
                            }));
                });
    }

    /**
     * 根据条件分页查询用户列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SysUser> selectUserList(SysUser user) {
        return userMapper.selectUserList(user);
    }

    /**
     * 根据条件分页查询已分配用户角色列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SysUser> selectAllocatedList(SysUser user) {
        return userMapper.selectAllocatedList(user);
    }

    /**
     * 根据条件分页查询未分配用户角色列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SysUser> selectUnallocatedList(SysUser user) {
        return userMapper.selectUnallocatedList(user);
    }

    /**
     * 通过用户名查询用户
     *
     * @param userName 用户名
     * @return 用户对象信息
     */
    @Override
    public SysUser selectUserByUserName(String userName) {
        return userMapper.selectUserByUserName(userName);
    }


    /**
     * 查询用户所属角色组
     *
     * @param userName 用户名
     * @return 结果
     */
    @Override
    public String selectUserRoleGroup(String userName) {
        List<SysRole> list = roleMapper.selectRolesByUserName(userName);
        if (CollectionUtils.isEmpty(list)) {
            return StringUtils.EMPTY;
        }
        return list.stream().map(SysRole::getRoleName).collect(Collectors.joining(","));
    }

    /**
     * 查询用户所属岗位组
     *
     * @param userName 用户名
     * @return 结果
     */
    @Override
    public String selectUserPostGroup(String userName) {
        List<SysPost> list = postMapper.selectPostsByUserName(userName);
        if (CollectionUtils.isEmpty(list)) {
            return StringUtils.EMPTY;
        }
        return list.stream().map(SysPost::getPostName).collect(Collectors.joining(","));
    }


    /**
     * 校验用户是否允许操作
     *
     * @param user 用户信息
     */
    @Override
    public void checkUserAllowed(SysUser user) {
        if (StringUtils.isNotNull(user.getUserId()) && user.isAdmin()) {
            throw new ServiceException("不允许操作超级管理员用户");
        }
    }

    /**
     * 校验用户是否有数据权限
     *
     * @param userId 用户id
     */
    @Override
    public void checkUserDataScope(Long userId) {
        ReactiveSecurityUtils.getUserId()
                .filter(SysUser::isNotAdmin)
                .subscribe(u -> {
                    SysUser user = new SysUser();
                    user.setUserId(userId);
                    List<SysUser> users = SpringUtils.getAopProxy(this).selectUserList(user);
                    if (StringUtils.isEmpty(users)) {
                        throw new ServiceException("没有权限访问用户数据！");
                    }
                });
    }

    /**
     * 用户授权角色
     *
     * @param userId  用户ID
     * @param roleIds 角色组
     */
    @Override
    @Transactional
    public void insertUserAuth(Long userId, List<Long> roleIds) {
        userRoleMapper.deleteUserRoleByUserId(userId);
        insertUserRole(userId, roleIds).subscribe();
    }

    /**
     * 修改用户状态
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int updateUserStatus(SysUser user) {
        return userMapper.updateUserStatus(user.getUserId(), user.getStatus());
    }

    /**
     * 修改用户基本信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int updateUserProfile(SysUser user) {
        return userMapper.updateUser(user);
    }

    /**
     * 修改用户头像
     *
     * @param userId 用户ID
     * @param avatar 头像地址
     * @return 结果
     */
    @Override
    public boolean updateUserAvatar(Long userId, String avatar) {
        return userMapper.updateUserAvatar(userId, avatar) > 0;
    }

    /**
     * 更新用户登录信息（IP和登录时间）
     *
     * @param userId    用户ID
     * @param loginIp   登录IP地址
     * @param loginDate 登录时间
     */
    public void updateLoginInfo(Long userId, String loginIp, Date loginDate) {
        userMapper.updateLoginInfo(userId, loginIp, loginDate);
    }

    /**
     * 重置用户密码
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int resetPwd(SysUser user) {
        return userMapper.resetUserPwd(user.getUserId(), user.getPassword());
    }

    /**
     * 重置用户密码
     *
     * @param userId   用户ID
     * @param password 密码
     * @return 结果
     */
    @Override
    public int resetUserPwd(Long userId, String password) {
        return userMapper.resetUserPwd(userId, password);
    }


    /**
     * 批量删除用户信息
     *
     * @param userIds 需要删除的用户ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteUserByIds(Long[] userIds) {
        for (Long userId : userIds) {
            checkUserAllowed(new SysUser(userId));
            checkUserDataScope(userId);
        }
        // 删除用户与角色关联
        userRoleMapper.deleteUserRole(userIds);
        // 删除用户与岗位关联
        userPostMapper.deleteUserPost(userIds);
        return userMapper.deleteUserByIds(userIds);
    }

}
