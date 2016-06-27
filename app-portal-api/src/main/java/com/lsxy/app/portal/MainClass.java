package com.lsxy.app.portal;

import com.lsxy.framework.FrameworkServiceConfig;
import com.lsxy.framework.cache.FrameworkCacheConfig;
import com.lsxy.framework.api.tenant.FrameworkApiConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.context.annotation.Import;

/**
 * Created by Tandy on 2016/6/13.
 */
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@Import({FrameworkApiConfig.class,FrameworkServiceConfig.class, FrameworkCacheConfig.class})
public class MainClass {


    public static void main(String[] args) throws Exception {
        SpringApplication.run(MainClass.class, args);
    }
}
