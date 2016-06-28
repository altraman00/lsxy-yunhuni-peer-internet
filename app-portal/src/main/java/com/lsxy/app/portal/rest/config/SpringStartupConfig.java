package com.lsxy.app.portal.rest.config;

import com.lsxy.app.portal.rest.console.test.upload.UploadCommonsMultipartResolver;
import com.lsxy.app.portal.rest.security.SpringSecurityConfig;
import com.lsxy.framework.config.SystemConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Created by Tandy on 2016/6/7.
 */
@ComponentScan("com.lsxy.app.portal")
@EnableWebMvc
@Configuration
@Import(SpringSecurityConfig.class)
public class SpringStartupConfig {
    @Bean
    public CommonsMultipartResolver multipartResolver(){
        CommonsMultipartResolver resolver = new UploadCommonsMultipartResolver();
        int size = Integer.valueOf( SystemConfig.getProperty("portal.realauth.resource.upload."+SystemConfig.getProperty("portal.realauth.resource.upload.mode")+".maxsize"));
        if(size>0) {
            resolver.setMaxUploadSize(size*1024*1024);
        }
        return resolver;
    }
}
