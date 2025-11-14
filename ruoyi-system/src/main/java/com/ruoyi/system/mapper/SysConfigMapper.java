package com.ruoyi.system.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 参数配置 数据层
 *
 * @author ruoyi
 */
public interface SysConfigMapper {

    @Select("select * from sys_config where config_key = #{configKey} limit 1")
    String selectConfigByKey(@Param("configKey") String configKey);

}
