package com.ruoyi.framework.security.handle;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.manager.AsyncManager;
import com.ruoyi.framework.manager.factory.AsyncFactory;
import com.ruoyi.framework.web.service.TokenService;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;
import reactor.core.publisher.Mono;

/**
 * 自定义退出处理类 返回成功
 *
 * @author ruoyi
 */
@Configuration
public class LogoutSuccessHandlerImpl implements ServerLogoutSuccessHandler {

    @Resource
    private AsyncFactory AsyncFactory;

    @Resource
    private ObjectMapper objectMapper;

    @Resource
    private TokenService tokenService;

    /**
     * 退出处理
     */
    @Override
    public Mono<Void> onLogoutSuccess(WebFilterExchange exchange, Authentication authentication) {
        LoginUser loginUser = tokenService.getLoginUser(exchange.getExchange());
        if (StringUtils.isNotNull(loginUser)) {
            String userName = loginUser.getUsername();
            // 删除用户缓存记录
            tokenService.delLoginUser(loginUser.getToken());
            // 记录用户退出日志
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(exchange.getExchange(), userName, Constants.LOGOUT, MessageUtils.message("user.logout.success")));
        }

        try {
            byte[] bytes = objectMapper.writeValueAsBytes(AjaxResult.success(MessageUtils.message("user.logout.success")));

            ServerHttpResponse response = exchange.getExchange().getResponse();
            response.setStatusCode(org.springframework.http.HttpStatus.OK);
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

            return response.writeWith(Mono.just(response.bufferFactory().wrap(bytes)));
        } catch (JsonProcessingException e) {
            return Mono.error(e);
        }
    }
}
