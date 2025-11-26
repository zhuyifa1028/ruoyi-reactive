package com.ruoyi.system.converter;

import com.ruoyi.system.dto.SysConfigDTO;
import com.ruoyi.system.entity.SysConfig;
import com.ruoyi.system.vo.SysConfigVO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SysConfigConverter {

    SysConfig toSysConfig(SysConfigDTO source);

    SysConfig toSysConfig(SysConfigDTO source, @MappingTarget SysConfig target);

    SysConfigVO toSysConfigVO(SysConfig source);

}
