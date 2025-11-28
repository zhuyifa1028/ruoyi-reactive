package com.ruoyi.system.query;

import com.ruoyi.framework.webflux.model.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "SysOnlineUserQuery")
@Data
public class SysOnlineUserQuery extends PageQuery {

    @Schema(description = "IP地址")
    private String ipaddr;

    @Schema(description = "用户账号")
    private String userName;

}
