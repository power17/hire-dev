package com.power.controller;

import com.power.controller.base.BaseInfoProperties;
import com.power.controller.result.GraceJsonResult;
import com.power.controller.utils.IPUtil;
import com.power.controller.utils.SMSUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("passport")
@Slf4j
public class PassportController extends BaseInfoProperties {
    @Autowired
    private SMSUtils smsUtils;

    @GetMapping("getSmsCode")
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
        redis.set(MOBILE_SMSCODE + ":" + mobile, code,  30 * 60);
        log.info("验证码为: {}", code);
        return  GraceJsonResult.ok();
    }

}
