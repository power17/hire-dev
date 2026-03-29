package com.power.controller.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("g")
@Slf4j
public class HelloController {
    @GetMapping("hello")
    public Object hello() {
        return "hello gateway";
    }
}
