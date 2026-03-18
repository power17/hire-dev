package com.power.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class HelloController {

    @PostMapping("hello")
    public Object hello() {
        return "Hello UserService~";
    }
}
