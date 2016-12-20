package com.lsxy.call.center;

import com.alibaba.dubbo.common.Constants;
import com.lsxy.call.center.api.CallCenterApiConfig;
import com.lsxy.framework.FrameworkServiceConfig;
import com.lsxy.framework.api.FrameworkApiConfig;
import com.lsxy.framework.cache.FrameworkCacheConfig;
import com.lsxy.framework.core.AbstractSpringBootStarter;
import com.lsxy.framework.dubbo.EnableDubboConfiguration;
import com.lsxy.framework.monitor.FrameworkMonitorConfig;
import com.lsxy.framework.mq.FrameworkMQConfig;
import com.lsxy.yunhuni.YunhuniServiceConfig;
import com.lsxy.yunhuni.api.YunhuniApiConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.EmbeddedServletContainerAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Created by tandy on 16/7/19.
 */
@SpringBootApplication(exclude = {EmbeddedServletContainerAutoConfiguration.class})
@ComponentScan
@Import({FrameworkApiConfig.class,FrameworkServiceConfig.class,
        FrameworkCacheConfig.class, YunhuniApiConfig.class,
        YunhuniServiceConfig.class, FrameworkMQConfig.class, FrameworkMonitorConfig.class, CallCenterApiConfig.class,CallCenterServiceConfig.class})
@EnableDubboConfiguration
@EnableAsync
public class CallCenterMainClass extends AbstractSpringBootStarter {


    private static final String systemId = "app.cc";
    static {
        System.setProperty("systemId",systemId);
    }

    public static void main(String[] args) {
        System.setProperty(Constants.DUBBO_PROPERTIES_KEY,"config.properties");
        SpringApplication.run(CallCenterMainClass.class);
    }

    @Override
    public String systemId() {
        return systemId;
    }
}
