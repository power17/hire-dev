package com.power.exceptions;

import com.power.result.GraceJsonResult;
import com.power.result.ResponseStatusEnum;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@ControllerAdvice
public class GraceExceptionHandler {
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseBody
    public GraceJsonResult returnMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        e.printStackTrace();
        return GraceJsonResult.exception(ResponseStatusEnum.FILE_MAX_SIZE_500KB_ERROR);
    }
    @ExceptionHandler(MyCustomException.class)
    @ResponseBody
    public GraceJsonResult returnMyCustomException(MyCustomException e) {
        e.printStackTrace();
        return GraceJsonResult.exception(e.getResponseStatusEnum());
    }
}
