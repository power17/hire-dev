package com.power.api.interceptor;

import com.google.gson.Gson;
import com.power.base.BaseInfoProperties;
import com.power.pojo.Admin;
import com.power.pojo.Users;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// 使用ThreadLocal可以在同一个线程内共享数据
// 比如：Interceptor --> 控制器Controller --> 业务层Service --> 数据层Mapper(da
@Slf4j
public class JwtUserInterceptor extends BaseInfoProperties implements HandlerInterceptor {
    public static ThreadLocal<Users> currentUser = new ThreadLocal<>();
    public static ThreadLocal<Admin> adminUser = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String appUserJson = request.getHeader(APP_USER_JSON);
        String saasUserJson = request.getHeader(SAAS_USER_JSON);
        String adminUserJson = request.getHeader(ADMIN_USER_JSON);

        if (StringUtils.isNotBlank(appUserJson)
                || StringUtils.isNotBlank(saasUserJson)) {
            Users appUser = new Gson().fromJson(appUserJson, Users.class);
            currentUser.set(appUser);
        }

        if (StringUtils.isNotBlank(adminUserJson)) {
            Admin admin = new Gson().fromJson(adminUserJson, Admin.class);
            adminUser.set(admin);
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        currentUser.remove();
        adminUser.remove();
    }
}
