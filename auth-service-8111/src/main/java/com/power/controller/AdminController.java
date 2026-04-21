package com.power.controller;

import com.google.gson.Gson;
import com.power.api.interceptor.JwtUserInterceptor;
import com.power.base.BaseInfoProperties;
import com.power.bo.AdminBo;
import com.power.pojo.Admin;
import com.power.result.GraceJsonResult;
import com.power.result.ResponseStatusEnum;
import com.power.service.AdminService;
import com.power.utils.JWTUtils;
import com.power.vo.AdminVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin")
@Slf4j
public class AdminController extends BaseInfoProperties {
    @Autowired
    private AdminService adminService;
    @Autowired
    private JWTUtils jwtUtils;

    @PostMapping("/login")
    public GraceJsonResult login(@Valid @RequestBody AdminBo adminBo) {
        boolean isExist = adminService.adminLogin(adminBo);
        if(!isExist) {
            return GraceJsonResult.errorCustom(ResponseStatusEnum.ADMIN_LOGIN_ERROR);
        }
        // 登录成功之后获得admin信息
        Admin admin = adminService.getAdminInfo(adminBo);
        String adminToken = jwtUtils.createJWTWithPrefix(new Gson().toJson(admin),
                TOKEN_ADMIN_PREFIX);
        return  GraceJsonResult.ok(adminToken);
    }

    @GetMapping("info")
    public GraceJsonResult info() {

        Admin admin = JwtUserInterceptor.adminUser.get();
        AdminVO adminVO = new AdminVO();
        BeanUtils.copyProperties(admin, adminVO);

        return GraceJsonResult.ok(adminVO);
    }

    @PostMapping("logout")
    public GraceJsonResult logout() {
        return GraceJsonResult.ok();
    }
}
