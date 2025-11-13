package com.ruoyi.system.converter;

import com.ruoyi.system.dto.SysDeptDTO;
import com.ruoyi.system.entity.SysDept;
import com.ruoyi.system.vo.SysDeptVO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SysDeptConverter {

    SysDept toSysDept(SysDeptDTO source);

    void copyProperties(SysDeptDTO source, @MappingTarget SysDept target);

    SysDeptVO toSysDeptVO(SysDept source);

}
