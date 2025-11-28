package com.ruoyi.system.service.impl;

import com.alibaba.fastjson2.JSON;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.enums.BusinessStatus;
import com.ruoyi.common.enums.HttpMethod;
import com.ruoyi.common.filter.PropertyPreExcludeFilter;
import com.ruoyi.common.utils.ExceptionUtil;
import com.ruoyi.common.utils.LogUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.ip.AddressUtils;
import com.ruoyi.common.utils.ip.IpUtils;
import com.ruoyi.framework.manager.factory.AsyncFactory;
import com.ruoyi.framework.security.ReactiveSecurityUtils;
import com.ruoyi.system.entity.SysAccessLog;
import com.ruoyi.system.entity.SysOperateLog;
import com.ruoyi.system.repository.SysAccessLogRepository;
import com.ruoyi.system.repository.SysOperateLogRepository;
import eu.bitwalker.useragentutils.UserAgent;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.JoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

/**
 * 异步工厂（产生任务用）
 *
 * @author ruoyi
 */
@Component
public class AsyncFactoryImpl implements AsyncFactory {

    private static final Logger sys_user_logger = LoggerFactory.getLogger("sys-user");

    /**
     * 排除敏感属性字段
     */
    public static final String[] EXCLUDE_PROPERTIES = {"password", "oldPassword", "newPassword", "confirmPassword"};

    @Resource
    private SysAccessLogRepository sysAccessLogRepository;
    @Resource
    private SysOperateLogRepository sysOperateLogRepository;

    /**
     * 记录访问信息
     */
    public Mono<Void> recordAccessInfo(ServerWebExchange exchange, String username, String status, String message, Object... args) {
        final UserAgent userAgent = UserAgent.parseUserAgentString(exchange.getRequest().getHeaders().getFirst("User-Agent"));
        final String ip = IpUtils.getIpAddr(exchange);
        String address = AddressUtils.getRealAddressByIP(ip);
        String s = LogUtils.getBlock(ip) +
                address +
                LogUtils.getBlock(username) +
                LogUtils.getBlock(status) +
                LogUtils.getBlock(message);
        // 打印信息到日志
        sys_user_logger.info(s, args);
        // 获取客户端操作系统
        String os = userAgent.getOperatingSystem().getName();
        // 获取客户端浏览器
        String browser = userAgent.getBrowser().getName();

        // 封装对象
        SysAccessLog log = new SysAccessLog();
        log.setUserName(username);
        log.setIpaddr(ip);
        log.setLocation(address);
        log.setBrowser(browser);
        log.setOs(os);
        log.setMsg(message);
        // 日志状态
        if (StringUtils.equalsAny(status, Constants.LOGIN_SUCCESS, Constants.LOGOUT, Constants.REGISTER)) {
            log.setStatus(Constants.SUCCESS);
        } else if (Constants.LOGIN_FAIL.equals(status)) {
            log.setStatus(Constants.FAIL);
        }
        // 插入数据
        return sysAccessLogRepository.save(log).then();
    }

    /**
     * 记录操作信息
     */
    public Mono<Void> recordOperateInfo(ServerWebExchange exchange, JoinPoint joinPoint, Log controllerLog, Throwable e, Object jsonResult, long startTime) {
        // *========数据库日志=========*//
        SysOperateLog operLog = new SysOperateLog();
        operLog.setStatus(BusinessStatus.SUCCESS.ordinal());

        if (Objects.nonNull(e)) {
            operLog.setStatus(BusinessStatus.FAIL.ordinal());
            operLog.setErrorMsg(StringUtils.substring(Convert.toStr(e.getMessage(), ExceptionUtil.getExceptionMessage(e)), 0, 2000));
        }
        // 设置方法名称
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        operLog.setMethod(className + "." + methodName + "()");
        // 设置消耗时间
        operLog.setCostTime(System.currentTimeMillis() - startTime);
        operLog.setOperTime(LocalDateTime.now());

        // 请求的地址
        operLog.setOperIp(IpUtils.getIpAddr(exchange));
        operLog.setOperUrl(StringUtils.substring(exchange.getRequest().getURI().getPath(), 0, 255));
        // 设置请求方式
        operLog.setRequestMethod(exchange.getRequest().getMethod().toString());

        // 处理设置注解上的参数
        getControllerMethodDescription(exchange.getRequest(), joinPoint, controllerLog, operLog, jsonResult);

        // 远程查询操作地点
        operLog.setOperLocation(AddressUtils.getRealAddressByIP(operLog.getOperIp()));

        // 获取当前的用户
        return ReactiveSecurityUtils.getLoginUser()
                .map(loginUser -> {
                    if (loginUser != null) {
                        operLog.setOperName(loginUser.getUsername());
//                        SysUser currentUser = loginUser.getUser();
//                        if (StringUtils.isNotNull(currentUser) && StringUtils.isNotNull(currentUser.getDept())) {
//                            operLog.setDeptName(currentUser.getDept().getDeptName());
//                        }
                    }
                    return operLog;
                })
                .then(sysOperateLogRepository.save(operLog))
                .then();
    }

