package com.ruoyi.system.repository.querydsl;

import com.ruoyi.system.entity.SysMenu;
import com.ruoyi.system.query.SysMenuQuery;
import reactor.core.publisher.Flux;

public interface SysMenuQuerydslRepository {

    Flux<SysMenu> selectMenuList(SysMenuQuery query);

    Flux<SysMenu> selectMenuListByUserId(Long userId, SysMenuQuery query);

}
