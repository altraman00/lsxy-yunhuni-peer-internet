package com.lsxy.app.oc.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by liuws on 2016/8/9.
 */
@ConditionalOnProperty(value = "global.swagger.import", havingValue = "true")
@Configuration
@EnableSwagger2
public class SwaggerConfig {

}