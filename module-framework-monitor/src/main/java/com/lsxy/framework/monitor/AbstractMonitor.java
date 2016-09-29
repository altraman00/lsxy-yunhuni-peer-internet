package com.lsxy.framework.monitor;


import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.framework.core.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by tandy on 16/9/29.
 */
public abstract class AbstractMonitor {

    public static final String PREFIX_CACHE="monitor_"+System.getProperty("systemId") +"_" + StringUtil.getHostName();

    //监控过期时间 默认5S
    public static final long MONITOR_EXPIRED_TIME=5;

    @Autowired
    private RedisCacheService cacheService;

    public abstract String getName() ;

    public abstract String fetch();

    /**
     * 获取监控中的缓存值
     * @return
     */
    public String getValue(){
        return cacheService.get(PREFIX_CACHE+getName());
    }
}
