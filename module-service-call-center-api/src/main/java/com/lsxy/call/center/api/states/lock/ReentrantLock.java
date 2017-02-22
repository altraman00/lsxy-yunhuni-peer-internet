package com.lsxy.call.center.api.states.lock;


import com.lsxy.call.center.api.states.lock.util.LockInfo;
import com.lsxy.framework.cache.exceptions.TransactionExecFailedException;
import com.lsxy.framework.cache.manager.RedisCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Created by liuws on 2016/11/11.
 * 使用redis 实现的分布式锁，支持可重入,lock多少次就要unlock多少次~~~
 * lock和unlock必须是同一个线程
 */
public class ReentrantLock extends DistributeLock{

    private static final Logger logger = LoggerFactory.getLogger(ReentrantLock.class);

    private RedisCacheService redis;

    // 锁的名字
    protected String lockKey;

    // 锁的有效时长(秒)
    protected long lockExpires;

    public ReentrantLock(RedisCacheService redis, String lockKey){
        this(redis,lockKey,60);
    }

    public ReentrantLock(RedisCacheService redis, String lockKey, long lockExpires){
        super(redis,lockKey,lockExpires);
        this.redis = redis;
        this.lockKey = lockKey;
        this.lockExpires = lockExpires;
    }

    /**
     * 阻塞式
     * @param useTimeout
     * @param time
     * @param unit
     * @param interrupt
     * @return
     * @throws InterruptedException
     */
    @Override
    public boolean lock(boolean useTimeout, long time, TimeUnit unit, boolean interrupt) throws InterruptedException {
        if (interrupt) {
            checkInterruption();
        }

        // 超时控制 的时间可以从本地获取, 因为这个和锁超时没有关系, 只是一段时间区间的控制
        long start = localTimeMillis();
        long timeout = unit.toMillis(time); // if !useTimeout, then it's useless

        // walkthrough
        // 1. lockKey未关联value, 直接设置lockKey, 成功获取到锁, return true
        // 2. lock 已过期, 用getset设置lockKey, 判断返回的旧的LockInfo
        // 2.1 若仍是超时的, 则成功获取到锁, return true
        // 2.2 若不是超时的, 则进入下一次循环重新开始 步骤1
        // 3. lock没过期, 判断是否是当前线程持有
        // 3.1 是, 则计数加 1, return true
        // 3.2 否, 则进入下一次循环重新开始 步骤1
        // note: 每次进入循环都检查 : 1.是否超时, 若是则return false; 2.是否检查中断(interrupt)被中断,
        // 若需检查中断且被中断, 则抛InterruptedException
        while (useTimeout ? !isTimeout(start, timeout) : true) {
            if (interrupt) {
                checkInterruption();
            }

            long lockExpireTime = serverTimeMillis() + lockExpires * 1000 + 1;// 锁超时时间
            String newLockInfoJson = LockInfo.newForCurrThread(lockExpireTime).toString();
            if (setTag(newLockInfoJson)) { // 条件能成立的唯一情况就是redis中lockKey还未关联value
                if(logger.isDebugEnabled()){
                    logger.info("{} get lock(new), lockInfo: {}", Thread.currentThread().getName(), newLockInfoJson);
                }
                locked = true;
                return true;
            }

            // value已有值, 但不能说明锁被持有, 因为锁可能expired了
            String currLockInfoJson = redis.get(lockKey);
            // 若这瞬间锁被delete了
            if (currLockInfoJson == null) {
                continue;
            }

            LockInfo currLockInfo = LockInfo.fromString(currLockInfoJson);
            // 竞争条件只可能出现在锁超时的情况, 因为如果没有超时, 线程发现锁并不是被自己持有, 线程就不会去动value
            if (isTimeExpired(currLockInfo.getExpires())) {
                // 锁超时了
                LockInfo oldLockInfo = LockInfo.fromString(redis.getAndSet(lockKey, newLockInfoJson));
                if (oldLockInfo != null && isTimeExpired(oldLockInfo.getExpires())) {
                    if(logger.isDebugEnabled()){
                        logger.info("{} get lock(new), lockInfo: {}", Thread.currentThread().getName(), newLockInfoJson);
                    }
                    locked = true;
                    return true;
                }
            } else {
                // 锁未超时, 不会有竞争情况
                if (isHeldByCurrentThread(currLockInfo)) { // 当前线程持有
                    // TODO 成功获取到锁, 设置相关标识
                    currLockInfo.setExpires(serverTimeMillis() + lockExpires * 1000 + 1); // 设置新的锁超时时间
                    currLockInfo.incCount();
                    redis.set(lockKey, currLockInfo.toString(),lockExpires);
                    if(logger.isDebugEnabled()){
                        logger.info("{} get lock(inc), lockInfo: {}", Thread.currentThread().getName(), currLockInfo);
                    }
                    locked = true;
                    return true;
                }
            }
        }
        locked = false;
        return false;
    }

