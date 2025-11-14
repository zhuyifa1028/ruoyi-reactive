package com.ruoyi.system.entity;

import com.ruoyi.framework.r2dbc.BaseEntity;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

/**
 * 岗位表
 *
 * @author bugout
 * @version 2025-11-14
 */
@Table("sys_post")
@Data
public class SysPost extends BaseEntity {

    /**
     * 岗位序号
     */
    @Id
    private Long postId;
    /**
     * 岗位编码
     */
    private String postCode;
    /**
     * 岗位名称
     */
    private String postName;
    /**
     * 岗位排序
     */
    private Integer postSort;
    /**
     * 状态（0正常 1停用）
     */
    private String status;
    /**
     * 备注
     */
    private String remark;

}
