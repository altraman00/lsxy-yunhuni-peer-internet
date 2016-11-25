package com.lsxy.app.opensips;

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
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Created by liups on 2016/11/23.
 */
@SpringBootApplication()
@Import({FrameworkApiConfig.class,FrameworkServiceConfig.class,
        FrameworkCacheConfig.class, YunhuniApiConfig.class,
        YunhuniServiceConfig.class, FrameworkMQConfig.class, FrameworkMonitorConfig.class,CallCenterApiConfig.class})
@EnableDubboConfiguration
@EnableJpaRepositories
public class OpensipsMain extends AbstractSpringBootStarter {


    private static final String systemId = "app.opensips.proxy";
    static {
        System.setProperty("systemId",systemId);
    }

    public static void main(String[] args) {
        System.setProperty(Constants.DUBBO_PROPERTIES_KEY,"config.properties");
        SpringApplication.run(OpensipsMain.class);
    }

    @Override
    public String systemId() {
        return systemId;
    }
}
