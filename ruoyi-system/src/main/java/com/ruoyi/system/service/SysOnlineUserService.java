package com.ruoyi.system.service;

import com.ruoyi.system.query.SysOnlineUserQuery;
import com.ruoyi.system.vo.SysOnlineUserVO;
import org.springframework.data.domain.Page;
import reactor.core.publisher.Mono;

/**
 * 在线用户 业务层
 *
 * @author bugout
 * @version 2025-11-28
 */
public interface SysOnlineUserService {

    /**
     * 根据条件分页查询在线用户
     */
    Mono<Page<SysOnlineUserVO>> selectOnlineUserList(SysOnlineUserQuery query);

    /**
     * 强退用户
     */
    Mono<Void> deleteOnlineUserById(String tokenId);

}
