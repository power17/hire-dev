package com.power.controller;

import com.power.pojo.Stu;
import com.power.service.StuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class HelloController {
    @Autowired
    private StuService stuService;

    @GetMapping("hello")
    public Object hello() {
        Stu stu = new Stu();
        stu.setAge(11);
        stu.setName("test");
        stuService.save(stu);

        return "Hello UserService~";
    }
}
