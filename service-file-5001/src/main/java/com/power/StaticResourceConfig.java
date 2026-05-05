package com.power;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;
import java.nio.file.Paths;

@Configuration
public class StaticResourceConfig extends WebMvcConfigurationSupport {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String dir = Paths.get("").toAbsolutePath().toString();
        registry.addResourceHandler("/static/**")
                .addResourceLocations("file:" + File.separator+dir + File.separator  + "upload" + File.separator);
    }
}
