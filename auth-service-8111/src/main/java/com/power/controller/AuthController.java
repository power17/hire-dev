package com.power.controller;

import com.power.mapper.UsersMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("a")
@Slf4j
public class AuthController {
    private UsersMapper usersMapper;
    @GetMapping("hello")
    public Object Hello() {
        return "ok";
    }
}
