package com.ruoyi.system.query;

import com.ruoyi.framework.webflux.model.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Schema(description = "SysOperateLogQuery")
@Data
public class SysOperateLogQuery extends PageQuery {

    @Schema(description = "操作地址")
    private String operIp;

    @Schema(description = "操作模块")
    private String title;

    @Schema(description = "业务类型（0其它 1新增 2修改 3删除 4授权 5导出 6导入 7强退 8生成代码 9清空数据）")
    private Integer businessType;

    @Schema(description = "操作状态（0正常 1异常）")
    private Integer status;

    @Schema(description = "操作人员")
    private String operName;

    @Schema(description = "开始时间")
    private LocalDate startTime;

    @Schema(description = "结束时间")
    private LocalDate endTime;

}
