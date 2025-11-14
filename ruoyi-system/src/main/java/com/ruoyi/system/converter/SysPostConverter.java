package com.ruoyi.system.converter;

import com.ruoyi.system.dto.SysPostDTO;
import com.ruoyi.system.entity.SysPost;
import com.ruoyi.system.vo.SysPostVO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SysPostConverter {

    SysPost toSysPost(SysPostDTO source);

    void copyProperties(SysPostDTO source, @MappingTarget SysPost target);

    SysPostVO toSysPostVO(SysPost source);

}
