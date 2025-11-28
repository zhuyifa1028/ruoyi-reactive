package com.ruoyi.system.service.impl;

import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.framework.redis.ReactiveRedisUtils;
import com.ruoyi.framework.security.LoginUser;
import com.ruoyi.system.query.SysOnlineUserQuery;
import com.ruoyi.system.service.SysOnlineUserService;
import com.ruoyi.system.vo.SysOnlineUserVO;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 在线用户 业务实现
 *
 * @author bugout
 * @version 2025-11-28
 */
@Service
public class SysOnlineUserServiceImpl implements SysOnlineUserService {

    @Resource
    private ReactiveRedisUtils<LoginUser> reactiveRedisUtils;

    /**
     * 根据条件分页查询在线用户
     */
    @Override
    public Mono<Page<SysOnlineUserVO>> selectOnlineUserList(SysOnlineUserQuery query) {
        return reactiveRedisUtils.scan(CacheConstants.LOGIN_TOKEN_KEY + "*")
                .flatMap(key -> reactiveRedisUtils.getCacheObject(key)
                        .filter(user -> Objects.nonNull(user) && Objects.nonNull(user.getUser()))
                        .filter(user -> {
                            // 条件匹配（IP模糊匹配 + 用户名模糊匹配）
                            boolean ipaddrMatch = StringUtils.isBlank(query.getIpaddr()) || StringUtils.contains(user.getIpaddr(), query.getIpaddr());
                            boolean userNameMatch = StringUtils.isBlank(query.getUserName()) || StringUtils.contains(user.getUsername(), query.getUserName());
                            return ipaddrMatch && userNameMatch;
                        })
                        .map(this::convertLoginUser)
                )
                .collectList()
                .map(voList -> {
                    long total = voList.size();

                    // 排序，分页截取
                    List<SysOnlineUserVO> content = voList.stream()
                            .sorted(Comparator.comparing(SysOnlineUserVO::getLoginTime).reversed())
                            .skip(query.offset())
                            .limit(query.limit())
                            .collect(Collectors.toList());

                    return new PageImpl<>(content, query.pageable(), total);
                });
    }

    /**
     * 设置在线用户信息
     */
    private SysOnlineUserVO convertLoginUser(LoginUser user) {
        SysOnlineUserVO vo = new SysOnlineUserVO();
        vo.setTokenId(user.getToken());
        vo.setUserName(user.getUsername());
        vo.setIpaddr(user.getIpaddr());
        vo.setLoginLocation(user.getLoginLocation());
        vo.setBrowser(user.getBrowser());
        vo.setOs(user.getOs());
        vo.setLoginTime(user.getLoginTime());
//        if (StringUtils.isNotNull(user.getUser().getDept())) {
//            sysUserOnline.setDeptName(user.getUser().getDept().getDeptName());
//        }
        return vo;
    }

    /**
     * 强退用户
     */
    @Override
    public Mono<Void> deleteOnlineUserById(String tokenId) {
        return reactiveRedisUtils.deleteObject(CacheConstants.LOGIN_TOKEN_KEY + tokenId)
                .then();
    }

}
