package com.power.api;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Aspect
public class ServiceLogAspect {
    @Around("execution(* com.power.service.impl..*.*(..))")
    public Object recordTimeLog(ProceedingJoinPoint joinPoint) throws Throwable {
        long begin = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();

        String point = joinPoint.getTarget().getClass().getName()
                + "."
                + joinPoint.getSignature().getName();

        long end = System.currentTimeMillis();
        long takeTime = end - begin;

        if (takeTime > 3000) {
            log.error("执行方法：{} 执行时间太长了，耗费了{}毫秒", point, takeTime);
        } else if (takeTime > 2000) {
            log.warn("执行方法：{} 执行时间稍微有点长，耗费了{}毫秒", point, takeTime);
        } else {
            log.info("执行方法：{} 执行时间，耗费了{}毫秒", point, takeTime);
        }

        return proceed;
    }


}
