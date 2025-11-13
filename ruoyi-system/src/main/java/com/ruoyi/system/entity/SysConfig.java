package com.ruoyi.system.entity;

import com.ruoyi.framework.r2dbc.BaseEntity;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

/**
 * 配置表
 *
 * @author bugout
 * @version 2025-11-11
 */
@Table("sys_config")
@Data
public class SysConfig extends BaseEntity {

    /**
     * 参数主键
     */
    @Id
    private Long configId;
    /**
     * 参数名称
     */
    private String configName;
    /**
     * 参数键名
     */
    private String configKey;
    /**
     * 参数键值
     */
    private String configValue;
    /**
     * 系统内置（Y是 N否）
     */
    private String configType;
    /**
     * 备注
     */
    private String remark;

}
