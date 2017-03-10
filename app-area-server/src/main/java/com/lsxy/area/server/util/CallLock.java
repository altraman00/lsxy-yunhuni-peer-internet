package com.lsxy.area.server.util;

import com.lsxy.call.center.api.states.lock.ReentrantLock;
import com.lsxy.framework.cache.manager.RedisCacheService;

/**
 * Created by liuws on 2016/11/11.
 */
public class CallLock extends ReentrantLock {


    private static final String PREFIXED = "as.calllock_";

    public static String getPrefixed(){
        return PREFIXED;
    }

    public static String getKey(String callId){
        return PREFIXED + callId;
    }

    public CallLock(RedisCacheService redis, String callId){
        super(redis,getKey(callId));
    }
}
