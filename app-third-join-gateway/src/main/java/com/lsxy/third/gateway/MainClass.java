package com.lsxy.third.gateway;

import com.lsxy.framework.FrameworkServiceConfig;
import com.lsxy.framework.api.FrameworkApiConfig;
import com.lsxy.framework.cache.FrameworkCacheConfig;
import com.lsxy.framework.dubbo.EnableDubboConfiguration;
import com.lsxy.framework.monitor.FrameworkMonitorConfig;
import com.lsxy.framework.web.web.AbstractSpringBootWebStarter;
import com.lsxy.msg.MsgServiceConfig;
import com.lsxy.msg.api.MsgApiConfig;
import com.lsxy.yunhuni.YunhuniServiceConfig;
import com.lsxy.yunhuni.api.YunhuniApiConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.context.annotation.Import;

/**
 * Created by Tandy on 2016/6/13.
 */
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@Import({FrameworkApiConfig.class,FrameworkServiceConfig.class, FrameworkCacheConfig.class, YunhuniApiConfig.class, YunhuniServiceConfig.class,
        FrameworkMonitorConfig.class,MsgApiConfig.class,MsgServiceConfig.class})
@EnableDubboConfiguration
public class MainClass  extends AbstractSpringBootWebStarter {

    public static final String systemId = "third.join.gateway";

    static {
        System.setProperty("systemId",systemId);
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(MainClass.class, args);
    }

    @Override
    public String systemId() {
        return systemId;
    }
}
