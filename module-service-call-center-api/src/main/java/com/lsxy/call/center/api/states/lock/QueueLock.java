package com.lsxy.call.center.api.states.lock;

import com.lsxy.framework.cache.manager.RedisCacheService;

/**
 * Created by liuws on 2016/11/11.
 */
public class QueueLock extends DistributeLock {

    private static final String PREFIXED = "callcenter.queuelock_";

    public static String getPrefixed(){
        return PREFIXED;
    }

    public static String getKey(String queueId){
        return PREFIXED + queueId;
    }

    public QueueLock(RedisCacheService redis, String queueId){
        super(redis,getKey(queueId));
    }
}
