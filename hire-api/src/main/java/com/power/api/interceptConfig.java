package com.power.api;


import com.power.api.interceptor.JwtUserInterceptor;
import com.power.api.interceptor.SMSInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class interceptConfig implements WebMvcConfigurer {
    @Bean
    public SMSInterceptor smsInterceptor() {
        return new SMSInterceptor();
    }
    @Bean
    public JwtUserInterceptor jwtUserInterceptor() {
        return new JwtUserInterceptor();
    }
    //  注册拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(smsInterceptor())
                .addPathPatterns("/passport/getSmsCode");
        registry.addInterceptor(jwtUserInterceptor())
                .addPathPatterns("/**");
    }
}
