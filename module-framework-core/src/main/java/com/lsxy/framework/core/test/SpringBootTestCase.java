package com.lsxy.framework.core.test;

import com.lsxy.framework.config.Constants;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * Created by Tandy on 2016/7/21.
 */
public abstract class SpringBootTestCase {
    static {
        //将 spring boot 的默认配置文件设置为系统配置文件
        System.setProperty("spring.config.location","classpath:"+ Constants.DEFAULT_CONFIG_FILE);
    }
    

    @Bean(name = "systemId")
    public String systemId(){
        return getSystemId();
    }

    protected abstract String getSystemId();
}
