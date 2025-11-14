package com.ruoyi.system.query;

import com.ruoyi.common.core.page.PageDomain;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Schema(description = "SysConfigDTO")
@Data
public class SysRoleQuery extends PageDomain {

    @Schema(description = "角色名称")
    private String roleName;

    @Schema(description = "角色键名")
    private String roleKey;

    @Schema(description = "角色状态（0正常 1停用）")
    private String status;

    @Schema(description = "开始时间")
    private LocalDate startTime;

    @Schema(description = "结束时间")
    private LocalDate endTime;

}
