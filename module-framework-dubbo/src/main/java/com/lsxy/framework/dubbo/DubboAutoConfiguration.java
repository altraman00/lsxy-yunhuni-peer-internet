package com.lsxy.framework.dubbo;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.lsxy.framework.config.SystemConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

/**
 * dubbo auto configuration
 *
 * @author linux_china
 */
@Configuration
@EnableConfigurationProperties(DubboProperties.class)
public class DubboAutoConfiguration {
    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private DubboProperties properties;

    @Autowired
    private String systemId;

    @Bean
    @ConditionalOnMissingBean
    public ApplicationConfig dubboApplicationConfig() {
        System.setProperty("dubbo.application.logger","slf4j");
        ApplicationConfig appConfig = new ApplicationConfig();
        appConfig.setName(systemId);
        return appConfig;
    }

    @Bean
    @ConditionalOnMissingBean
    public ProtocolConfig dubboProtocolConfig() {
        ProtocolConfig protocolConfig = new ProtocolConfig();

        protocolConfig.setName(properties.getProtocol());

        String port = SystemConfig.getProperty(systemId+".dubbo.port");
        if(StringUtils.isEmpty(port)){
            port = properties.getPort().toString();
        }
        protocolConfig.setPort(Integer.parseInt(port));
        protocolConfig.setThreads(properties.getThreads());
        return protocolConfig;
    }

    @Bean
    @ConditionalOnMissingBean
    public RegistryConfig dubboRegistryConfig() {
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setAddress(properties.getRegistry());
        return registryConfig;
    }

    @Bean
    public DubboOperationEndpoint dubboOperationEndpoint() {
        return new DubboOperationEndpoint();
    }

    @Bean
    public DubboHealthIndicator dubboHealthIndicator() {
        return new DubboHealthIndicator();
    }

    @Bean
    public DubboEndpoint dubboEndpoint() {
        return new DubboEndpoint();
    }

    @Bean
    public DubboMetrics dubboConsumerMetrics() {
        return new DubboMetrics();
    }

}
