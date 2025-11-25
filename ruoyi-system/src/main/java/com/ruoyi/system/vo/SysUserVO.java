package com.ruoyi.system.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "SysUserDTO")
@Data
public class SysUserVO implements Serializable {

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "部门ID")
    private Long deptId;

    @Schema(description = "部门名称")
    private String deptName;

    @Schema(description = "用户账号")
    private String userName;

    @Schema(description = "用户昵称")
    private String nickName;

    @Schema(description = "用户邮箱")
    private String email;

    @Schema(description = "手机号码")
    private String phonenumber;

    @Schema(description = "用户性别")
    private String sex;

    @Schema(description = "用户头像")
    private String avatar;

    @Schema(description = "账号状态（0正常 1停用）")
    private String status;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "角色组")
    private List<Long> roleIds;

    @Schema(description = "岗位组")
    private List<Long> postIds;

}
