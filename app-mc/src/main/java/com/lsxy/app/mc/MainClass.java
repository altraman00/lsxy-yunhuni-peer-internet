package com.lsxy.app.mc;

import com.lsxy.framework.FrameworkServiceConfig;
import com.lsxy.framework.api.FrameworkApiConfig;
import com.lsxy.framework.cache.FrameworkCacheConfig;
import com.lsxy.framework.monitor.FrameworkMonitorConfig;
import com.lsxy.framework.web.web.AbstractSpringBootWebStarter;
import com.lsxy.yunhuni.YunhuniServiceConfig;
import com.lsxy.yunhuni.api.YunhuniApiConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.context.annotation.Import;

/**
 * Created by tandy on 16/11/19.
 */
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@Import({FrameworkApiConfig.class,FrameworkServiceConfig.class,YunhuniApiConfig.class, YunhuniServiceConfig.class, FrameworkCacheConfig.class,
        FrameworkMonitorConfig.class})
public class MainClass extends AbstractSpringBootWebStarter {
    @Override
    public String systemId() {
        return systemId;
    }

    public static final String systemId = "app.mc";
    static {
        System.setProperty("systemId",systemId);
    }

    public static void main(String[] args) {
        SpringApplication.run(MainClass.class, args);
    }
}
