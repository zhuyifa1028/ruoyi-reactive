package com.ruoyi.system.service.impl;

import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.model.LoginBody;
import com.ruoyi.common.exception.user.*;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.ip.IpUtils;
import com.ruoyi.framework.manager.factory.AsyncFactory;
import com.ruoyi.framework.redis.ReactiveRedisUtils;
import com.ruoyi.framework.security.LoginUser;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.system.repository.SysUserRepository;
import com.ruoyi.system.service.SysConfigService;
import jakarta.annotation.Resource;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

/**
 * 登录校验方法
 *
 * @author ruoyi
 */
@Component
public class SysLoginService {

    @Resource
    private AsyncFactory asyncFactory;

    @Resource
    private TokenService tokenService;

    @Resource
    private ReactiveAuthenticationManager authenticationManager;

    @Resource
    private ReactiveRedisUtils<String> reactiveRedisUtils;

    @Resource
    private SysUserRepository sysUserRepository;

    @Resource
    private SysConfigService configService;

    /**
     * 登录验证
     */
    public Mono<String> login(LoginBody loginBody, ServerWebExchange exchange) {
        String username = loginBody.getUsername();
        String password = loginBody.getPassword();

        // 验证码校验
        return this.validateCaptcha(loginBody, exchange)
                // 登录前置校验
                .then(this.loginPreCheck(loginBody, exchange))
                // 用户验证
                .then(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password))
                        .onErrorResume(error -> {
                            if (error instanceof BadCredentialsException) {
                                return asyncFactory.recordAccessInfo(exchange, username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match"))
                                        .then(Mono.error(error));
                            }
                            return asyncFactory.recordAccessInfo(exchange, username, Constants.LOGIN_FAIL, error.getMessage())
                                    .then(Mono.error(error));
                        })
                )
                // 登录成功
                .flatMap(authentication -> {

                    LoginUser loginUser = (LoginUser) authentication.getPrincipal();

                    // 记录登录信息
                    return asyncFactory.recordAccessInfo(exchange, username, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success"))
                            .then(sysUserRepository.updateLoginInfo(loginUser.getUserId(), IpUtils.getIpAddr(exchange), LocalDateTime.now()))
                            .then(tokenService.createToken(loginUser, exchange));
                });
    }

    /**
     * 校验验证码
     */
    public Mono<Void> validateCaptcha(LoginBody loginBody, ServerWebExchange exchange) {
        String username = loginBody.getUsername();
        String code = loginBody.getCode();
        String uuid = loginBody.getUuid();

        return configService.selectCaptchaEnabled()
                .flatMap(captchaEnabled -> {
                    if (captchaEnabled) {
                        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + StringUtils.nvl(uuid, "");
                        return reactiveRedisUtils.getCacheObject(verifyKey)
                                .switchIfEmpty(asyncFactory.recordAccessInfo(exchange, username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.expire"))
                                        .then(Mono.error(new CaptchaExpireException()))
                                )
                                .flatMap(captcha -> {
                                    // 删除缓存
                                    return reactiveRedisUtils.deleteObject(verifyKey)
                                            .flatMap(bool -> {
                                                if (!code.equalsIgnoreCase(captcha)) {
                                                    return asyncFactory.recordAccessInfo(exchange, username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.error"))
                                                            .then(Mono.error(new CaptchaException()));
                                                }
                                                return Mono.empty();
                                            });
                                });
                    }
                    return Mono.empty();
                })
                .then();
    }

    /**
     * 登录前置校验
     */
    public Mono<Void> loginPreCheck(LoginBody loginBody, ServerWebExchange exchange) {
        String username = loginBody.getUsername();
        String password = loginBody.getPassword();

        // 用户名或密码为空 错误
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return asyncFactory.recordAccessInfo(exchange, username, Constants.LOGIN_FAIL, MessageUtils.message("not.null"))
                    .then(Mono.error(new UserNotExistsException()));
        }
        // 密码如果不在指定范围内 错误
        if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH) {
            return asyncFactory.recordAccessInfo(exchange, username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match"))
                    .then(Mono.error(new UserPasswordNotMatchException()));
        }
        // 用户名不在指定范围内 错误
        if (username.length() < UserConstants.USERNAME_MIN_LENGTH
                || username.length() > UserConstants.USERNAME_MAX_LENGTH) {
            return asyncFactory.recordAccessInfo(exchange, username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match"))
                    .then(Mono.error(new UserPasswordNotMatchException()));
        }
        // IP黑名单校验
        return configService.selectConfigByKey("sys.login.blackIPList")
                .flatMap(blackStr -> {
                    if (IpUtils.isMatchedIp(blackStr, IpUtils.getIpAddr(exchange))) {
                        return asyncFactory.recordAccessInfo(exchange, username, Constants.LOGIN_FAIL, MessageUtils.message("login.blocked"))
                                .then(Mono.error(new BlackListException()));
                    }
                    return Mono.empty();
                })
                .then();
    }

}
