package com.lsxy.area.agent;

import com.lsxy.area.agent.oss.OSSClientFactory;
import com.lsxy.framework.cache.FrameworkCacheConfig;
import com.lsxy.framework.oss.FrameworkOSSConfig;
import com.lsxy.framework.oss.ali.AliOSSClientFactoryBean;
import com.lsxy.framework.rpc.FrameworkRPCConfig;
import com.lsxy.framework.rpc.api.client.ClientSessionContext;
import com.lsxy.framework.rpc.api.session.SessionContext;
import com.lsxy.framework.rpc.exceptions.RemoteServerStartException;
import com.lsxy.framework.web.web.AbstractSpringBootWebStarter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by tandy on 16/7/30.
 */
@SpringBootApplication
@ComponentScan
@Import(value = {FrameworkCacheConfig.class,FrameworkRPCConfig.class, FrameworkOSSConfig.class})
@EnableScheduling
public class AreaAgentMainClass extends AbstractSpringBootWebStarter {

    private static final String systemId = "area.agent";
    static {
        System.setProperty("systemId",systemId);
    }


    public static void main(String[] args) throws RemoteServerStartException {
        SpringApplication.run(AreaAgentMainClass.class);
    }

    @Bean
    public AliOSSClientFactoryBean getAliOSSClientFactoryBean(){
        AliOSSClientFactoryBean aliOSSClientFactoryBean = new OSSClientFactory();
        return aliOSSClientFactoryBean;
    }

    @Bean(name="sessionContext")
    public SessionContext getSessionContext(){
        SessionContext sessionContext = new ClientSessionContext();
        return sessionContext;
    }

    @Override
    public String systemId() {
        return systemId;
    }
}
