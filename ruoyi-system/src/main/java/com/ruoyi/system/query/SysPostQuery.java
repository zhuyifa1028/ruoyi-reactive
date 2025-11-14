package com.ruoyi.system.query;

import com.ruoyi.common.core.page.PageDomain;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "SysPostQuery")
@Data
public class SysPostQuery extends PageDomain {

    @Schema(description = "岗位编码")
    private String postCode;

    @Schema(description = "岗位名称")
    private String postName;

    @Schema(description = "状态（0正常 1停用）")
    private String status;

}
