package com.lsxy.app.portal.config;

import com.lsxy.app.portal.security.SpringSecurityConfig;
import com.lsxy.framework.cache.FrameworkCacheConfigNotSpringBoot;
import com.lsxy.framework.monitor.FrameworkMonitorConfig;
import com.lsxy.framework.oss.FrameworkOSSConfig;
import org.springframework.context.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Created by Tandy on 2016/6/7.
 */
@ComponentScan({"com.lsxy.app.portal","com.lsxy.framework.mail"})
@EnableWebMvc
@Configuration
@PropertySource("classpath:/config.properties")
@Import({SpringSecurityConfig.class, FrameworkCacheConfigNotSpringBoot.class, FrameworkOSSConfig.class, FrameworkMonitorConfig.class})
public class SpringStartupConfig {

}
