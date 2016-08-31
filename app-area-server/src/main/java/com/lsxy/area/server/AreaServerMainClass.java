package com.lsxy.area.server;

import com.alibaba.dubbo.common.Constants;
import com.lsxy.framework.FrameworkServiceConfig;
import com.lsxy.framework.api.FrameworkApiConfig;
import com.lsxy.framework.cache.FrameworkCacheConfig;
import com.lsxy.framework.core.AbstractSpringBootStarter;
import com.lsxy.framework.dubbo.EnableDubboConfiguration;
import com.lsxy.framework.jmx.FrameworkJMXConfig;
import com.lsxy.framework.mq.FrameworkMQConfig;
import com.lsxy.framework.rpc.FrameworkRPCConfig;
import com.lsxy.framework.rpc.api.RPCMessage;
import com.lsxy.framework.rpc.api.session.SelectSessionPolicy;
import com.lsxy.framework.rpc.api.session.Session;
import com.lsxy.framework.rpc.api.session.SessionContext;
import com.lsxy.framework.rpc.api.server.ServerSessionContext;
import com.lsxy.framework.rpc.exceptions.RemoteServerStartException;
import com.lsxy.yunhuni.YunhuniServiceConfig;
import com.lsxy.yunhuni.api.YunhuniApiConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.EmbeddedServletContainerAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Created by tandy on 16/7/19.
 */
@SpringBootApplication(exclude = {EmbeddedServletContainerAutoConfiguration.class})
@ComponentScan
@Import(value={FrameworkRPCConfig.class, FrameworkMQConfig.class, FrameworkCacheConfig.class,FrameworkApiConfig.class,
        FrameworkServiceConfig.class, YunhuniApiConfig.class, YunhuniServiceConfig.class,
        FrameworkJMXConfig.class})
@EnableDubboConfiguration
@EnableAsync
public class AreaServerMainClass extends AbstractSpringBootStarter {

    @Autowired
    private AreaServerSelectSessionPolicy selectSessionPolicy;

    public static void main(String[] args) throws RemoteServerStartException {
        System.setProperty(Constants.DUBBO_PROPERTIES_KEY,"config.properties");
        SpringApplication.run(AreaServerMainClass.class);
    }

    @Bean(name = "sessionContext")
    public SessionContext getSessionContext(){
        SessionContext sessionContext = new ServerSessionContext();
        sessionContext.setSelectSessionPolicy(selectSessionPolicy);
        return sessionContext;
    }

    @Override
    public String systemId() {
        return "area.server";
    }
}
