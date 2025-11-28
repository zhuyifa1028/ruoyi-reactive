package com.ruoyi.system.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Schema(description = "SysLoginLogVO")
@Data
public class SysAccessLogVO implements Serializable {

    @Schema(description = "日志ID")
    private Long logId;

    @Schema(description = "用户账号")
    private String userName;

    @Schema(description = "状态 0成功 1失败")
    private String status;

    @Schema(description = "IP地址")
    private String ipaddr;

    @Schema(description = "地点")
    private String location;

    @Schema(description = "浏览器类型")
    private String browser;

    @Schema(description = "操作系统")
    private String os;

    @Schema(description = "提示消息")
    private String msg;

    @Schema(description = "访问时间")
    private LocalDateTime accessTime;

}
