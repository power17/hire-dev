package com.power.service;


import com.power.bo.ModifyUserBO;
import com.power.pojo.Users;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author power
 * @since 2026-03-21
 */
public interface UserService {
    /**
     * 修改用户信息
     * @param userBO
     */
    public void modifyUserInfo(ModifyUserBO userBO);

    /**
     * 获得用户信息
     * @param uid
     * @return
     */
    public Users getById(String uid);
}
