package com.power.controller.utils;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
@Component
@Data
@PropertySource("classpath:tecentCloud.properties")
@ConfigurationProperties(prefix = "tecent.cloud")
public class TecentCloudProperties {
    private String SecertId;
    private String SecrtKey;

}
