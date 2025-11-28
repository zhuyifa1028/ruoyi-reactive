package com.ruoyi.web.controller.monitor;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.framework.webflux.model.P;
import com.ruoyi.framework.webflux.model.R;
import com.ruoyi.system.query.SysOperateLogQuery;
import com.ruoyi.system.service.SysOperateLogService;
import com.ruoyi.system.vo.SysOperateLogVO;
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

@Tag(name = "操作日志 接口")
@Validated
@RestController
@RequestMapping("/monitor/operlog")
public class SysOperateLogController extends BaseController {

    @Resource
    private SysOperateLogService operLogService;

    @Operation(summary = "根据条件分页查询操作日志")
    @PreAuthorize("hasAuthority('monitor:operlog:list')")
    @GetMapping("/list")
    public Mono<P<List<SysOperateLogVO>>> list(@ParameterObject @Valid SysOperateLogQuery query) {
        return operLogService.selectOperateLogList(query)
                .map(P::ok);
    }

    @Operation(summary = "批量删除操作日志")
    @Parameter(name = "operIds", description = "日志ID（多个以,分隔）")
    @Log(title = "操作日志", businessType = BusinessType.DELETE)
    @PreAuthorize("hasAuthority('monitor:operlog:remove')")
    @DeleteMapping("/{operIds}")
    public Mono<R<Void>> remove(@PathVariable List<Long> operIds) {
        return operLogService.deleteOperateLogByIds(operIds)
                .thenReturn(R.ok());
    }

    @Operation(summary = "清空操作日志")
    @Log(title = "操作日志", businessType = BusinessType.CLEAN)
    @PreAuthorize("hasAuthority('monitor:operlog:remove')")
    @DeleteMapping("/clean")
    public Mono<R<Void>> clean() {
        return operLogService.clearOperateLog()
                .thenReturn(R.ok());
    }

}
