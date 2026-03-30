package com.power.api.interceptor;

import com.power.base.BaseInfoProperties;
import com.power.exceptions.GraceException;
import com.power.exceptions.MyCustomException;
import com.power.result.ResponseStatusEnum;
import com.power.utils.IPUtil;import lombok.extern.slf4j.Slf4j;import org.checkerframework.checker.nullness.qual.Nullable;
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
           GraceException.dispaly(ResponseStatusEnum.SMS_NEED_WAIT_ERROR);
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
