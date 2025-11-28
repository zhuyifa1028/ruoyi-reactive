package com.ruoyi.web.controller.monitor;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.framework.webflux.model.P;
import com.ruoyi.framework.webflux.model.R;
import com.ruoyi.system.query.SysOnlineUserQuery;
import com.ruoyi.system.service.SysOnlineUserService;
import com.ruoyi.system.vo.SysOnlineUserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@Tag(name = "在线用户 接口")
@Validated
@RestController
@RequestMapping("/monitor/online")
public class SysOnlineUserController extends BaseController {

    @Resource
    private SysOnlineUserService sysOnlineUserService;

    @Operation(summary = "根据条件分页查询在线用户")
    @PreAuthorize("hasAuthority('monitor:online:list')")
    @GetMapping("/list")
    public Mono<P<List<SysOnlineUserVO>>> list(@ParameterObject @Valid SysOnlineUserQuery query) {
        return sysOnlineUserService.selectOnlineUserList(query)
                .map(P::ok);
    }

    @Operation(summary = "强退用户")
    @Log(title = "在线用户", businessType = BusinessType.FORCE)
    @PreAuthorize("hasAuthority('monitor:online:forceLogout')")
    @DeleteMapping("/{tokenId}")
    public Mono<R<Void>> forceLogout(@PathVariable String tokenId) {
        return sysOnlineUserService.deleteOnlineUserById(tokenId)
                .thenReturn(R.ok());
    }

}
