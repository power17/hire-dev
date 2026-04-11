package com.power.controller;

import com.google.gson.Gson;
import com.power.api.interceptor.JwtUserInterceptor;
import com.power.base.BaseInfoProperties;
import com.power.pojo.Stu;
import com.power.pojo.Users;
import com.power.result.JsonResult;
import com.power.service.StuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("u")
@Slf4j
public class HelloController extends BaseInfoProperties {
    @Autowired
    private StuService stuService;
    @Autowired
    private BaseInfoProperties baseInfoProperties;
    @Value("${server.port}")
    private String port;


    @GetMapping("hello")
    public Object hello(HttpServletRequest request) {
        String userJson = request.getHeader(APP_USER_JSON);
        Users jwtUsers = new Gson().fromJson(userJson, Users.class);
        Users users = JwtUserInterceptor.currentUser.get();
//        log.info(jwtUsers.toString());
        log.info(users.toString());

        return JsonResult.ok();
    }
}
