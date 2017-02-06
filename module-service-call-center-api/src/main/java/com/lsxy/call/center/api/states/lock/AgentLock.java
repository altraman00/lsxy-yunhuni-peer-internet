package com.lsxy.call.center.api.states.lock;

import com.lsxy.framework.cache.manager.RedisCacheService;

/**
 * Created by liuws on 2016/11/11.
 */
public class AgentLock extends DistributeLock {


    private static final String PREFIXED = "callcenter.agentlock_";

    public static String getPrefixed(){
        return PREFIXED;
    }

    public static String getKey(String agentId){
        return PREFIXED + agentId;
    }

    public AgentLock(RedisCacheService redis, String agentId){
        super(redis,getKey(agentId));
    }
}
