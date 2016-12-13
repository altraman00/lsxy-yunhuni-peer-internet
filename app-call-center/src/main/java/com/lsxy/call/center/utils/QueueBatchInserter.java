package com.lsxy.call.center.utils;

import com.lsxy.call.center.api.model.CallCenterQueue;
import com.lsxy.call.center.api.service.CallCenterQueueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by liuws on 2016/12/13.
 */
@Component
public class QueueBatchInserter extends Thread{

    private static final Logger logger = LoggerFactory.getLogger(QueueBatchInserter.class);

    private LinkedBlockingQueue<CallCenterQueue> queue = new LinkedBlockingQueue<>(1000);

    @Autowired
    private CallCenterQueueService callCenterQueueService;

    public void put(CallCenterQueue e){
        try{
            queue.put(e);
        }catch (Throwable t){
            throw new RuntimeException(t);
        }
    }

    @PostConstruct
    private void init(){
        this.start();
    }
    @Override
    public void run(){
        List<CallCenterQueue> stack = new ArrayList<>();
        while(true){
            try{
                CallCenterQueue e = queue.poll();
                if(e != null){
                    stack.add(e);
                }
                if((e == null && stack.size() > 0) || stack.size() >= 50){
                    if(logger.isDebugEnabled()){
                        logger.info("批量入库size={}",stack.size());
                    }
                    callCenterQueueService.save(stack);
                    stack.clear();
                    if(logger.isDebugEnabled()){
                        logger.info("批量入库size={},完成",stack.size());
                    }
                }
                if(e == null){
                    try {
                        this.sleep(100);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
            }catch (Throwable t){
                logger.error("批量入库失败",t);
            }
        }
    }
}
