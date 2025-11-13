package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.dto.SysDeptDTO;
import com.ruoyi.system.query.SysDeptQuery;
import com.ruoyi.system.service.SysDeptService;
import com.ruoyi.system.vo.SysDeptVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@Tag(name = "部门表 接口")
@RestController
@RequestMapping("/system/dept")
public class SysDeptController extends BaseController {

    @Resource
    private SysDeptService deptService;

    @Operation(summary = "查询部门列表")
    @PreAuthorize("@ss.hasPermi('system:dept:list')")
    @GetMapping("/list")
    public Mono<R<List<SysDeptVO>>> list(SysDeptQuery query) {
        return deptService.selectDeptList(query)
                .collectList()
                .map(R::ok);
    }

    @Operation(summary = "查询部门列表（排除指定节点）")
    @PreAuthorize("@ss.hasPermi('system:dept:list')")
    @GetMapping("/list/exclude/{deptId}")
    public Mono<R<List<SysDeptVO>>> excludeChild(@PathVariable(value = "deptId", required = false) Long deptId) {
        return deptService.selectDeptListExclude(deptId)
                .collectList()
                .map(R::ok);
    }

    @Operation(summary = "根据部门ID查询信息")
    @PreAuthorize("@ss.hasPermi('system:dept:query')")
    @GetMapping(value = "/{deptId}")
    public Mono<R<SysDeptVO>> getInfo(@PathVariable Long deptId) {
        return deptService.selectDeptById(deptId)
                .map(R::ok);
    }

    @Operation(summary = "新增部门")
    @Log(title = "部门管理", businessType = BusinessType.INSERT)
    @PreAuthorize("@ss.hasPermi('system:dept:add')")
    @PostMapping
    public Mono<R<Void>> add(@RequestBody @Validated SysDeptDTO dto) {
        return deptService.insertDept(dto)
                .thenReturn(R.ok());
    }

    @Operation(summary = "修改部门")
    @Log(title = "部门管理", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('system:dept:edit')")
    @PutMapping
    public Mono<R<Void>> edit(@RequestBody @Validated SysDeptDTO dto) {
        return deptService.updateDept(dto)
                .thenReturn(R.ok());
    }

    @Operation(summary = "删除部门")
    @Log(title = "部门管理", businessType = BusinessType.DELETE)
    @PreAuthorize("@ss.hasPermi('system:dept:remove')")
    @DeleteMapping("/{deptId}")
    public Mono<R<Void>> remove(@PathVariable Long deptId) {
        return deptService.deleteDeptById(deptId)
                .thenReturn(R.ok());
    }

}
