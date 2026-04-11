package com.power.filter;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.google.gson.Gson;
import com.power.base.BaseInfoProperties;
import com.power.result.GraceJsonResult;
import com.power.result.ResponseStatusEnum;
import com.power.utils.JWTUtils;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
@Slf4j
public class SecurityFilterJWT extends BaseInfoProperties implements GlobalFilter, Ordered {
    public static final String HEADER_USER_TOKEN = "headerUserToken";
    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private ExcludeUrlProperties excludeUrlProperties;

    private AntPathMatcher antPathMatcher = new AntPathMatcher();


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 1. 获取当前的请求路径
        String url = exchange.getRequest().getURI().getPath();
        // 2. 获得所有的需要排除校验的url list
        List<String> excludeList = excludeUrlProperties.getUrls();
        // 3. 校验并且排除excludeList
        if (excludeList != null && !excludeList.isEmpty()) {
            for (String excludeUrl : excludeList) {
                if (antPathMatcher.matchStart(excludeUrl, url)) {
                    // 如果匹配到，则直接放行，表示当前的请求url是不需要被拦截校验的
                    return chain.filter(exchange);
                }
            }
        }
        log.info(url +  ":被拦截了。。。");
        // 判断header中是否有token，对用户请求进行判断拦截
        HttpHeaders headers = exchange.getRequest().getHeaders();
        String userToken = headers.getFirst(HEADER_USER_TOKEN);
        // 判空header中的令牌
        if (StringUtils.isNotBlank(userToken)) {
            String[] tokenArr = userToken.split(JWTUtils.at);
            if (tokenArr.length < 2) {
                return renderErrorMsg(exchange, ResponseStatusEnum.UN_LOGIN);
            }

            // 获得jwt的令牌与前缀
            String prefix = tokenArr[0];
            String jwt = tokenArr[1];

            // 判断并且处理用户信息
            if (prefix.equalsIgnoreCase(TOKEN_USER_PREFIX)) {
                return dealJWT(jwt, exchange, chain, APP_USER_JSON);
            } else if (prefix.equalsIgnoreCase(TOKEN_SAAS_PREFIX)) {
                return dealJWT(jwt, exchange, chain, SAAS_USER_JSON);
            } else if (prefix.equalsIgnoreCase(TOKEN_ADMIN_PREFIX)) {
                return dealJWT(jwt, exchange, chain, ADMIN_USER_JSON);
            }

//            return dealJWT(jwt, exchange, chain, APP_USER_JSON);
        }
        return renderErrorMsg(exchange, ResponseStatusEnum.UN_LOGIN);
    }
    public ServerWebExchange setNewHeader(ServerWebExchange exchange,
                                          String headerKey,
                                          String headerValue) {
        // 重新构建新的request
        ServerHttpRequest newRequest = exchange.getRequest()
                .mutate()
                .header(headerKey, headerValue)
                .build();
        // 替换原来的request
        ServerWebExchange newExchange = exchange.mutate().request(newRequest).build();
        return newExchange;
    }
    public Mono<Void> dealJWT(String jwt, ServerWebExchange exchange, GatewayFilterChain chain, String key) {
        try {
            String userJson = jwtUtils.checkJWT(jwt);
            ServerWebExchange serverWebExchange = setNewHeader(exchange, key, userJson);
            return chain.filter(serverWebExchange); // 放行
        } catch (ExpiredJwtException e) {
            e.printStackTrace();
            return renderErrorMsg(exchange, ResponseStatusEnum.JWT_EXPIRE_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            return renderErrorMsg(exchange, ResponseStatusEnum.JWT_SIGNATURE_ERROR);
        }
    }
    /**
     * 重新包装并且返回错误信息
     * @param exchange
     * @param statusEnum
     * @return
     */
    public Mono<Void> renderErrorMsg(ServerWebExchange exchange,
                                     ResponseStatusEnum statusEnum) {
        // 1. 获得response
        ServerHttpResponse response = exchange.getResponse();

        // 2. 构建jsonResult
        GraceJsonResult jsonResult = GraceJsonResult.exception(statusEnum);

        // 3. 修改response的code为500
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);

        // 4. 设定header类型
        if (!response.getHeaders().containsKey("Content-Type"))
            response.getHeaders().add("Content-Type", MimeTypeUtils.APPLICATION_JSON_VALUE);

        // 5. 转换json并且向response中写入数据
        String resultJson = new Gson().toJson(jsonResult);
        DataBuffer dataBuffer = response
                .bufferFactory()
                .wrap(resultJson.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(dataBuffer));
    }
    @Override
    public int getOrder() {
        return 0;
    }
}
