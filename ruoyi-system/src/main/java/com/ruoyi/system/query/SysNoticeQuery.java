package com.ruoyi.system.query;

import com.ruoyi.common.core.page.PageDomain;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "SysNoticeQuery")
@Data
public class SysNoticeQuery extends PageDomain {

    @Schema(description = "通告标题")
    private String noticeTitle;

    @Schema(description = "通告类型（1通知 2公告）")
    private String noticeType;

    @Schema(description = "操作人")
    private String createBy;

}
