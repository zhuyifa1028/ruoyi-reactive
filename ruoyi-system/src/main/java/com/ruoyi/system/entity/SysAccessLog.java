package com.ruoyi.system.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 访问日志表
 *
 * @author bugout
 * @version 2025-11-28
 */
@Table("sys_access_log")
@Data
public class SysAccessLog implements Serializable {

    /**
     * 日志ID
     */
    @Id
    private Long logId;
    /**
     * 用户账号
     */
    private String userName;
    /**
     * 状态 0成功 1失败
     */
    private String status;
    /**
     * IP地址
     */
    private String ipaddr;
    /**
     * 地点
     */
    private String location;
    /**
     * 浏览器类型
     */
    private String browser;
    /**
     * 操作系统
     */
    private String os;
    /**
     * 提示消息
     */
    private String msg;
    /**
     * 访问时间
     */
    private LocalDateTime accessTime;

}
