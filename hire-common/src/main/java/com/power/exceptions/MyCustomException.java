package com.power.exceptions;

import com.power.result.ResponseStatusEnum;
/*
 *  自定义异常处理
 */
public class MyCustomException extends RuntimeException {
    private ResponseStatusEnum responseStatusEnum;

    public MyCustomException(ResponseStatusEnum responseStatusEnum)  {
        super("异常状态码为:" + responseStatusEnum.status() + "异常信息为:" + responseStatusEnum.msg());
        this.responseStatusEnum = responseStatusEnum;
    }

    public ResponseStatusEnum getResponseStatusEnum() {
        return responseStatusEnum;
    }

    public void setResponseStatusEnum(ResponseStatusEnum responseStatusEnum) {
        this.responseStatusEnum = responseStatusEnum;
    }
}
