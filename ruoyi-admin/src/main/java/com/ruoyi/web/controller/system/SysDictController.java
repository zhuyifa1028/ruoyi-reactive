package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.dto.SysDictDTO;
import com.ruoyi.system.query.SysDictQuery;
import com.ruoyi.system.service.SysDictService;
import com.ruoyi.system.vo.SysDictVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@Tag(name = "字典表 接口")
@RestController
@RequestMapping("/system/dict/data")
public class SysDictController extends BaseController {

    @Resource
    private SysDictService sysDictService;

    @Operation(summary = "根据条件分页查询字典列表")
    @PreAuthorize("@ss.hasPermi('system:dict:list')")
    @GetMapping("/list")
    public Mono<TableDataInfo> list(SysDictQuery query) {
        return sysDictService.selectDictList(query)
                .flatMap(page -> {
                    TableDataInfo info = new TableDataInfo();
                    info.setCode(HttpStatus.SUCCESS);
                    info.setMsg("查询成功");
                    info.setRows(page.getContent());
                    info.setTotal(page.getTotalElements());
                    return Mono.just(info);
                });
    }

    @Operation(summary = "根据字典类型查询字典列表")
    @GetMapping(value = "/type/{dictType}")
    public Mono<R<List<SysDictVO>>> dictType(@PathVariable String dictType) {
        return sysDictService.selectDictListByType(dictType)
                .collectList()
                .map(R::ok);
    }

    @Operation(summary = "根据字典ID查询信息")
    @PreAuthorize("@ss.hasPermi('system:dict:query')")
    @GetMapping(value = "/{dictCode}")
    public Mono<R<SysDictVO>> getInfo(@PathVariable Long dictCode) {
        return sysDictService.selectDictById(dictCode)
                .map(R::ok);
    }

    @Operation(summary = "新增字典")
    @Log(title = "字典管理", businessType = BusinessType.INSERT)
    @PreAuthorize("@ss.hasPermi('system:dict:add')")
    @PostMapping
    public Mono<R<Void>> add(@RequestBody @Validated SysDictDTO dto) {
        return sysDictService.insertDict(dto)
                .thenReturn(R.ok());
    }

    @Operation(summary = "修改字典")
    @Log(title = "字典管理", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('system:dict:edit')")
    @PutMapping
    public Mono<R<Void>> edit(@RequestBody @Validated SysDictDTO dto) {
        return sysDictService.updateDict(dto)
                .thenReturn(R.ok());
    }

    @Operation(summary = "批量删除字典")
    @Log(title = "字典管理", businessType = BusinessType.DELETE)
    @PreAuthorize("@ss.hasPermi('system:dict:remove')")
    @DeleteMapping("/{dictCodes}")
    public Mono<R<Void>> remove(@PathVariable List<Long> dictCodes) {
        return sysDictService.deleteDictByIds(dictCodes)
                .thenReturn(R.ok());
    }

    @Operation(summary = "刷新字典缓存")
    @PreAuthorize("@ss.hasPermi('system:dict:remove')")
    @Log(title = "字典管理", businessType = BusinessType.CLEAN)
    @DeleteMapping("/refreshCache")
    public Mono<R<Void>> refreshCache() {
        return sysDictService.resetDictCache()
                .thenReturn(R.ok());
    }

}
