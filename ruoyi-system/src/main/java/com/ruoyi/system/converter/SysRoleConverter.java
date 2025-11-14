package com.ruoyi.system.converter;

import com.ruoyi.system.dto.SysRoleDTO;
import com.ruoyi.system.entity.SysRole;
import com.ruoyi.system.vo.SysRoleVO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SysRoleConverter {

    SysRole toSysRole(SysRoleDTO source);

    void copyProperties(SysRoleDTO source, @MappingTarget SysRole target);

    SysRoleVO toSysRoleVO(SysRole source);

}
