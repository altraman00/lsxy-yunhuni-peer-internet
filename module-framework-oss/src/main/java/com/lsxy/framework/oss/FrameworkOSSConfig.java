package com.lsxy.framework.oss;

import com.lsxy.framework.oss.ali.AliOSSClientFactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by Tandy on 2016/7/14.
 */
@ComponentScan
public class FrameworkOSSConfig {
    @Bean
    @ConditionalOnMissingBean
    public AliOSSClientFactoryBean getAliOSSClientFactoryBean(){
        AliOSSClientFactoryBean aliOSSClientFactoryBean = new AliOSSClientFactoryBean();
        return aliOSSClientFactoryBean;
    }
}
