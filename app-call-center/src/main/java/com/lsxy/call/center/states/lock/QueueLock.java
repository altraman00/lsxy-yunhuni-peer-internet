package com.lsxy.call.center.states.lock;

import com.lsxy.framework.cache.manager.RedisCacheService;

/**
 * Created by liuws on 2016/11/11.
 */
public class QueueLock extends DistributeLock {

    private static final String PREFIXED = "call.center.queuelock_";

    public QueueLock(RedisCacheService redis, String queueId){
        super(redis,PREFIXED+queueId);
    }
}
