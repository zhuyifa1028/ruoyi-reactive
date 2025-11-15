package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.dto.SysPostDTO;
import com.ruoyi.system.query.SysPostQuery;
import com.ruoyi.system.service.SysPostService;
import com.ruoyi.system.vo.SysPostVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@Tag(name = "岗位表 接口")
@RestController
@RequestMapping("/system/post")
public class SysPostController extends BaseController {

    @Resource
    private SysPostService sysPostService;

    @Operation(summary = "查询岗位列表")
    @PreAuthorize("@ss.hasPermi('system:post:list')")
    @GetMapping("/list")
    public Mono<TableDataInfo> list(SysPostQuery query) {
        return sysPostService.selectPostList(query)
                .flatMap(page -> {
                    TableDataInfo info = new TableDataInfo();
                    info.setCode(HttpStatus.SUCCESS);
                    info.setMsg("查询成功");
                    info.setRows(page.getContent());
                    info.setTotal(page.getTotalElements());
                    return Mono.just(info);
                });
    }

    @Operation(summary = "通过岗位ID查询岗位信息")
    @PreAuthorize("@ss.hasPermi('system:post:query')")
    @GetMapping(value = "/{postId}")
    public Mono<R<SysPostVO>> getInfo(@PathVariable Long postId) {
        return sysPostService.selectPostById(postId)
                .map(R::ok);
    }

    @Operation(summary = "新增岗位")
    @Log(title = "岗位管理", businessType = BusinessType.INSERT)
    @PreAuthorize("@ss.hasPermi('system:post:add')")
    @PostMapping
    public Mono<R<Void>> add(@RequestBody @Validated SysPostDTO dto) {
        return sysPostService.insertPost(dto)
                .thenReturn(R.ok());
    }

    @Operation(summary = "修改岗位")
    @Log(title = "岗位管理", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('system:post:edit')")
    @PutMapping
    public Mono<R<Void>> edit(@RequestBody @Validated SysPostDTO dto) {
        return sysPostService.updatePost(dto)
                .thenReturn(R.ok());
    }

    @Operation(summary = "批量删除岗位")
    @Log(title = "岗位管理", businessType = BusinessType.DELETE)
    @PreAuthorize("@ss.hasPermi('system:post:remove')")
    @DeleteMapping("/{postIds}")
    public Mono<R<Void>> remove(@PathVariable List<Long> postIds) {
        return sysPostService.deletePostByIds(postIds)
                .thenReturn(R.ok());
    }

    @Operation(summary = "查询岗位选择框列表")
    @GetMapping("/optionselect")
    public Mono<R<List<SysPostVO>>> optionselect() {
        return sysPostService.selectPostAll()
                .collectList()
                .map(R::ok);
    }

}
