package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.dto.SysNoticeDTO;
import com.ruoyi.system.query.SysNoticeQuery;
import com.ruoyi.system.service.SysNoticeService;
import com.ruoyi.system.vo.SysNoticeVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@Tag(name = "通告表 接口")
@RestController
@RequestMapping("/system/notice")
public class SysNoticeController extends BaseController {

    @Resource
    private SysNoticeService noticeService;

    @Operation(summary = "查询通告列表")
    @PreAuthorize("hasAuthority('system:notice:list')")
    @GetMapping("/list")
    public Mono<TableDataInfo> list(SysNoticeQuery notice) {
        return noticeService.selectNoticeList(notice)
                .flatMap(page -> {
                    TableDataInfo info = new TableDataInfo();
                    info.setCode(HttpStatus.SUCCESS);
                    info.setMsg("查询成功");
                    info.setRows(page.getContent());
                    info.setTotal(page.getTotalElements());
                    return Mono.just(info);
                });
    }

    @Operation(summary = "根据通告ID查询信息")
    @PreAuthorize("hasAuthority('system:notice:query')")
    @GetMapping(value = "/{noticeId}")
    public Mono<R<SysNoticeVO>> getInfo(@PathVariable Long noticeId) {
        return noticeService.selectNoticeById(noticeId)
                .map(R::ok);
    }

    @Operation(summary = "新增通告")
    @Log(title = "通告管理", businessType = BusinessType.INSERT)
    @PreAuthorize("hasAuthority('system:notice:add')")
    @PostMapping
    public Mono<R<Void>> add(@RequestBody @Validated SysNoticeDTO dto) {
        return noticeService.insertNotice(dto)
                .thenReturn(R.ok());
    }

    @Operation(summary = "修改通告")
    @Log(title = "通告管理", businessType = BusinessType.UPDATE)
    @PreAuthorize("hasAuthority('system:notice:edit')")
    @PutMapping
    public Mono<R<Void>> edit(@RequestBody @Validated SysNoticeDTO dto) {
        return noticeService.updateNotice(dto)
                .thenReturn(R.ok());
    }

    @Operation(summary = "批量删除通告")
    @Log(title = "通告管理", businessType = BusinessType.DELETE)
    @PreAuthorize("hasAuthority('system:notice:remove')")
    @DeleteMapping("/{noticeIds}")
    public Mono<R<Void>> remove(@PathVariable List<Long> noticeIds) {
        return noticeService.deleteNoticeByIds(noticeIds)
                .thenReturn(R.ok());
    }

}
