package com.lsxy.call.center.states.lock;

import com.lsxy.framework.cache.manager.RedisCacheService;

/**
 * Created by liuws on 2016/11/11.
 */
public class ExtensionLock extends DistributeLock {

    private static final String PREFIXED = "callcenter.extensionlock_";

    public static String getPrefixed(){
        return PREFIXED;
    }

    public static String getKey(String extensionId){
        return PREFIXED + extensionId;
    }

    public ExtensionLock(RedisCacheService redis, String extensionId){
        super(redis,getKey(extensionId));
    }
}
