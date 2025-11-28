package com.ruoyi.web.controller.monitor;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.framework.web.service.SysPasswordService;
import com.ruoyi.framework.webflux.model.P;
import com.ruoyi.framework.webflux.model.R;
import com.ruoyi.system.query.SysAccessLogQuery;
import com.ruoyi.system.service.SysAccessLogService;
import com.ruoyi.system.vo.SysAccessLogVO;
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

@Tag(name = "访问日志 接口")
@Validated
@RestController
@RequestMapping("/monitor/logininfor")
public class SysAccessLogController extends BaseController {

    @Resource
    private SysAccessLogService sysAccessLogService;

    @Resource
    private SysPasswordService passwordService;

    @Operation(summary = "根据条件分页查询访问日志")
    @PreAuthorize("hasAuthority('monitor:logininfor:list')")
    @GetMapping("/list")
    public Mono<P<List<SysAccessLogVO>>> list(@ParameterObject @Valid SysAccessLogQuery query) {
        return sysAccessLogService.selectAccessLogList(query)
                .map(P::ok);
    }

    @Operation(summary = "批量删除访问日志")
    @Parameter(name = "logIds", description = "日志ID（多个以,分隔）")
    @Log(title = "访问日志", businessType = BusinessType.DELETE)
    @PreAuthorize("hasAuthority('monitor:logininfor:remove')")
    @DeleteMapping("/{logIds}")
    public Mono<R<Void>> remove(@PathVariable List<Long> logIds) {
        return sysAccessLogService.deleteAccessLogByIds(logIds)
                .thenReturn(R.ok());
    }

    @Operation(summary = "清空访问日志")
    @Log(title = "访问日志", businessType = BusinessType.CLEAN)
    @PreAuthorize("hasAuthority('monitor:logininfor:remove')")
    @DeleteMapping("/clean")
    public Mono<R<Void>> clean() {
        return sysAccessLogService.clearAccessLog()
                .thenReturn(R.ok());
    }

    @PreAuthorize("hasAuthority('monitor:logininfor:unlock')")
    @Parameter(name = "userName", description = "用户账号")
    @Log(title = "账户解锁", businessType = BusinessType.OTHER)
    @GetMapping("/unlock/{userName}")
    public Mono<R<Void>> unlock(@PathVariable String userName) {
        return passwordService.clearLoginRecordCache(userName)
                .thenReturn(R.ok());
    }

}
