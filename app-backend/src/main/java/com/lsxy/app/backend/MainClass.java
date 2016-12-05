package com.lsxy.app.backend;

import com.alibaba.dubbo.common.Constants;
import com.lsxy.call.center.api.CallCenterApiConfig;
import com.lsxy.framework.FrameworkServiceConfig;
import com.lsxy.framework.api.FrameworkApiConfig;
import com.lsxy.framework.cache.FrameworkCacheConfig;
import com.lsxy.framework.core.AbstractSpringBootStarter;
import com.lsxy.framework.dubbo.EnableDubboConfiguration;
import com.lsxy.framework.monitor.FrameworkMonitorConfig;
import com.lsxy.framework.mq.FrameworkMQConfig;
import com.lsxy.framework.oss.FrameworkOSSConfig;
import com.lsxy.yunhuni.YunhuniServiceConfig;
import com.lsxy.yunhuni.api.YunhuniApiConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by Tandy on 2016/6/13.
 */
@SpringBootApplication()
@EnableScheduling
@Import({FrameworkApiConfig.class,FrameworkServiceConfig.class, FrameworkCacheConfig.class, YunhuniApiConfig.class, YunhuniServiceConfig.class, FrameworkMQConfig.class,
        FrameworkMonitorConfig.class,CallCenterApiConfig.class,FrameworkOSSConfig.class})
@EnableDubboConfiguration
public class MainClass extends AbstractSpringBootStarter {
    private static final String systemId = "app.backend";
    static {
        System.setProperty("systemId",systemId);
    }


    public static void main(String[] args) throws Exception {
        System.setProperty(Constants.DUBBO_PROPERTIES_KEY,"config.properties");
        SpringApplication.run(MainClass.class);
    }

    @Override
    public String systemId() {
        return systemId;
    }
}
