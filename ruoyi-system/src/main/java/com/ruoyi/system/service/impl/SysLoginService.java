package com.ruoyi.system.service.impl;

import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.model.LoginBody;
import com.ruoyi.common.exception.user.*;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.ip.IpUtils;
import com.ruoyi.framework.manager.AsyncManager;
import com.ruoyi.framework.manager.factory.AsyncFactory;
import com.ruoyi.framework.redis.ReactiveRedisUtils;
import com.ruoyi.framework.security.LoginUser;
import com.ruoyi.framework.security.context.AuthenticationContextHolder;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.system.service.SysConfigService;
import com.ruoyi.system.service.SysUserService;
import jakarta.annotation.Resource;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 登录校验方法
 *
 * @author ruoyi
 */
@Component
public class SysLoginService {

    @Resource
    private AsyncFactory AsyncFactory;

    @Resource
    private TokenService tokenService;

    @Resource
    private ReactiveAuthenticationManager authenticationManager;

    @Resource
    private ReactiveRedisUtils<String> reactiveRedisUtils;

    @Resource
    private SysUserService userService;

    @Resource
    private SysConfigService configService;

    /**
     * 登录验证
     */
    public Mono<String> login(LoginBody loginBody, ServerWebExchange exchange) {
        String username = loginBody.getUsername();
        String password = loginBody.getPassword();

        // 验证码校验
        return Mono.fromRunnable(() -> validateCaptcha(loginBody, exchange))
                // 登录前置校验
                .then(Mono.fromRunnable(() -> loginPreCheck(loginBody, exchange)))
                // 用户验证
                .then(Mono.defer(() -> {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
                    AuthenticationContextHolder.setContext(authenticationToken);

                    return authenticationManager.authenticate(authenticationToken)
                            .doOnError(BadCredentialsException.class, e -> AsyncManager.me().execute(AsyncFactory.recordLogininfor(exchange, username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match"))))
                            .doOnError(e -> AsyncManager.me().execute(AsyncFactory.recordLogininfor(exchange, username, Constants.LOGIN_FAIL, e.getMessage())))
                            .doFinally(signal -> AuthenticationContextHolder.clearContext());
                }))
                // 登录成功
                .flatMap(auth -> {
                    AsyncManager.me().execute(AsyncFactory.recordLogininfor(exchange, username, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
                    LoginUser loginUser = (LoginUser) auth.getPrincipal();

                    return Mono.fromRunnable(() -> recordLoginInfo(loginUser.getUserId(), exchange))
                            .then(Mono.just(tokenService.createToken(loginUser)));
                });
    }

    /**
     * 校验验证码
     */
    public void validateCaptcha(LoginBody loginBody, ServerWebExchange exchange) {
        String username = loginBody.getUsername();
        String code = loginBody.getCode();
        String uuid = loginBody.getUuid();

        boolean captchaEnabled = configService.selectCaptchaEnabled();
        if (captchaEnabled) {
            String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + StringUtils.nvl(uuid, "");
            reactiveRedisUtils.getCacheObject(verifyKey)
                    .switchIfEmpty(Mono.defer(() -> {
                        AsyncManager.me().execute(AsyncFactory.recordLogininfor(exchange, username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.expire")));
                        throw new CaptchaExpireException();
                    }))
                    .subscribe(captcha -> {
                        // 删除缓存
                        reactiveRedisUtils.deleteObject(verifyKey)
                                .subscribe(bool -> {
                                    if (!code.equalsIgnoreCase(captcha)) {
                                        AsyncManager.me().execute(AsyncFactory.recordLogininfor(exchange, username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.error")));
                                        throw new CaptchaException();
                                    }
                                });
                    });
        }
    }

    /**
     * 登录前置校验
     */
    public void loginPreCheck(LoginBody loginBody, ServerWebExchange exchange) {
        String username = loginBody.getUsername();
        String password = loginBody.getPassword();

        // 用户名或密码为空 错误
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(exchange, username, Constants.LOGIN_FAIL, MessageUtils.message("not.null")));
            throw new UserNotExistsException();
        }
        // 密码如果不在指定范围内 错误
        if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(exchange, username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
            throw new UserPasswordNotMatchException();
        }
        // 用户名不在指定范围内 错误
        if (username.length() < UserConstants.USERNAME_MIN_LENGTH
                || username.length() > UserConstants.USERNAME_MAX_LENGTH) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(exchange, username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
            throw new UserPasswordNotMatchException();
        }
        // IP黑名单校验
        configService.selectConfigByKey("sys.login.blackIPList")
                .subscribe(blackStr -> {
                    if (IpUtils.isMatchedIp(blackStr, IpUtils.getIpAddr(exchange))) {
                        AsyncManager.me().execute(AsyncFactory.recordLogininfor(exchange, username, Constants.LOGIN_FAIL, MessageUtils.message("login.blocked")));
                        throw new BlackListException();
                    }
                });
    }

    /**
     * 记录登录信息
     */
    public void recordLoginInfo(Long userId, ServerWebExchange exchange) {
        userService.updateLoginInfo(userId, IpUtils.getIpAddr(exchange), DateUtils.getNowDate());
    }
}
