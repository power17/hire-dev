package com.power.service.impl;

import com.power.base.BaseInfoProperties;
import com.power.bo.ModifyUserBO;
import com.power.exceptions.GraceException;
import com.power.mapper.UsersMapper;
import com.power.pojo.Users;
import com.power.result.ResponseStatusEnum;
import com.power.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl extends BaseInfoProperties implements UserService {
    @Autowired
    private UsersMapper usersMapper;

    @Override
    public void modifyUserInfo(ModifyUserBO userBO) {
        String userId = userBO.getUserId();
        if (StringUtils.isBlank(userId))
            GraceException.display(ResponseStatusEnum.USER_INFO_UPDATED_ERROR);

        Users pendingUser = new Users();
        pendingUser.setId(userId);
        pendingUser.setUpdatedTime(LocalDateTime.now());

        BeanUtils.copyProperties(userBO, pendingUser);

        usersMapper.updateById(pendingUser);

    }

    @Override
    public Users getById(String uid) {
        return usersMapper.selectById(uid);
    }
}
