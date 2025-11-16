package com.ruoyi.framework.web.exception;

import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.exception.DemoModeException;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.html.EscapeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * 全局异常处理器
 *
 * @author ruoyi
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 权限校验异常
     */
    @ExceptionHandler(AccessDeniedException.class)
    public Mono<R<Void>> handleAccessDeniedException(AccessDeniedException e, ServerHttpRequest request) {
        String requestURI = request.getURI().getPath();
        log.error("请求地址'{}',权限校验失败'{}'", requestURI, e.getMessage());
        return Mono.just(R.fail(HttpStatus.FORBIDDEN, "没有权限，请联系管理员授权"));
    }

    /**
     * 请求方式不支持
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Mono<R<Void>> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e,
                                                             ServerHttpRequest request) {
        String requestURI = request.getURI().getPath();
        log.error("请求地址'{}',不支持'{}'请求", requestURI, e.getMethod());
        return Mono.just(R.fail(e.getMessage()));
    }

    /**
     * 业务异常
     */
    @ExceptionHandler(ServiceException.class)
    public Mono<R<Void>> handleServiceException(ServiceException e) {
        log.error(e.getMessage(), e);
        Integer code = e.getCode();
        return Mono.just(Objects.nonNull(code) ? R.fail(code, e.getMessage()) : R.fail(e.getMessage()));
    }

    /**
     * 请求路径中缺少必需的路径变量
     */
    @ExceptionHandler(MissingPathVariableException.class)
    public Mono<R<Void>> handleMissingPathVariableException(MissingPathVariableException e, ServerHttpRequest request) {
        String requestURI = request.getURI().getPath();
        log.error("请求路径中缺少必需的路径变量'{}',发生系统异常.", requestURI, e);
        return Mono.just(R.fail(String.format("请求路径中缺少必需的路径变量[%s]", e.getVariableName())));
    }

    /**
     * 请求参数类型不匹配
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Mono<R<Void>> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e, ServerHttpRequest request) {
        String requestURI = request.getURI().getPath();
        String value = Convert.toStr(e.getValue());
        if (StringUtils.isNotEmpty(value)) {
            value = EscapeUtil.clean(value);
        }
        log.error("请求参数类型不匹配'{}',发生系统异常.", requestURI, e);
        return Mono.just(R.fail(String.format("请求参数类型不匹配，参数[%s]要求类型为：'%s'，但输入值为：'%s'", e.getName(), Objects.requireNonNull(e.getRequiredType()).getName(), value)));
    }

    /**
     * 拦截未知的运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public Mono<R<Void>> handleRuntimeException(RuntimeException e, ServerHttpRequest request) {
        String requestURI = request.getURI().getPath();
        log.error("请求地址'{}',发生未知异常.", requestURI, e);
        return Mono.just(R.fail(e.getMessage()));
    }

    /**
     * 系统异常
     */
    @ExceptionHandler(Exception.class)
    public Mono<R<Void>> handleException(Exception e, ServerHttpRequest request) {
        String requestURI = request.getURI().getPath();
        log.error("请求地址'{}',发生系统异常.", requestURI, e);
        return Mono.just(R.fail(e.getMessage()));
    }

    /**
     * 自定义验证异常
     */
    @ExceptionHandler(BindException.class)
    public Mono<R<Void>> handleBindException(BindException e) {
        log.error(e.getMessage(), e);
        String message = e.getAllErrors().get(0).getDefaultMessage();
        return Mono.just(R.fail(message));
    }

    /**
     * 自定义验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Mono<R<Void>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        String message = Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage();
        return Mono.just(R.fail(message));
    }

    /**
     * 演示模式异常
     */
    @ExceptionHandler(DemoModeException.class)
    public Mono<R<Void>> handleDemoModeException() {
        return Mono.just(R.fail("演示模式，不允许操作"));
    }
}
