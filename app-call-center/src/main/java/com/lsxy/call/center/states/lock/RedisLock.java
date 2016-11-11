package com.lsxy.call.center.states.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * Created by liuws on 2016/11/11.
 */
public abstract class RedisLock implements Lock{

    protected abstract String setKey();

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public void lock(){}

    @Override
    public boolean tryLock() {
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    public void unlock(){}

    @Override
    public Condition newCondition() {
        return null;
    }


}
