package com.lsxy.area.server;

import com.alibaba.dubbo.common.Constants;
import com.lsxy.framework.cache.FrameworkCacheConfig;
import com.lsxy.framework.core.AbstractSpringBootStarter;
import com.lsxy.framework.dubbo.EnableDubboConfiguration;
import com.lsxy.framework.mq.FrameworkMQConfig;
import com.lsxy.framework.rpc.FrameworkRPCConfig;
import com.lsxy.framework.rpc.exceptions.RemoteServerStartException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.web.EmbeddedServletContainerAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Created by tandy on 16/7/19.
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class, EmbeddedServletContainerAutoConfiguration.class})
@ComponentScan
@Import(value={FrameworkRPCConfig.class, FrameworkMQConfig.class, FrameworkCacheConfig.class})
@EnableDubboConfiguration
@EnableAsync
public class AreaServerMainClass extends AbstractSpringBootStarter {

    public static void main(String[] args) throws RemoteServerStartException {
        System.setProperty(Constants.DUBBO_PROPERTIES_KEY,"config.properties");
        SpringApplication.run(AreaServerMainClass.class);
    }

    @Override
    public String systemId() {
        return "area.server";
    }
}
