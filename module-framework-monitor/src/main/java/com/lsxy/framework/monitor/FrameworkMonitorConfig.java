package com.lsxy.framework.monitor;

import com.lsxy.framework.cache.FrameworkCacheConfig;
import org.springframework.beans.factory.annotation.Configurable;
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
public class FrameworkMonitorConfig {

}
