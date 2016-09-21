package com.lsxy.app.portal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Created by tandy on 16/9/21.
 * 暗码配置
 */

@Configuration
public class HideCodeConfig {

    @Bean
    @Profile("test")
    public String hideCode(){
        return "!@#$%^&*()__)O(*&^%$#@Q!klm";
    }
}
