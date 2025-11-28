package com.ruoyi.system.query;

import com.ruoyi.framework.webflux.model.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Schema(description = "SysAccessLogQuery")
@Data
public class SysAccessLogQuery extends PageQuery {

    @Schema(description = "IP地址")
    private String ipaddr;

    @Schema(description = "用户账号")
    private String userName;

    @Schema(description = "状态 0成功 1失败")
    private String status;

    @Schema(description = "开始时间")
    private LocalDate startTime;

    @Schema(description = "结束时间")
    private LocalDate endTime;

}
