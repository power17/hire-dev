package com.power.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.power.bo.AdminBo;

import com.power.mapper.AdminMapper;
import com.power.pojo.Admin;
import com.power.service.AdminService;

import com.power.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin>  implements AdminService {

    @Autowired
    private AdminMapper adminMapper;
    @Override
    public boolean adminLogin(AdminBo adminBO) {
        Admin admin = getSelfAdmin(adminBO.getUsername());
        if(admin == null) {
            return false;
        }
        String slat = admin.getSlat();
        String md5Str =  MD5Utils.encrypt(adminBO.getPassword(), slat);
        if (md5Str.equalsIgnoreCase(admin.getPassword())) {
            return true;
        }
        return false;
    }

    @Override
    public Admin getAdminInfo(AdminBo adminBO) {
        return this.getSelfAdmin(adminBO.getUsername());
    }

    private Admin getSelfAdmin(String username) {
        Admin admin = adminMapper.selectOne(
                new QueryWrapper<Admin>()
                        .eq("username", username)
        );
        return admin;
    }
}
