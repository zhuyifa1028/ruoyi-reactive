package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.framework.webflux.model.P;
import com.ruoyi.framework.webflux.model.R;
import com.ruoyi.system.dto.SysConfigDTO;
import com.ruoyi.system.query.SysConfigQuery;
import com.ruoyi.system.service.SysConfigService;
import com.ruoyi.system.vo.SysConfigVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@Tag(name = "配置表 接口")
@Validated
@RestController
@RequestMapping("/system/config")
public class SysConfigController extends BaseController {

    @Resource
    private SysConfigService sysConfigService;

    @Operation(summary = "获取配置列表")
    @PreAuthorize("hasAuthority('system:config:list')")
    @GetMapping("/list")
    public Mono<P<List<SysConfigVO>>> list(@ParameterObject @Valid SysConfigQuery query) {
        return sysConfigService.selectConfigList(query)
                .map(P::ok);
    }

    @Operation(summary = "根据配置ID获取详细信息")
    @Parameter(name = "configId", description = "配置ID")
    @PreAuthorize("hasAuthority('system:config:query')")
    @GetMapping(value = "/{configId}")
    public Mono<R<SysConfigVO>> getInfo(@PathVariable Long configId) {
        return sysConfigService.selectConfigById(configId)
                .map(R::ok);
    }

    @Operation(summary = "根据配置键名查询配置值")
    @Parameter(name = "configKey", description = "配置键名")
    @GetMapping(value = "/configKey/{configKey}")
    public Mono<R<String>> getConfigKey(@PathVariable String configKey) {
        return sysConfigService.selectConfigByKey(configKey)
                .map(R::ok);
    }

    @Operation(summary = "新增配置")
    @Log(title = "配置管理", businessType = BusinessType.INSERT)
    @PreAuthorize("hasAuthority('system:config:add')")
    @PostMapping
    public Mono<R<Void>> add(@RequestBody @Valid SysConfigDTO dto) {
        return sysConfigService.insertConfig(dto)
                .thenReturn(R.ok());
    }

    @Operation(summary = "修改配置")
    @Log(title = "配置管理", businessType = BusinessType.UPDATE)
    @PreAuthorize("hasAuthority('system:config:edit')")
    @PutMapping
    public Mono<R<Void>> edit(@RequestBody @Valid SysConfigDTO dto) {
        return sysConfigService.updateConfig(dto)
                .thenReturn(R.ok());
    }

    @Operation(summary = "批量删除配置")
    @Parameter(name = "configIds", description = "配置ID（多个以,分隔）")
    @Log(title = "配置管理", businessType = BusinessType.DELETE)
    @PreAuthorize("hasAuthority('system:config:remove')")
    @DeleteMapping("/{configIds}")
    public Mono<R<Void>> remove(@PathVariable List<Long> configIds) {
        return sysConfigService.deleteConfigByIds(configIds)
                .thenReturn(R.ok());
    }

    @Operation(summary = "刷新配置缓存")
    @Log(title = "配置管理", businessType = BusinessType.CLEAN)
    @PreAuthorize("hasAuthority('system:config:remove')")
    @DeleteMapping("/refreshCache")
    public Mono<R<Void>> refreshCache() {
        return sysConfigService.resetConfigCache()
                .thenReturn(R.ok());
    }

}
