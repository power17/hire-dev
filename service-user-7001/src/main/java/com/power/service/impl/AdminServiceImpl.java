package com.power.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.power.base.BaseInfoProperties;
import com.power.bo.CreateAdminBO;
import com.power.bo.UpdateAdminBO;
import com.power.exceptions.GraceException;
import com.power.mapper.AdminMapper;
import com.power.mapper.StuMapper;
import com.power.pojo.Admin;
import com.power.pojo.Stu;
import com.power.result.ResponseStatusEnum;
import com.power.service.AdminService;
import com.power.service.StuService;
import com.power.utils.MD5Utils;
import com.power.utils.PagedGridResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author power
 * @since 2026-03-21
 */
@Service
public class AdminServiceImpl  extends BaseInfoProperties implements AdminService {
    @Autowired
    private AdminMapper adminMapper;

    @Override
    public void createAdmin(CreateAdminBO createAdminBO) {
        // admin账号判断是否存在，如果存在，则禁止账号分配
        Admin admin = getSelfAdmin(createAdminBO.getUsername());
        // 优雅异常解耦完美体现
        if (admin != null) GraceException.display(ResponseStatusEnum.ADMIN_USERNAME_EXIST_ERROR);
        // 创建账号
        Admin newAdmin = new Admin();
        BeanUtils.copyProperties(createAdminBO, newAdmin);
        String slat = (int)((Math.random() * 9 + 1) * 100000) + "";
        String pwd = MD5Utils.encrypt(createAdminBO.getPassword(), slat);

        newAdmin.setPassword(pwd);
        newAdmin.setSlat(slat);
        newAdmin.setCreateTime(LocalDateTime.now());
        newAdmin.setUpdatedTime(LocalDateTime.now());

        adminMapper.insert(newAdmin);
    }

    @Override
    public PagedGridResult getAdminList(String accountName, Integer page, Integer limit) {

        PageHelper.startPage(page, limit);

        List<Admin> adminList = adminMapper.selectList(
                new QueryWrapper<Admin>()
                        .like("username", accountName)
        );

        return setterPagedGrid(adminList, page);
    }

    @Override
    public void deleteAdmin(String username) {
        int res = adminMapper.delete(
                new QueryWrapper<Admin>()
                        .eq("username", username)
                        .ne("username", "admin")
        );

        if (res == 0) GraceException.display(ResponseStatusEnum.ADMIN_DELETE_ERROR);

    }

    @Override
    public Admin getById(String adminId) {
        return adminMapper.selectById(adminId);
    }

    @Override
    public void updateAdmin(UpdateAdminBO adminBO) {
        Admin admin = new Admin();
        BeanUtils.copyProperties(adminBO, admin);
        admin.setUpdatedTime(LocalDateTime.now());
        adminMapper.updateById(admin);
    }
    private Admin getSelfAdmin(String username) {
        Admin admin = adminMapper.selectOne(
                new QueryWrapper<Admin>()
                        .eq("username", username)
        );
        return admin;
    }
}
