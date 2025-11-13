package com.ruoyi.framework.r2dbc;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Entity基类
 *
 * @author bugout
 * @version 2025-11-11
 */
@Data
public class BaseEntity implements Serializable {

    /**
     * 创建者
     */
    @CreatedBy
    private String createBy;
    /**
     * 创建时间
     */
    @CreatedDate
    private LocalDateTime createTime;
    /**
     * 更新者
     */
    @LastModifiedBy
    private String updateBy;
    /**
     * 更新时间
     */
    @LastModifiedDate
    private LocalDateTime updateTime;

}
