package com.power.api.feign;

import com.power.result.GraceJsonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("work-service")
public interface WorkMicroServiceFeign {
    @PostMapping("/resume/init")
    public GraceJsonResult init(@RequestParam("userId") String userId);
}
