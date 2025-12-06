package com.github.yingzhong.framework.security.config;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.httpauth.basic.SaHttpBasicUtil;
import cn.dev33.satoken.reactor.context.SaReactorSyncHolder;
import cn.dev33.satoken.reactor.filter.SaReactorFilter;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.github.yingzhong.framework.security.config.properties.SecurityProperties;
import com.github.yingzhong.framework.security.handler.AllUrlHandler;
import com.github.yingzhong.framework.security.utils.LoginHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.dromara.common.core.utils.SpringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;

/**
 * 权限安全配置
 *
 * @author Lion Li
 */

@Slf4j
@AutoConfiguration
@EnableConfigurationProperties(SecurityProperties.class)
@RequiredArgsConstructor
public class SecurityConfig {

    private final SecurityProperties securityProperties;

    @Value("${sse.path}")
    private String ssePath;

    /**
     * 注册sa-token的拦截器
     */
    @Bean
    public SaReactorFilter saReactorFilter() {
        AllUrlHandler allUrlHandler = SpringUtils.getBean(AllUrlHandler.class);

        // 注册路由拦截器，自定义验证规则
        return new SaReactorFilter()
            // 获取所有的
            .setExcludeList(allUrlHandler.getUrls())
            .setExcludeList(securityProperties.getExcludes())
            .addExclude(ssePath)
            // 对未排除的路径进行检查
            .setBeforeAuth(r -> {
                ServerWebExchange exchange = SaReactorSyncHolder.getExchange();
                ServerHttpRequest request = exchange.getRequest();
                ServerHttpResponse response = exchange.getResponse();
                response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                // 检查是否登录 是否有token
                StpUtil.checkLogin();

                // 检查 header 与 param 里的 clientid 与 token 里的是否一致
                String headerCid = request.getHeaders().getFirst(LoginHelper.CLIENT_KEY);
                String paramCid = request.getQueryParams().getFirst(LoginHelper.CLIENT_KEY);
                String clientId = StpUtil.getExtra(LoginHelper.CLIENT_KEY).toString();
                if (!StringUtils.equalsAny(clientId, headerCid, paramCid)) {
                    // token 无效
                    throw NotLoginException.newInstance(StpUtil.getLoginType(), "-100", "客户端ID与Token不匹配", StpUtil.getTokenValue());
                }

                // 有效率影响 用于临时测试
                if (log.isDebugEnabled()) {
                    log.info("剩余有效时间: {}", StpUtil.getTokenTimeout());
                    log.info("临时有效时间: {}", StpUtil.getTokenActiveTimeout());
                }
            });
    }

    /**
     * 对 actuator 健康检查接口 做账号密码鉴权
     */
    @Bean
    public SaReactorFilter actuatorSaReactorFilter() {
        String username = SpringUtils.getProperty("spring.boot.admin.client.username");
        String password = SpringUtils.getProperty("spring.boot.admin.client.password");
        return new SaReactorFilter()
            .addInclude("/actuator", "/actuator/**")
            .setAuth(obj -> SaHttpBasicUtil.check(username + ":" + password))
            .setError(e -> SaResult.notLogin().setMsg(e.getMessage()));
    }

}
