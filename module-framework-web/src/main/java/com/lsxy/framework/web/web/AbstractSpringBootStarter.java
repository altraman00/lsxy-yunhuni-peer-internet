package com.lsxy.framework.web.web;

import com.lsxy.framework.config.Constants;
import com.lsxy.framework.config.SystemConfig;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.context.annotation.Bean;

/**
 * Created by Tandy on 2016/7/4.
 */
public abstract class AbstractSpringBootStarter implements EmbeddedServletContainerCustomizer {
    static {
        //将 spring boot 的默认配置文件设置为系统配置文件
        System.setProperty("spring.config.location","classpath:"+ Constants.DEFAULT_CONFIG_FILE);
    }
    @Override
    public void customize(ConfigurableEmbeddedServletContainer container) {

        container.setPort(Integer.parseInt(SystemConfig.getProperty(systemId() + ".http.port","8080")));
        //供系统其它位置使用
        System.setProperty("systemId",systemId());
    }

    /**
     * 需要指定系统标识
     * portal
     * portal-api
     * ....
     * @return
     */
    @Bean
    public abstract  String systemId();
}
