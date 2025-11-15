package com.ruoyi.system.converter;

import com.ruoyi.system.dto.SysUserDTO;
import com.ruoyi.system.entity.SysUser;
import com.ruoyi.system.vo.SysUserVO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SysUserConverter {

    SysUser toSysUser(SysUserDTO source);

    void copyProperties(SysUserDTO source, @MappingTarget SysUser target);

    SysUserVO toSysUserVO(SysUser source);

}
