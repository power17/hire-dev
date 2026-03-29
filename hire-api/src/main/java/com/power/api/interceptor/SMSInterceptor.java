package com.power.api.interceptor;

import com.power.controller.base.BaseInfoProperties;import com.power.controller.utils.IPUtil;import lombok.extern.slf4j.Slf4j;import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class SMSInterceptor extends BaseInfoProperties implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ip = IPUtil.getRequestIp(request);
        Boolean isExistIp = redis.keyIsExist(MOBILE_SMSCODE + ":" + ip);
        if (isExistIp) {
            log.error("短信发送频率太高了~~~");
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
    }
}
