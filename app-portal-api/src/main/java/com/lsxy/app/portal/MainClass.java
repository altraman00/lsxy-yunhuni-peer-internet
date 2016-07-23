package com.lsxy.app.portal;

import com.lsxy.framework.FrameworkServiceConfig;
import com.lsxy.framework.api.FrameworkApiConfig;
import com.lsxy.framework.cache.FrameworkCacheConfig;
import com.lsxy.framework.sms.FrameworkSmsConfig;
import com.lsxy.framework.web.web.AbstractSpringBootStarter;
import com.lsxy.yunhuni.api.YunhuniApiConfig;
import com.lsxy.yunhuni.YunhuniServiceConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.util.Properties;

/**
 * Created by Tandy on 2016/6/13.
 */
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@Import({FrameworkApiConfig.class,FrameworkServiceConfig.class, FrameworkCacheConfig.class, YunhuniApiConfig.class, YunhuniServiceConfig.class,
        FrameworkSmsConfig.class})
public class MainClass extends AbstractSpringBootStarter{

    public static final String systemId = "portal.api";

    public static void main(String[] args) throws Exception {
        SpringApplication.run(MainClass.class, args);
    }

    @Override
    public String systemId() {
        return systemId;
    }
}
