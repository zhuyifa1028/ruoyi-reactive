package com.ruoyi.system.query;

import com.ruoyi.common.core.page.PageDomain;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Schema(description = "SysUserQuery")
@Data
public class SysUserQuery extends PageDomain {

    @Schema(description = "用户名称")
    private String userName;

    @Schema(description = "手机号码")
    private String phonenumber;

    @Schema(description = "用户状态（0正常 1停用）")
    private String status;

    @Schema(description = "开始时间")
    private LocalDate startTime;

    @Schema(description = "结束时间")
    private LocalDate endTime;

}
