package com.power.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.power.api.feign.WorkMicroServiceFeign;
import com.power.enums.Sex;
import com.power.enums.ShowWhichName;
import com.power.enums.UserRole;
import com.power.mapper.UsersMapper;
import com.power.pojo.Users;
import com.power.service.UsersService;
import com.power.utils.DesensitizationUtil;
import com.power.utils.LocalDateUtils;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author power
 * @since 2026-03-21
 */
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements UsersService {
    @Autowired
    private UsersMapper usersMapper;
    @Autowired
    private WorkMicroServiceFeign workMicroServiceFeign;

    private static final String USER_FACE1 = "http://122.152.205.72:88/group1/M00/00/05/CpoxxF6ZUySASMbOAABBAXhjY0Y649.png";
    @Override
    public boolean save(Users users) {
        usersMapper.insert(users);
        return false;
    }

    @Override
    public Users queryMobileIsExist(String mobile) {

        return usersMapper.selectOne(new QueryWrapper<Users>()
                .eq("mobile", mobile));

    }
//    @Transactional
    @GlobalTransactional
    @Override
    public Users createUser(String mobile) {
        Users user = new Users();
        user.setMobile(mobile);
        user.setNickname("用户" + DesensitizationUtil.commonDisplay(mobile));
        user.setRealName("用户" + DesensitizationUtil.commonDisplay(mobile));
        user.setShowWhichName(ShowWhichName.nickname.type);

        user.setSex(Sex.secret.type);
        user.setFace(USER_FACE1);
        user.setEmail("");

        LocalDate birthday = LocalDateUtils
                .parseLocalDate("1980-01-01",
                        LocalDateUtils.DATE_PATTERN);
        user.setBirthday(birthday);

        user.setCountry("中国");
        user.setProvince("");
        user.setCity("");
        user.setDistrict("");
        user.setDescription("这家伙很懒，什么都没留下~");

        // 我参加工作的日期，默认使用注册当天的日期
        user.setStartWorkDate(LocalDate.now());
        user.setPosition("底层码农");
        user.setRole(UserRole.CANDIDATE.type);
        user.setHrInWhichCompanyId("");

        user.setCreatedTime(LocalDateTime.now());
        user.setUpdatedTime(LocalDateTime.now());

        usersMapper.insert(user);
        //远程调用，创建简历
        workMicroServiceFeign.init(user.getId());

        return user;

    }
}
