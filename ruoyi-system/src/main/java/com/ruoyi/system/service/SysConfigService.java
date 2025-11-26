package com.ruoyi.system.service;

import com.ruoyi.system.dto.SysConfigDTO;
import com.ruoyi.system.query.SysConfigQuery;
import com.ruoyi.system.vo.SysConfigVO;
import org.springframework.data.domain.Page;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 配置表 业务层
 *
 * @author bugout
 * @version 2025-11-11
 */
public interface SysConfigService {

    /**
     * 获取配置列表
     */
    Mono<Page<SysConfigVO>> selectConfigList(SysConfigQuery query);

    /**
     * 根据配置编号获取详细信息
     */
    Mono<SysConfigVO> selectConfigById(Long configId);

    /**
     * 根据配置键名查询配置值
     */
    Mono<String> selectConfigByKey(String configKey);

    /**
     * 获取验证码开关
     */
    Mono<Boolean> selectCaptchaEnabled();

    /**
     * 新增配置
     */
    Mono<Boolean> insertConfig(SysConfigDTO dto);

    /**
     * 修改配置
     */
    Mono<Void> updateConfig(SysConfigDTO dto);

    /**
     * 批量删除配置
     */
    Mono<Void> deleteConfigByIds(List<Long> configIds);

    /**
     * 刷新配置缓存
     */
    Mono<Void> resetConfigCache();

}
