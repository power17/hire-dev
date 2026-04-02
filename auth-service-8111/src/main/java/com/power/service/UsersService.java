package com.power.service;

import com.power.pojo.Users;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author power
 * @since 2026-03-21
 */
public interface UsersService {
    public boolean createUser(String mobile);
    public Users queryMobileIsExist (String mobile);
}
