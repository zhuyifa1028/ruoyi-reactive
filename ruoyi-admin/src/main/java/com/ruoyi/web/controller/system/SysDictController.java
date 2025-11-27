package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.framework.webflux.model.P;
import com.ruoyi.framework.webflux.model.R;
import com.ruoyi.system.dto.SysDictDTO;
import com.ruoyi.system.query.SysDictQuery;
import com.ruoyi.system.service.SysDictService;
import com.ruoyi.system.vo.SysDictVO;
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

@Tag(name = "字典表 接口")
@Validated
@RestController
@RequestMapping("/system/dict/data")
public class SysDictController extends BaseController {

    @Resource
    private SysDictService sysDictService;

    @Operation(summary = "根据条件分页查询字典列表")
    @PreAuthorize("hasAuthority('system:dict:list')")
    @GetMapping("/list")
    public Mono<P<List<SysDictVO>>> list(@ParameterObject @Valid SysDictQuery query) {
        return sysDictService.selectDictList(query)
                .map(P::ok);
    }

    @Operation(summary = "根据字典类型查询字典列表")
    @Parameter(name = "dictType", description = "字典类型")
    @GetMapping(value = "/type/{dictType}")
    public Mono<R<List<SysDictVO>>> dictType(@PathVariable String dictType) {
        return sysDictService.selectDictListByType(dictType)
                .collectList()
                .map(R::ok);
    }

    @Operation(summary = "根据字典ID查询信息")
    @Parameter(name = "dictCode", description = "字典ID")
    @PreAuthorize("hasAuthority('system:dict:query')")
    @GetMapping(value = "/{dictCode}")
    public Mono<R<SysDictVO>> getInfo(@PathVariable Long dictCode) {
        return sysDictService.selectDictById(dictCode)
                .map(R::ok);
    }

    @Operation(summary = "新增字典")
    @Log(title = "字典管理", businessType = BusinessType.INSERT)
    @PreAuthorize("hasAuthority('system:dict:add')")
    @PostMapping
    public Mono<R<Void>> add(@RequestBody @Valid SysDictDTO dto) {
        return sysDictService.insertDict(dto)
                .thenReturn(R.ok());
    }

    @Operation(summary = "修改字典")
    @Log(title = "字典管理", businessType = BusinessType.UPDATE)
    @PreAuthorize("hasAuthority('system:dict:edit')")
    @PutMapping
    public Mono<R<Void>> edit(@RequestBody @Valid SysDictDTO dto) {
        return sysDictService.updateDict(dto)
                .thenReturn(R.ok());
    }

    @Operation(summary = "批量删除字典")
    @Parameter(name = "dictCodes", description = "字典ID（多个以,分隔）")
    @Log(title = "字典管理", businessType = BusinessType.DELETE)
    @PreAuthorize("hasAuthority('system:dict:remove')")
    @DeleteMapping("/{dictCodes}")
    public Mono<R<Void>> remove(@PathVariable List<Long> dictCodes) {
        return sysDictService.deleteDictByIds(dictCodes)
                .thenReturn(R.ok());
    }

    @Operation(summary = "刷新字典缓存")
    @PreAuthorize("hasAuthority('system:dict:remove')")
    @Log(title = "字典管理", businessType = BusinessType.CLEAN)
    @DeleteMapping("/refreshCache")
    public Mono<R<Void>> refreshCache() {
        return sysDictService.resetDictCache()
                .thenReturn(R.ok());
    }

}
