package com.lsxy.area.server.batch;

import com.lsxy.yunhuni.api.session.model.CallSession;
import com.lsxy.yunhuni.api.session.service.CallSessionService;
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
public class CallSessionBatchInserter extends Thread{

    private static final Logger logger = LoggerFactory.getLogger(CallSessionBatchInserter.class);

    private LinkedBlockingQueue<CallSession> queue = new LinkedBlockingQueue<>(1000);

    @Autowired
    private CallSessionService callSessionService;

    public void put(CallSession e){
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
        List<CallSession> stack = new ArrayList<>();
        while(true){
            try{
                CallSession e = queue.poll();
                if(e != null){
                    stack.add(e);
                }
                if((e == null && stack.size() > 0) || stack.size() >= 50){
                    if(logger.isDebugEnabled()){
                        logger.info("批量入库size={}",stack.size());
                    }
                    callSessionService.save(stack);
                    if(logger.isDebugEnabled()){
                        logger.info("批量入库size={},完成",stack.size());
                    }
                    stack.clear();
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
