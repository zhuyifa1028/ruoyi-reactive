package com.ruoyi.web.controller.system;

import com.github.pagehelper.PageHelper;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.sql.SqlUtil;
import com.ruoyi.system.domain.SysConfig;
import com.ruoyi.system.dto.SysConfigDTO;
import com.ruoyi.system.service.SysConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@Tag(name = "配置表 接口")
@RestController
@RequestMapping("/system/config")
public class SysConfigController extends BaseController {

    @Resource
    private SysConfigService sysConfigService;

    @Operation(summary = "获取配置列表")
    @PreAuthorize("@ss.hasPermi('system:config:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysConfig config, PageDomain pageDomain) {
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
        Boolean reasonable = pageDomain.getReasonable();
        PageHelper.startPage(pageNum, pageSize, orderBy).setReasonable(reasonable);

        List<SysConfig> list = sysConfigService.selectConfigList(config);
        return getDataTable(list);
    }

    @Operation(summary = "根据配置编号获取详细信息")
    @PreAuthorize("@ss.hasPermi('system:config:query')")
    @GetMapping(value = "/{configId}")
    public Mono<AjaxResult> getInfo(@PathVariable Long configId) {
        return sysConfigService.selectConfigById(configId)
                .map(AjaxResult::success);
    }

    @Operation(summary = "根据配置键名查询配置值")
    @GetMapping(value = "/configKey/{configKey}")
    public Mono<AjaxResult> getConfigKey(@PathVariable String configKey) {
        return sysConfigService.selectConfigByKey(configKey)
                .map(AjaxResult::success);
    }

    @Operation(summary = "新增配置")
    @Log(title = "配置管理", businessType = BusinessType.INSERT)
    @PreAuthorize("@ss.hasPermi('system:config:add')")
    @PostMapping
    public Mono<AjaxResult> add(@Validated @RequestBody SysConfigDTO dto) {
        return sysConfigService.insertConfig(dto)
                .thenReturn(AjaxResult.success());
    }

    @Operation(summary = "修改配置")
    @Log(title = "配置管理", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('system:config:edit')")
    @PutMapping
    public Mono<AjaxResult> edit(@Validated @RequestBody SysConfigDTO dto) {
        return sysConfigService.updateConfig(dto)
                .thenReturn(AjaxResult.success());
    }

    @Operation(summary = "批量删除配置")
    @Log(title = "配置管理", businessType = BusinessType.DELETE)
    @PreAuthorize("@ss.hasPermi('system:config:remove')")
    @DeleteMapping("/{configIds}")
    public Mono<AjaxResult> remove(@PathVariable List<Long> configIds) {
        return sysConfigService.deleteConfigByIds(configIds)
                .thenReturn(AjaxResult.success());
    }

    @Operation(summary = "刷新配置缓存")
    @Log(title = "配置管理", businessType = BusinessType.CLEAN)
    @PreAuthorize("@ss.hasPermi('system:config:remove')")
    @DeleteMapping("/refreshCache")
    public Mono<AjaxResult> refreshCache() {
        return sysConfigService.resetConfigCache()
                .thenReturn(AjaxResult.success());
    }

}
