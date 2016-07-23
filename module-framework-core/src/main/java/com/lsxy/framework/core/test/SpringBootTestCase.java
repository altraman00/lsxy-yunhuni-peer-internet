package com.lsxy.framework.core.test;

import com.lsxy.framework.config.Constants;

/**
 * Created by Tandy on 2016/7/21.
 */

public class SpringBootTestCase {
    static {
        //将 spring boot 的默认配置文件设置为系统配置文件
        System.setProperty("spring.config.location","classpath:"+ Constants.DEFAULT_CONFIG_FILE);
    }
}
