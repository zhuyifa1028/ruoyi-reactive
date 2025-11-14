package com.ruoyi.system.converter;

import com.ruoyi.system.dto.SysNoticeDTO;
import com.ruoyi.system.entity.SysNotice;
import com.ruoyi.system.vo.SysNoticeVO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SysNoticeConverter {

    SysNotice toSysNotice(SysNoticeDTO source);

    void copyProperties(SysNoticeDTO source, @MappingTarget SysNotice target);

    SysNoticeVO toSysNoticeVO(SysNotice source);

}
