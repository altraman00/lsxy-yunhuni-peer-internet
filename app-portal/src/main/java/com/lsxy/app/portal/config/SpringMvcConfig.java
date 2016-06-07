package com.lsxy.app.portal.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

/**
 * Created by Tandy on 2016/6/6.
 */
@Configuration
public class SpringMvcConfig extends WebMvcConfigurerAdapter{

    private Log logger = LogFactory.getLog(SpringMvcConfig.class);

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        if(logger.isDebugEnabled()){
            logger.debug("添加静态资源文件映射");
        }
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");

    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setViewClass(JstlView.class);
        viewResolver.setSuffix(".jsp");
        viewResolver.setPrefix("/WEB-INF/jsp/");
        registry.viewResolver(viewResolver);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/aa");
    }
}
