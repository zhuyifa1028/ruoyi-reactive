package com.ruoyi.framework.webflux.model;

import com.ruoyi.common.constant.HttpStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Schema(description = "统一响应结构")
@Data
public class R<T> implements Serializable {

    @Schema(description = "状态码")
    private Integer code;

    @Schema(description = "消息")
    private String message;

    @Schema(description = "数据")
    private T data;

    private static <T> R<T> newInstance(Integer code, String message, T data) {
        R<T> r = new R<>();
        r.setCode(code);
        r.setMessage(message);
        r.setData(data);
        return r;
    }

    public static R<Void> ok() {
        return ok(null);
    }

    public static <T> R<T> ok(T data) {
        return newInstance(HttpStatus.SUCCESS, "操作成功", data);
    }

    public static R<Void> fail(String message) {
        return fail(HttpStatus.ERROR, message);
    }

    public static R<Void> fail(Integer code, String message) {
        return newInstance(code, message, null);
    }

}
