package com.lsxy.call.center.states.lock;

import com.lsxy.framework.cache.manager.RedisCacheService;

/**
 * Created by liuws on 2016/11/11.
 */
public class AgentLock extends DistributeLock {


    private static final String PREFIXED = "call.center.agentlock_";

    public static String getPrefixed(){
        return PREFIXED;
    }

    public AgentLock(RedisCacheService redis, String agentId){
        super(redis,PREFIXED+agentId);
    }
}
