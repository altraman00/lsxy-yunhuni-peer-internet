package com.lsxy.framework.api.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by liuws on 2016/12/13.
 */
public abstract class AsyncBatchInserter<T> extends Thread{

    private static final Logger logger = LoggerFactory.getLogger(AsyncBatchInserter.class);

    private LinkedBlockingQueue<T> queue = new LinkedBlockingQueue<>(5000);

    private volatile boolean started = false;

    public abstract BaseService<T> getBaseService();

    public void put(T t){
        try{
            queue.put(t);
        }catch (Throwable e){
            throw new RuntimeException(e);
        }
        if(!started){
            started = true;
            this.start();
        }
    }

    @Override
    public void run(){
        List<T> stack = new ArrayList<>();
        int retryTimes = 0;
        BaseService<T> baseService = getBaseService();
        while(true){
            try{
                T e = queue.poll();
                if(e != null){
                    stack.add(e);
                }
                if((e == null && stack.size() > 0) || stack.size() >= 30){
                    if(logger.isDebugEnabled()){
                        logger.info("批量入库size={}",stack.size());
                    }
                    baseService.save(stack);
                    if(logger.isDebugEnabled()){
                        logger.info("批量入库size={},完成",stack.size());
                    }
                    stack.clear();
                    retryTimes = 0;
                }
                if(queue.size() == 0){
                    try {
                        this.sleep(30);
                    } catch (InterruptedException e1) {
                        logger.error("异步批量入库线程被中断",e1);
                        Thread.currentThread().interrupt();
                    }
                }
            }catch (Throwable t){
                logger.error("批量入库失败",t);
                retryTimes++;
                if(retryTimes >= 5){
                    stack.clear();
                    retryTimes = 0;
                }else{
                    try {//sleep随机时间（1秒内）后重试
                        this.sleep(new Random().nextInt(1000));
                    } catch (InterruptedException e1) {
                        logger.error("异步批量入库线程被中断",e1);
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
    }
}
