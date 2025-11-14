package com.ruoyi.system.vo;

import com.ruoyi.common.xss.Xss;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Schema(description = "SysNoticeVO")
@Data
public class SysNoticeVO implements Serializable {

    @Schema(description = "通告ID")
    private Long noticeId;

    @Schema(description = "通告标题")
    @Xss(message = "通告标题不能包含脚本字符")
    @NotBlank(message = "通告标题不能为空")
    @Size(max = 50, message = "通告标题不能超过50个字符")
    private String noticeTitle;

    @Schema(description = "通告类型（1通知 2公告）")
    private String noticeType;

    @Schema(description = "通告内容")
    private String noticeContent;

    @Schema(description = "通告状态（0正常 1关闭）")
    private String status;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "创建者")
    private String createBy;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

}
