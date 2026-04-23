package com.power.controller;

import com.google.gson.Gson;
import com.power.api.task.SMSTask;
import com.power.base.BaseInfoProperties;
import com.power.bo.RegisterBo;
import com.power.pojo.Users;
import com.power.result.GraceJsonResult;
import com.power.result.ResponseStatusEnum;
import com.power.service.UsersService;
import com.power.utils.IPUtil;
import com.power.utils.JWTUtils;
import com.power.utils.SMSUtils;
import com.power.vo.UsersVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("passport")
@Slf4j
public class PassportController extends BaseInfoProperties {
    @Autowired
    private SMSUtils smsUtils;
    @Autowired
    private UsersService usersService;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private SMSTask smsTask;

    @PostMapping("getSmsCode")
    public GraceJsonResult getSMSCode(String mobile, HttpServletRequest request)  {
//        if (StringUtils.isBlank(mobile)) {
//            return GraceJsonResult.error();
//        }
        // 限制用户60s发送一次
        String ip = IPUtil.getRequestIp(request);
        redis.setnx60s(MOBILE_SMSCODE + ":" + ip,  mobile);

        String code = (int) ((Math.random() * 9  + 1)  * 100000) + "";
//        发送短信
//        smsUtils.sendSMS(mobile, code);
        smsTask.sendTask();
        redis.set(MOBILE_SMSCODE + ":" + mobile, code,  30 * 60);
        log.info("验证码为: {}", code);
        return  GraceJsonResult.ok();
    }
    @PostMapping("login")
    public GraceJsonResult login(@Valid @RequestBody RegisterBo registerBo) {
        String mobile = registerBo.getMobile();
        String code = registerBo.getSmsCode();
        // 1. 从redis中获得验证码进行校验判断是否匹配
        String redisCode = redis.get(MOBILE_SMSCODE + ":" + mobile);
        if (StringUtils.isBlank(redisCode) || !redisCode.equalsIgnoreCase(code)) {
            return GraceJsonResult.errorCustom(ResponseStatusEnum.SMS_CODE_ERROR);
        }
        // 2. 根据mobile查询数据库，判断用户是否存在
        Users user = usersService.queryMobileIsExist(mobile);
        if (user == null) {
            // 2.1 如果查询的用户为空，则表示没有注册过，则需要注册信息入库
             user = usersService.createUser(mobile);
        }

        // 4. 用户登录注册以后，删除redis中的短信验证码
        redis.del(MOBILE_SMSCODE + ":" + mobile);

        // 创建jwt
        String jwt = jwtUtils.createJWTWithPrefix(new Gson().toJson(user), TOKEN_USER_PREFIX);

        // 构建vo
        UsersVO usersVO = new UsersVO();
        BeanUtils.copyProperties(user, usersVO);
        usersVO.setUserToken(jwt);

        return GraceJsonResult.ok(usersVO);
    }

}
