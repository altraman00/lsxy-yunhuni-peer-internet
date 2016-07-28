package com.lsxy.app.portal.config;

import com.lsxy.app.portal.security.SpringSecurityConfig;
import com.lsxy.framework.cache.FrameworkCacheConfig;
import com.lsxy.framework.cache.FrameworkCacheConfigNotSpringBoot;
import com.lsxy.framework.oss.FrameworkOSSConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Created by Tandy on 2016/6/7.
 */
@ComponentScan({"com.lsxy.app.portal","com.lsxy.framework.mail"})
@EnableWebMvc
@Configuration
@Import({SpringSecurityConfig.class, FrameworkCacheConfigNotSpringBoot.class, FrameworkOSSConfig.class})
public class SpringStartupConfig {

}
