package com.ruoyi.framework.webflux.model;

import com.ruoyi.common.constant.HttpStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Schema(description = "统一分页结构")
@Data
public class P<T> extends R<T> {

    @Schema(description = "总记录数")
    private Long total;

    public static <T> P<List<T>> ok(Page<T> page) {
        P<List<T>> r = new P<>();
        r.setCode(HttpStatus.SUCCESS);
        r.setMessage("操作成功");
        r.setData(page.getContent());
        r.setTotal(page.getTotalElements());
        return r;
    }

}
