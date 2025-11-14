package com.ruoyi.system.entity;

import com.ruoyi.framework.r2dbc.BaseEntity;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

/**
 * 通告表
 *
 * @author bugout
 * @version 2025-11-14
 */
@Table("sys_notice")
@Data
public class SysNotice extends BaseEntity {

    /**
     * 通告ID
     */
    @Id
    private Long noticeId;
    /**
     * 通告标题
     */
    private String noticeTitle;
    /**
     * 通告类型（1通知 2公告）
     */
    private String noticeType;
    /**
     * 通告内容
     */
    private String noticeContent;
    /**
     * 通告状态（0正常 1关闭）
     */
    private String status;
    /**
     * 备注
     */
    private String remark;

}
