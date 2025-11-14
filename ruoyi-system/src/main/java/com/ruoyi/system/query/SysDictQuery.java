package com.ruoyi.system.query;

import com.ruoyi.common.core.page.PageDomain;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Schema(description = "SysDictQuery")
@Data
public class SysDictQuery extends PageDomain {

    @Schema(description = "字典类型")
    private String dictType;

    @Schema(description = "字典标签")
    private String dictLabel;

    @Schema(description = "字典值")
    private String dictValue;

    @Schema(description = "状态（0正常 1停用）")
    private String status;

    @Schema(description = "开始时间")
    private LocalDate startTime;

    @Schema(description = "结束时间")
    private LocalDate endTime;

}
