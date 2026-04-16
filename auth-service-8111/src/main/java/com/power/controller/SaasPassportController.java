package com.power.controller;

import com.google.gson.Gson;
import com.power.base.BaseInfoProperties;
import com.power.pojo.Users;
import com.power.result.GraceJsonResult;
import com.power.result.ResponseStatusEnum;
import com.power.service.UsersService;
import com.power.utils.JWTUtils;
import com.power.vo.SaasUserVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("saas")
@Slf4j
public class SaasPassportController extends BaseInfoProperties {

    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private UsersService usersService;
    /**
     * 1. 获得二维码token令牌
     * @return
     */
    @PostMapping("getQRToken")
    public GraceJsonResult getQRToken () {
        String qrToken = UUID.randomUUID().toString();
        redis.set(SAAS_PLATFORM_LOGIN_TOKEN + ":" + qrToken, qrToken,  5*60);
        redis.set(SAAS_PLATFORM_LOGIN_TOKEN_READ + ":" + qrToken,  "0", 5*60);
        return GraceJsonResult.ok(qrToken);
    }
    /**
     * 2. 手机端使用HR角色进行扫码操作
     * @param qrToken
     * @return
     */
    @PostMapping("scanCode")
    public GraceJsonResult scanCode(String qrToken, HttpServletRequest request) {
        // 判空 - qrToken
        if (StringUtils.isBlank(qrToken))
            return GraceJsonResult.errorCustom(ResponseStatusEnum.FAILED);

        // 从redis中获得并且判断qrToken是否有效
        String redisQRToken = redis.get(SAAS_PLATFORM_LOGIN_TOKEN + ":" + qrToken);
        if (!redisQRToken.equalsIgnoreCase(qrToken))
            return GraceJsonResult.errorCustom(ResponseStatusEnum.FAILED);

        // 从header中获得用户id和jwt令牌
        String headerUserId = request.getHeader("appUserId");
        String headerUserToken = request.getHeader("appUserToken");

        // 判空 - headerUserId + headerUserToken
        if (StringUtils.isBlank(headerUserId) || StringUtils.isBlank(headerUserToken))
            return GraceJsonResult.errorCustom(ResponseStatusEnum.HR_TICKET_INVALID);

        // 对JWT进行校验
        String userJson = jwtUtils.checkJWT(headerUserToken.split("@")[1]);
        if (StringUtils.isBlank(userJson))
            return GraceJsonResult.errorCustom(ResponseStatusEnum.HR_TICKET_INVALID);

        // 执行后续正常业务
        // 生成预登录token
        String preToken = UUID.randomUUID().toString();
        redis.set(SAAS_PLATFORM_LOGIN_TOKEN + ":" + qrToken, preToken, 5*60);

        // redis写入标记，当前qrToken需要被读取并且失效覆盖，网页端标记二维码已被扫
        redis.set(SAAS_PLATFORM_LOGIN_TOKEN_READ + ":" + qrToken, "1," + preToken, 5*60);

        // 返回给手机端，app下次请求携带preToken
        return GraceJsonResult.ok(preToken);
    }


    /**
     * 3. SAAS网页端每隔一段时间（3秒）定时查询qrToken是否被读取，用于页面的展示标记判断
     * 前端处理：限制用户在页面不操作而频繁发起调用：【页面失效，请刷新后再执行扫描登录！】
     * 注：如果使用websocket或者netty，可以在app扫描之后，在上一个接口，直接通信浏览器（H5）进行页面扫码的状态标记
     * @param qrToken
     * @return
     */
    @PostMapping("codeHasBeenRead")
    public GraceJsonResult codeHasBeenRead(String qrToken) {

        String readStr = redis.get(SAAS_PLATFORM_LOGIN_TOKEN_READ + ":" + qrToken);
        List list = new ArrayList();

        if (StringUtils.isNotBlank(readStr)) {

            String[] readArr = readStr.split(",");
            if (readArr.length >= 2) {
                list.add(Integer.valueOf(readArr[0]));
                list.add(readArr[1]);
            } else {
                list.add(0);
            }

            return GraceJsonResult.ok(list);
        } else {
            return GraceJsonResult.ok(list);
        }
    }
    @PostMapping("goQRLogin")
    public GraceJsonResult goQRLogin(String userId,
                                     String qrToken,
                                     String preToken) {

        String preTokenRedisArr = redis.get(SAAS_PLATFORM_LOGIN_TOKEN_READ + ":" + qrToken);

        if (StringUtils.isNotBlank(preTokenRedisArr)) {
            String preTokenRedis = preTokenRedisArr.split(",")[1];
            if (preTokenRedis.equalsIgnoreCase(preToken)) {
                // 根据用户id获得用户信息
                Users hrUser = usersService.getById(userId);
                if (hrUser == null) {
                    return GraceJsonResult.errorCustom(ResponseStatusEnum.USER_NOT_EXIST_ERROR);
                }

                // 存入用户信息到redis中，因为H5在未登录的情况下，拿不到用户id，所以暂存用户信息到redis。
                // *如果使用websocket是可以直接通信H5获得用户id，则无此问题
                redis.set(REDIS_SAAS_USER_INFO + ":temp:" + preToken, new Gson().toJson(hrUser),5*60);
            }
        }

        return GraceJsonResult.ok();
    }
    /**
     * 5. 页面登录跳转
     * @param preToken
     * @return
     */
    @PostMapping("checkLogin")
    public GraceJsonResult checkLogin(String preToken) {

        if (StringUtils.isBlank(preToken))
            return GraceJsonResult.ok("");

        // 获得用户临时信息
        String userJson = redis.get(REDIS_SAAS_USER_INFO + ":temp:" + preToken);

        if (StringUtils.isBlank(userJson))
            return GraceJsonResult.errorCustom(ResponseStatusEnum.USER_NOT_EXIST_ERROR);

        // 确认执行登录后，生成saas用户的token，并且长期有效
        String saasUserToken = jwtUtils.createJWTWithPrefix(userJson, TOKEN_SAAS_PREFIX);

        // 存入用户信息，长期有效
        redis.set(REDIS_SAAS_USER_INFO + ":" + saasUserToken, userJson);

        return GraceJsonResult.ok(saasUserToken);
    }
    @GetMapping("info")
    public GraceJsonResult info(String token) {

        String userJson = redis.get(REDIS_SAAS_USER_INFO + ":" + token);
        Users saasUser = new Gson().fromJson(userJson, Users.class);

        SaasUserVO saasUserVO = new SaasUserVO();
        BeanUtils.copyProperties(saasUser, saasUserVO);

        return GraceJsonResult.ok(saasUserVO);
    }

    @PostMapping("logout")
    public GraceJsonResult logout(String token) {
        return GraceJsonResult.ok();
    }
}
