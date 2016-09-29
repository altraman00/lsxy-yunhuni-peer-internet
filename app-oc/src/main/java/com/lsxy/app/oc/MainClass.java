package com.lsxy.app.oc;

import com.lsxy.app.oc.config.SwaggerConfig;
import com.lsxy.framework.FrameworkServiceConfig;
import com.lsxy.framework.api.FrameworkApiConfig;
import com.lsxy.framework.cache.FrameworkCacheConfig;
import com.lsxy.framework.monitor.FrameworkMonitorConfig;
import com.lsxy.framework.mq.FrameworkMQConfig;
import com.lsxy.framework.oss.FrameworkOSSConfig;
import com.lsxy.framework.sms.FrameworkSmsConfig;
import com.lsxy.framework.web.web.AbstractSpringBootWebStarter;
import com.lsxy.yunhuni.YunhuniServiceConfig;
import com.lsxy.yunhuni.api.YunhuniApiConfig;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.orm.jpa.JpaVendorAdapter;

/**
 * Created by Tandy on 2016/6/13.
 */
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@Import({FrameworkApiConfig.class,FrameworkServiceConfig.class, FrameworkCacheConfig.class, YunhuniApiConfig.class, YunhuniServiceConfig.class,
        FrameworkSmsConfig.class, FrameworkMQConfig.class, SwaggerConfig.class, FrameworkOSSConfig.class, FrameworkMonitorConfig.class})
public class MainClass extends AbstractSpringBootWebStarter{


    public static final String systemId = "oc.api";
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
