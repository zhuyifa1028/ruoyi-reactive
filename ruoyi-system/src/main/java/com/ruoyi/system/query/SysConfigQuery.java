package com.ruoyi.system.query;

import com.ruoyi.framework.webflux.model.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Schema(description = "SysConfigDTO")
@Data
public class SysConfigQuery extends PageQuery {

    @Schema(description = "参数名称")
    private String configName;

    @Schema(description = "参数键名")
    private String configKey;

    @Schema(description = "系统内置（Y是 N否）")
    private String configType;

    @Schema(description = "开始时间")
    private LocalDate startTime;

    @Schema(description = "结束时间")
    private LocalDate endTime;

}
