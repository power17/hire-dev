package com.power.controller;


import com.power.result.GraceJsonResult;
import com.power.service.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("resume")
public class ResumeController {
    @Autowired
    private ResumeService resumeService;
    @PostMapping("init")
    public GraceJsonResult init(@RequestParam("userId") String userId) {
        resumeService.initResume(userId);
        return  GraceJsonResult.ok();
    }
}
