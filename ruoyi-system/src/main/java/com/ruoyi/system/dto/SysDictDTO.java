package com.ruoyi.system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

@Schema(description = "SysDictDTO")
@Data
public class SysDictDTO implements Serializable {

    @Schema(description = "字典编码")
    private Long dictCode;

    @Schema(description = "字典排序")
    private Long dictSort;

    @Schema(description = "字典标签")
    @NotBlank(message = "字典标签不能为空")
    @Size(max = 100, message = "字典标签长度不能超过100个字符")
    private String dictLabel;

    @Schema(description = "字典键值")
    @NotBlank(message = "字典键值不能为空")
    @Size(max = 100, message = "字典键值长度不能超过100个字符")
    private String dictValue;

    @Schema(description = "字典类型")
    @NotBlank(message = "字典类型不能为空")
    @Size(max = 100, message = "字典类型长度不能超过100个字符")
    private String dictType;

    @Schema(description = "样式属性（其他样式扩展）")
    @Size(max = 100, message = "样式属性长度不能超过100个字符")
    private String cssClass;

    @Schema(description = "表格字典样式")
    private String listClass;

    @Schema(description = "是否默认（Y是 N否）")
    private String isDefault;

    @Schema(description = "状态（0正常 1停用）")
    private String status;

    @Schema(description = "备注")
    private String remark;

}