    /**
     * 获取注解中对方法的描述信息 用于Controller层注解
     *
     * @param log     日志
     * @param operLog 操作日志
     */
    public void getControllerMethodDescription(ServerHttpRequest request, JoinPoint joinPoint, Log log, SysOperateLog operLog, Object jsonResult) {
        // 设置action动作
        operLog.setBusinessType(log.businessType().ordinal());
        // 设置标题
        operLog.setTitle(log.title());
        // 设置操作人类别
        operLog.setOperatorType(log.operatorType().ordinal());
        // 是否需要保存request，参数和值
        if (log.isSaveRequestData()) {
            // 获取参数的信息，传入到数据库中。
            setRequestValue(request, joinPoint, operLog, log.excludeParamNames());
        }
        // 是否需要保存response，参数和值
        if (log.isSaveResponseData() && StringUtils.isNotNull(jsonResult)) {
            operLog.setJsonResult(StringUtils.substring(JSON.toJSONString(jsonResult), 0, 2000));
        }
    }

    /**
     * 获取请求的参数，放到log中
     *
     */
    private void setRequestValue(ServerHttpRequest request, JoinPoint joinPoint, SysOperateLog operLog, String[] excludeParamNames) {
        Map<?, ?> paramsMap = request.getQueryParams();
        String requestMethod = operLog.getRequestMethod();
        if (StringUtils.isEmpty(paramsMap) && StringUtils.equalsAny(requestMethod, HttpMethod.PUT.name(), HttpMethod.POST.name(), HttpMethod.DELETE.name())) {
            String params = argsArrayToString(joinPoint.getArgs(), excludeParamNames);
            operLog.setOperParam(StringUtils.substring(params, 0, 2000));
        } else {
            operLog.setOperParam(StringUtils.substring(JSON.toJSONString(paramsMap, excludePropertyPreFilter(excludeParamNames)), 0, 2000));
        }
    }

    /**
     * 参数拼装
     */
    private String argsArrayToString(Object[] paramsArray, String[] excludeParamNames) {
        StringBuilder params = new StringBuilder();
        if (paramsArray != null) {
            for (Object o : paramsArray) {
                if (StringUtils.isNotNull(o) && !isFilterObject(o)) {
                    try {
                        String jsonObj = JSON.toJSONString(o, excludePropertyPreFilter(excludeParamNames));
                        params.append(jsonObj).append(" ");
                    } catch (Exception ignored) {
                    }
                }
            }
        }
        return params.toString().trim();
    }

    /**
     * 忽略敏感属性
     */
    public PropertyPreExcludeFilter excludePropertyPreFilter(String[] excludeParamNames) {
        return new PropertyPreExcludeFilter().addExcludes(ArrayUtils.addAll(EXCLUDE_PROPERTIES, excludeParamNames));
    }

    /**
     * 判断是否需要过滤的对象。
     *
     * @param o 对象信息。
     * @return 如果是需要过滤的对象，则返回true；否则返回false。
     */
    @SuppressWarnings("rawtypes")
    public boolean isFilterObject(final Object o) {
        Class<?> clazz = o.getClass();
        if (clazz.isArray()) {
            return clazz.getComponentType().isAssignableFrom(MultipartFile.class);
        } else if (Collection.class.isAssignableFrom(clazz)) {
            Collection collection = (Collection) o;
            for (Object value : collection) {
                return value instanceof MultipartFile;
            }
        } else if (Map.class.isAssignableFrom(clazz)) {
            Map map = (Map) o;
            for (Object value : map.entrySet()) {
                Map.Entry entry = (Map.Entry) value;
                return entry.getValue() instanceof MultipartFile;
            }
        }
        return o instanceof MultipartFile || o instanceof HttpServletRequest || o instanceof HttpServletResponse
                || o instanceof BindingResult;
    }
}
