package com.lsxy.framework.monitor;

import com.lsxy.framework.cache.manager.RedisCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.lsxy.framework.monitor.AbstractMonitor.MONITOR_EXPIRED_TIME;
import static com.lsxy.framework.monitor.AbstractMonitor.PREFIX_CACHE;

/**
 * Created by tandy on 16/9/29.
 * 监控自动更新缓存值
 */
@Component
public class MonitorTask {
    private static final Logger logger = LoggerFactory.getLogger(MonitorTask.class);


    @Autowired
    private MonitorFactory registry;

    @Autowired
    private RedisCacheService cacheService;

    /**
     * 5s更新缓存一次
     */
    @Scheduled(fixedDelay=5000)
    public void updateMonitor(){
        List<AbstractMonitor> monitors = registry.getMonitors();
        for (AbstractMonitor monitor: monitors) {
            String mName = monitor.getName();
            String mValue = monitor.fetch();
            if(logger.isDebugEnabled()){
                logger.debug("监控==>{}:{}",mName,mValue);
            }
            cacheService.set(PREFIX_CACHE  + mName,mValue,MONITOR_EXPIRED_TIME);
        }
    }


}
