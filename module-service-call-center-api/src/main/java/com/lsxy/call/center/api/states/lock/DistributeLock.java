package com.lsxy.call.center.api.states.lock;

import com.lsxy.framework.cache.exceptions.TransactionExecFailedException;
import com.lsxy.framework.cache.manager.RedisCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Created by liuws on 2016/11/11.
 * 使用redis 实现的分布式锁，不支持可重入
 * 允许lock和unlock不是同一个线程
 */
public class DistributeLock {

    private static final Logger logger = LoggerFactory.getLogger(DistributeLock.class);

    private static final String LOCK_TAG = "1";

    private String key;

    private RedisCacheService redis;

    private long expire;

    protected boolean locked;

    public DistributeLock(RedisCacheService redis,String key,long expire){
        this.redis = redis;
        this.key = key;
        this.expire = expire;
    }

    public DistributeLock(RedisCacheService redis,String key){
        this(redis,key,60);
    }

    /**
     * 阻塞式获取锁
     * @param useTimeout
     * @param time
     * @param unit
     * @param interrupt
     * @return
     * @throws InterruptedException
     */
    public boolean lock(boolean useTimeout, long time, TimeUnit unit, boolean interrupt) throws InterruptedException {
        if (interrupt) {
            checkInterruption();
        }

        long start = localTimeMillis();
        long timeout = unit.toMillis(time); // if !useTimeout, then it's useless

        while (useTimeout ? !isTimeout(start, timeout) : true) {
            if (interrupt) {
                checkInterruption();
            }
            if(lock()){
                locked = true;
                return true;
            }
        }
        locked = false;
        return false;
    }

    /**
     * 非阻塞式获取锁
     * @return
     */
    public boolean lock(){
        try {
            redis.setTransactionFlag(this.key,LOCK_TAG,this.expire);
        } catch (TransactionExecFailedException e) {
            return false;
        } catch (Throwable e) {
            logger.error("lock失败",e);
            return false;
        }
        return true;
    }


    public void unlock(){
        try{
            redis.del(this.key);
        }catch (Throwable t){
            logger.error("unlock失败",t);
        }
    }

    public boolean isLocked(){
        if (!locked) { // 本地locked为false, 肯定没加锁
            return false;
        }
        String json = redis.get(key);
        if (json == null) {
            return false;
        }
        return true;
    }

    private void checkInterruption() throws InterruptedException {
        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }
    }
    private boolean isTimeout(long start, long timeout) {
        // 这里拿本地的时间来比较
        return start + timeout < System.currentTimeMillis();
    }

    private long localTimeMillis() {
        return System.currentTimeMillis();
    }
}
