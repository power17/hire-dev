package com.power.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;

@Slf4j
public class SMSUtilsRetry {
    public static boolean sendSMS() {
        int num = RandomUtils.nextInt(0, 4);
        log.info("随机数为 num = {}", num);
        switch (num) {
            case 0: {
                // 模拟发送异常，充实
                throw new IllegalArgumentException("参数异常");
            }
            case 1: {
                return true;
            }
            case 2: {
                throw new ArrayIndexOutOfBoundsException("数据越界...");
            }
            case 3: {
                return false;
            }
        }
        // 其他数组则触发最终的别的异常
        throw new NullPointerException("空指针，其他数值异常");
    }
}
