package com.power.controller.controller;

import com.power.controller.pojo.Stu;
import com.power.controller.result.JsonResult;
import com.power.controller.service.StuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("u")
@Slf4j
public class HelloController {
    @Autowired
    private StuService stuService;
    @Value("${server.port}")
    private String port;

    @GetMapping("hello")
    public Object hello() {
        Stu stu = new Stu();
        stu.setAge(11);
        stu.setName("test");
        stuService.save(stu);
        log.info(port);

        return JsonResult.ok();
    }
}
