package com.lsxy.call.center.utils;

import com.lsxy.call.center.states.statics.CQs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by liuws on 2016/12/13.
 */
@Component
public class CQSBatchInserter extends Thread{

    private static final Logger logger = LoggerFactory.getLogger(CQSBatchInserter.class);

    private LinkedBlockingQueue<Entity> queue = new LinkedBlockingQueue<>(5000);

    @Autowired
    private CQs cQs;

    public void put(String conditionId,String queueId){
        try{
            queue.put(new Entity(conditionId,queueId));
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
        while(true){
            try{
                Entity e = queue.poll();
                if(e == null){
                    try {
                        this.sleep(10);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    continue;
                }
                long start = System.currentTimeMillis();
                cQs.add(e.getConditionId(), e.getQueueId());
                logger.info("s2={}",(System.currentTimeMillis() - start));
            }catch (Throwable t){
                logger.error("add CQS失败",t);
            }
        }
    }

    public static class Entity{

        private String conditionId;
        private String queueId;

        public Entity(){}
        public Entity(String conditionId,String queueId){
            this.conditionId = conditionId;
            this.queueId = queueId;
        }
        public String getConditionId() {
            return conditionId;
        }

        public void setConditionId(String conditionId) {
            this.conditionId = conditionId;
        }

        public String getQueueId() {
            return queueId;
        }

        public void setQueueId(String queueId) {
            this.queueId = queueId;
        }
    }
}
