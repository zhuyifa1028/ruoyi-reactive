package com.ruoyi.framework.webflux.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;

@Schema(description = "分页查询")
@Data
public class PageQuery implements Serializable {

    @Schema(description = "当前页数", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "当前页数不能为空")
    @Min(value = 1, message = "当前页数不能小于1")
    private Integer pageNumber;

    @Schema(description = "每页显示记录数", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "每页显示记录数不能为空")
    @Min(value = 1, message = "每页显示记录数不能小于1")
    private Integer pageSize;

    public Pageable pageable() {
        return PageRequest.of(pageNumber - 1, pageSize);
    }

    public int limit() {
        return pageable().getPageSize();
    }

    public long offset() {
        return pageable().getOffset();
    }

}
