package com.lsxy.call.center.states.lock;

import com.lsxy.framework.cache.manager.RedisCacheService;

/**
 * Created by liuws on 2016/11/11.
 */
public class ModifyConditionLock extends DistributeLock {


    private static final String PREFIXED = "callcenter.modifyconditionlock_";

    public static String getPrefixed(){
        return PREFIXED;
    }

    public static String getKey(String conditionId){
        return PREFIXED + conditionId;
    }

    public ModifyConditionLock(RedisCacheService redis, String conditionId){
        super(redis,getKey(conditionId),1800);
    }
}
