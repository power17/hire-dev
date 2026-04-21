package com.power.service;

import com.power.bo.CreateAdminBO;
import com.power.bo.UpdateAdminBO;
import com.power.utils.PagedGridResult;
import com.power.pojo.Admin;
public interface AdminService {

    /**
     * 创建admin账号
     * @param createAdminBO
     */
    public void createAdmin(CreateAdminBO createAdminBO);

    /**
     * 查询admin列表
     * @param accountName
     * @param page
     * @param limit
     * @return
     */
    public PagedGridResult getAdminList(String accountName,
                                        Integer page,
                                        Integer limit);

    /**
     * 删除admin账号
     * @param username
     */
    public void deleteAdmin(String username);

    /**
     * 查询admin信息
     * @param adminId
     * @return
     */
    public Admin getById(String adminId);

    /**
     * 更新admin信息
     * @param adminBO
     */
    public void updateAdmin(UpdateAdminBO adminBO);
}
