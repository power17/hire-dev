package com.power.api.interceptor;

import com.power.exceptions.MyCustomException;
import com.power.result.GraceJsonResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GraceInterceptorHandler {
    @ExceptionHandler(MyCustomException.class)
    @ResponseBody
    public GraceJsonResult returnMyCustomException (MyCustomException e) {
        e.printStackTrace();
        return GraceJsonResult.exception(e.getResponseStatusEnum());
    }
    // 参数异常拦截
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public GraceJsonResult returnMethodArgumentNotValidException (MethodArgumentNotValidException e) {
        BindingResult result = e.getBindingResult();
        Map<String, String> errorMap = getErrors(result);
        return GraceJsonResult.errorMap(errorMap);
    }

    public Map<String, String> getErrors(BindingResult result) {
        Map<String, String> map = new HashMap<>();
        List<FieldError> fieldErrorList = result.getFieldErrors();
        for(FieldError fe: fieldErrorList) {
            String feild = fe.getField();
            String msg = fe.getDefaultMessage();
            map.put(feild, msg);
        }
        return map;
    }

}
