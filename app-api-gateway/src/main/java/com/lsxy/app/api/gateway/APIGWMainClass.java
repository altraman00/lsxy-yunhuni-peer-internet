package com.lsxy.app.api.gateway;

import com.lsxy.framework.FrameworkServiceConfig;
import com.lsxy.framework.api.FrameworkApiConfig;
import com.lsxy.framework.cache.FrameworkCacheConfig;
import com.lsxy.framework.dubbo.EnableDubboConfiguration;
import com.lsxy.framework.mq.FrameworkMQConfig;
import com.lsxy.framework.web.web.AbstractSpringBootWebStarter;
import com.lsxy.yunhuni.YunhuniServiceConfig;
import com.lsxy.yunhuni.api.YunhuniApiConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Created by Tandy on 2016/6/13.
 */
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@Import({FrameworkApiConfig.class,FrameworkServiceConfig.class,
        FrameworkCacheConfig.class, YunhuniApiConfig.class, YunhuniServiceConfig.class, FrameworkMQConfig.class})
@EnableAsync
@EnableWebMvc
@EnableDubboConfiguration
public class APIGWMainClass extends AbstractSpringBootWebStarter {

    public static void main(String[] args) throws Exception {

        SpringApplication.run(APIGWMainClass.class, args);
    }


    @Override
    public String systemId() {
        return "api.gateway";
    }
}
