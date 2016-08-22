package com.lsxy.framework.core;

import com.lsxy.framework.config.Constants;
import com.lsxy.framework.config.SystemConfig;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.context.annotation.Bean;

/**
 * Created by Tandy on 2016/7/4.
 */
public abstract class AbstractSpringBootStarter {
    static {
        //将 spring boot 的默认配置文件设置为系统配置文件
        System.setProperty("spring.config.location","classpath:"+ Constants.DEFAULT_CONFIG_FILE);
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
