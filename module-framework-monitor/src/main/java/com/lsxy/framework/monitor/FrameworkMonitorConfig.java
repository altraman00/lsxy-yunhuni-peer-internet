package com.lsxy.framework.monitor;

import com.lsxy.framework.cache.FrameworkCacheConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by tandy on 16/9/29.
 */
@Configurable
@EnableScheduling
@ComponentScan
@Import(value={FrameworkCacheConfig.class})
@EnableAutoConfiguration
public class FrameworkMonitorConfig {

}
