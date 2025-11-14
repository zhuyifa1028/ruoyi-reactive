package com.ruoyi.system.converter;

import com.ruoyi.system.dto.SysMenuDTO;
import com.ruoyi.system.entity.SysMenu;
import com.ruoyi.system.vo.SysMenuVO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SysMenuConverter {

    SysMenu toSysMenu(SysMenuDTO source);

    void copyProperties(SysMenuDTO source, @MappingTarget SysMenu target);

    SysMenuVO toSysMenuVO(SysMenu source);

}
