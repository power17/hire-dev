package com.power.controller;

import com.google.gson.Gson;
import com.power.api.interceptor.JwtUserInterceptor;
import com.power.base.BaseInfoProperties;
import com.power.pojo.Users;
import com.power.result.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("w")
@Slf4j
public class HelloController extends BaseInfoProperties {



    @Value("${server.port}")
    private String port;

    @GetMapping("hello")
    public Object hello(HttpServletRequest request) {
        return JsonResult.ok();
    }
    @GetMapping("port")
    public Object port(HttpServletRequest request) {
        log.info("port: {}", port);
        return JsonResult.ok();
    }
}
