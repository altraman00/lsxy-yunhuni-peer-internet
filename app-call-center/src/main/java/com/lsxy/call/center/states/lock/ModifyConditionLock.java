package com.lsxy.call.center.states.lock;

import com.lsxy.framework.cache.manager.RedisCacheService;

/**
 * Created by liuws on 2016/11/11.
 */
public class ModifyConditionLock extends DistributeLock {


    private static final String PREFIXED = "call.center.modifyconditionlock_";

    public static String getPrefixed(){
        return PREFIXED;
    }

    public ModifyConditionLock(RedisCacheService redis, String agentId){
        super(redis,PREFIXED+agentId,1800);
    }
}
