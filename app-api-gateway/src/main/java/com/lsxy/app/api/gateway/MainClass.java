package com.lsxy.app.api.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;

/**
 * Created by Tandy on 2016/6/13.
 */
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
//@Import({FrameworkApiConfig.class,FrameworkServiceConfig.class, FrameworkCacheConfig.class})
public class MainClass {


    public static void main(String[] args) throws Exception {
        SpringApplication.run(MainClass.class, args);
    }
}
