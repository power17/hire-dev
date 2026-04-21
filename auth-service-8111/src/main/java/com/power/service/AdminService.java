package com.power.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.power.bo.AdminBo;
import com.power.pojo.Admin;

public interface AdminService  extends IService<Admin>{
    /**
     * admin 登录
     * @param adminBO
     * @return
     */
    public boolean adminLogin(AdminBo adminBO);

    /**
     * 获得admin信息
     * @param adminBO
     * @return
     */
    public Admin getAdminInfo(AdminBo adminBO);
}
