package com.power.controller;

import com.power.result.GraceJsonResult;
import com.power.utils.SMSUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("passport")
@Slf4j
public class PassportController {
    @Autowired
    private SMSUtils smsUtils;

    @GetMapping("getSmsCode")
    public GraceJsonResult getSMSCode(String mobile) {
        String code = (int) ((Math.random() * 9  + 1)  * 100000) + "";
//        发送短信
//        smsUtils.sendSMS(mobile, code);

        return  GraceJsonResult.ok();
    }

}
