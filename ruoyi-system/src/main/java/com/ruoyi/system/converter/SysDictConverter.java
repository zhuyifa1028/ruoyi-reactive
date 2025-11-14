package com.ruoyi.system.converter;

import com.ruoyi.system.dto.SysDictDTO;
import com.ruoyi.system.entity.SysDict;
import com.ruoyi.system.vo.SysDictVO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SysDictConverter {

    SysDict toSysDict(SysDictDTO source);

    void copyProperties(SysDictDTO source, @MappingTarget SysDict target);

    SysDictVO toSysDictVO(SysDict source);

}
