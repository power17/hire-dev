package com.power.api.retry;

import com.power.exceptions.GraceException;
import com.power.result.GraceJsonResult;
import com.power.result.ResponseStatusEnum;
import com.power.utils.SMSUtilsRetry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class RetryComponent {
    @Retryable(
            value = {
                    IllegalArgumentException.class,
                    ArrayIndexOutOfBoundsException.class
            },
            maxAttempts = 5,
            backoff = @Backoff(delay = 100L, multiplier = 2)
    )
    public  boolean sendSmsWithRetry() {
        log.info("当前时间 Time= {}", LocalDateTime.now());
        return SMSUtilsRetry.sendSMS();
    }
    @Recover
    public boolean recover() {
        GraceException.display(ResponseStatusEnum.SYSTEM_ARITHMETIC_BY_ZERO);
        return false;
    }

}
