package com.lsxy.framework.web.web;

import com.lsxy.framework.config.SystemConfig;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;

/**
 * Created by Tandy on 2016/7/4.
 */
public abstract class AbstractSpringBootStarter implements EmbeddedServletContainerCustomizer {
    @Override
    public void customize(ConfigurableEmbeddedServletContainer container) {
        container.setPort(Integer.parseInt(SystemConfig.getProperty(systemId() + ".http.port","8080")));
    }

    /**
     * 需要指定系统标识
     * portal
     * portal-api
     * ....
     * @return
     */
    public abstract  String systemId();
}
