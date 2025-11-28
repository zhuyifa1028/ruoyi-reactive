package com.ruoyi.system.converter;

import com.ruoyi.system.entity.SysAccessLog;
import com.ruoyi.system.vo.SysAccessLogVO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SysAccessLogConverter {

    SysAccessLogVO toSysAccessLog(SysAccessLog source);

}
