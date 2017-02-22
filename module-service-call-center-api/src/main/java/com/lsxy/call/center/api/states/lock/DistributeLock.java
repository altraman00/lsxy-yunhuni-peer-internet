package com.lsxy.call.center.api.states.lock;

import com.lsxy.framework.cache.exceptions.TransactionExecFailedException;
import com.lsxy.framework.cache.manager.RedisCacheService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by liuws on 2016/11/11.
 */
public class DistributeLock {

    private final static Log logger = LogFactory.getLog(DistributeLock.class);

    private static final String LOCK_TAG = "1";

    private String key;

    private RedisCacheService redis;

    private long expire;

    public DistributeLock(RedisCacheService redis,String key,long expire){
        this.redis = redis;
        this.key = key;
        this.expire = expire;
    }

    public DistributeLock(RedisCacheService redis,String key){
        this(redis,key,60);
    }

    public boolean lock(){
        try {
            redis.setTransactionFlag(this.key,LOCK_TAG,this.expire);
        } catch (TransactionExecFailedException e) {
            return false;
        } catch (Throwable e) {
            logger.error(e);
            return false;
        }
        return true;
    }

    public void unlock(){
        try{
            redis.del(this.key);
        }catch (Throwable t){
            logger.error(t);
        }
    }
}
