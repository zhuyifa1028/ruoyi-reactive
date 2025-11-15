package com.ruoyi.system.entity;

import com.ruoyi.framework.r2dbc.BaseEntity;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * 用户表
 *
 * @author bugout
 * @version 2025-11-14
 */
@Table("sys_user")
@Data
public class SysUser extends BaseEntity {

    /**
     * 用户ID
     */
    @Id
    private Long userId;
    /**
     * 部门ID
     */
    private Long deptId;
    /**
     * 用户账号
     */
    private String userName;
    /**
     * 用户昵称
     */
    private String nickName;
    /**
     * 用户邮箱
     */
    private String email;
    /**
     * 手机号码
     */
    private String phonenumber;
    /**
     * 用户性别
     */
    private String sex;
    /**
     * 用户头像
     */
    private String avatar;
    /**
     * 密码
     */
    private String password;
    /**
     * 账号状态（0正常 1停用）
     */
    private String status;
    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;
    /**
     * 最后登录IP
     */
    private String loginIp;
    /**
     * 最后登录时间
     */
    private LocalDateTime loginDate;
    /**
     * 密码最后更新时间
     */
    private LocalDateTime pwdUpdateDate;
    /**
     * 备注
     */
    private String remark;

    public static boolean isAdmin(Long userId) {
        return userId != null && 1L == userId;
    }

    public static boolean isNotAdmin(Long userId) {
        return !isAdmin(userId);
    }

}
