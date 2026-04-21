package com.power.controller;

import com.power.api.interceptor.JwtUserInterceptor;
import com.power.base.BaseInfoProperties;
import com.power.bo.CreateAdminBO;
import com.power.bo.UpdateAdminBO;
import com.power.pojo.Admin;
import com.power.result.GraceJsonResult;
import com.power.service.AdminService;
import com.power.utils.PagedGridResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("admininfo")
@Slf4j
public class AdminInfoController extends BaseInfoProperties {
    @Autowired
    private AdminService adminService;
    @PostMapping("create")
    public GraceJsonResult create(@Valid @RequestBody CreateAdminBO createAdminBO) {
        adminService.createAdmin(createAdminBO);
        return GraceJsonResult.ok();
    }


    @PostMapping("list")
    public GraceJsonResult list(String accountName,
                                Integer page,
                                Integer limit) {

        if (page == null) page = 1;
        if (limit == null) limit = 10;

        PagedGridResult listResult = adminService.getAdminList(accountName,
                page,
                limit);

        return GraceJsonResult.ok(listResult);
    }

    @PostMapping("delete")
    public GraceJsonResult delete(String username) {
        adminService.deleteAdmin(username);
        return GraceJsonResult.ok();
    }

    @PostMapping("updateMyInfo")
    public GraceJsonResult updateMyInfo(@RequestBody @Valid UpdateAdminBO adminBO) {
        Admin admin = JwtUserInterceptor.adminUser.get();

        adminBO.setId(admin.getId());
        adminService.updateAdmin(adminBO);

        return GraceJsonResult.ok();
    }

//    @PostMapping("resetPwd")
//    public GraceJsonResult resetPwd(@RequestBody ResetPwdBO resetPwdBO) {
//
//        // resetPwdBO 校验
//        // adminService 重置密码
//
//        resetPwdBO.modifyPwd();
//        return GraceJsonResult.ok();
//    }
//
//    @PostMapping("myInfo")
//    public GraceJsonResult myInfo() {
//
//        Admin admin = JwtUserInterceptor.adminUser.get();
//
//        Admin adminInfo = adminService.getById(admin.getId());
//
//        AdminInfoVO adminInfoVO = new AdminInfoVO();
//        BeanUtils.copyProperties(adminInfo, adminInfoVO);
//
//        return GraceJsonResult.ok(adminInfoVO);
//    }
}
