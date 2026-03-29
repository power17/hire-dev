package com.power.controller.controller;

import com.power.controller.result.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("c")
@Slf4j
public class HelloController {
    @GetMapping("hello")
    public Object hello() {
        log.info("hello");
        return JsonResult.ok();
    }
}