    /***
     * 非阻塞式
     * @return
     */
    @Override
    public boolean lock() {
        long lockExpireTime = serverTimeMillis() + lockExpires * 1000 + 1;
        String newLockInfo = LockInfo.newForCurrThread(lockExpireTime).toString();

        if (setTag(newLockInfo)) {
            locked = true;
            return true;
        }

        String currLockInfoJson = redis.get(lockKey);
        if (currLockInfoJson == null) {
            // 再一次尝试获取
            if (setTag(newLockInfo)) {
                locked = true;
                return true;
            } else {
                locked = false;
                return false;
            }
        }

        LockInfo currLockInfo = LockInfo.fromString(currLockInfoJson);

        if (isTimeExpired(currLockInfo.getExpires())) {
            LockInfo oldLockInfo = LockInfo.fromString(redis.getAndSet(lockKey, newLockInfo));
            if (oldLockInfo != null && isTimeExpired(oldLockInfo.getExpires())) {
                locked = true;
                return true;
            }
        } else {
            if (isHeldByCurrentThread(currLockInfo)) {
                currLockInfo.setExpires(serverTimeMillis() + lockExpires * 1000 + 1);
                currLockInfo.incCount();
                redis.set(lockKey, currLockInfo.toString(),lockExpires);
                locked = true;
                return true;
            }
        }
        locked = false;
        return false;
    }

    /**
     * Queries if this lock is held by any thread.
     *
     * @return {@code true} if any thread holds this lock and {@code false}
     *         otherwise
     */
    @Override
    public boolean isLocked() {
        // 1. lockKey未关联value, return false
        // 2. 若 lock 已过期, return false, 否则 return true
        if (!locked) {// 本地locked为false, 肯定没加锁
            return false;
        }
        String json = redis.get(lockKey);
        if (json == null) {
            return false;
        }
        if (isTimeExpired(LockInfo.fromString(json).getExpires())) {
            return false;
        }
        return true;
    }

    @Override
    public void unlock() {
        // walkthrough
        // 1. 若锁过期, return
        // 2. 判断自己是否是锁的owner
        // 2.1 是, 若 count = 1, 则删除lockKey; 若 count > 1, 则计数减 1, return
        // 2.2 否, 则抛异常 IllegalMonitorStateException, reutrn
        // done, return
        LockInfo currLockInfo = LockInfo.fromString(redis.get(lockKey));
        if (isTimeExpired(currLockInfo.getExpires())) {
            return;
        }

        if (isHeldByCurrentThread(currLockInfo)) {
            if (currLockInfo.getCount() == 1) {
                redis.del(lockKey);
                if(logger.isDebugEnabled()){
                    logger.info("{} unlock(del), lockInfo: null", Thread.currentThread().getName());
                }
            } else {
                currLockInfo.decCount(); // 持有锁计数减1
                currLockInfo.setExpires(serverTimeMillis() + lockExpires * 1000 + 1);
                String json = currLockInfo.toString();
                redis.set(lockKey, json,lockExpires);
                if(logger.isDebugEnabled()){
                    logger.info("{} unlock(dec), lockInfo: {}", Thread.currentThread().getName(), json);
                }
            }
        } else {
            throw new IllegalMonitorStateException(String.format("current thread[%s] does not holds the lock", Thread.currentThread().toString()));
        }

    }

    public boolean isHeldByCurrentThread() {
        return isHeldByCurrentThread(LockInfo.fromString(redis.get(lockKey)));
    }

    // ------------------- utility methods ------------------------

    private boolean setTag(String newLockInfo){
        try {
            redis.setTransactionFlag(lockKey, newLockInfo,lockExpires);
        } catch (TransactionExecFailedException e) {
            return false;
        }
        return true;
    }
    private boolean isHeldByCurrentThread(LockInfo lockInfo) {
        return lockInfo.currentThread();
    }

    private void checkInterruption() throws InterruptedException {
        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }
    }

    private boolean isTimeExpired(long time) {
        return time < serverTimeMillis();
    }

    private boolean isTimeout(long start, long timeout) {
        // 这里拿本地的时间来比较
        return start + timeout < System.currentTimeMillis();
    }

    private long serverTimeMillis() {
        return System.currentTimeMillis();
    }

    private long localTimeMillis() {
        return System.currentTimeMillis();
    }

}