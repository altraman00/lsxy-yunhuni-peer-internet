package com.lsxy.area.agent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by tandy on 16/8/9.
 */
@Component
@EnableScheduling
public class StasticsCounter {
    private static final Logger logger = LoggerFactory.getLogger(StasticsCounter.class);

    //收到区域管理器请求次数
    private AtomicInteger receivedAreaServerRequestCount = new AtomicInteger(0);
    //发送给区域管理器的请求次数
    private AtomicInteger sendAreaServerRequestCount = new AtomicInteger(0);

    //收到CTI事件次数
    private AtomicInteger receivedCTIEventCount = new AtomicInteger(0);
    //发出CTI请求次数
    private AtomicInteger sendCTIRequestCount = new AtomicInteger(0);

    //收到应答指令次数
    private AtomicInteger receivedCTIAnswerCount = new AtomicInteger(0);
    //收到挂机指令次数
    private AtomicInteger receivedCTIDropCount = new AtomicInteger(0);

    //收到INCOMING事件次数
    private AtomicInteger receivedCTIIncomingEventCount = new AtomicInteger(0);

    public AtomicInteger getReceivedCTIIncomingEventCount() {
        return receivedCTIIncomingEventCount;
    }

    public AtomicInteger getReceivedCTIAnswerCount() {
        return receivedCTIAnswerCount;
    }

    public AtomicInteger getReceivedCTIDropCount() {
        return receivedCTIDropCount;
    }

    public AtomicInteger getReceivedCTIEventCount() {
        return receivedCTIEventCount;
    }

    public AtomicInteger getSendCTIRequestCount() {
        return sendCTIRequestCount;
    }


    public AtomicInteger getReceivedAreaServerRequestCount() {
        return receivedAreaServerRequestCount;
    }

    public AtomicInteger getSendAreaServerRequestCount() {
        return sendAreaServerRequestCount;
    }

    @Scheduled(fixedDelay=10000)
    public void doOutStatistic(){
        if(logger.isDebugEnabled()){
            logger.debug("==============统计指标=========");
            logger.debug("收到区域管理器请求次数:{}",this.receivedAreaServerRequestCount.get());
            logger.debug("发送给区域管理器的请求次数:{}",this.sendAreaServerRequestCount.get());
            logger.debug("收到CTI事件次数:{}",this.receivedCTIEventCount.get());
            logger.debug("发出CTI请求次数:{}",this.sendCTIRequestCount.get());
            logger.debug("收到应答指令次数:{}",this.receivedCTIAnswerCount.get());
            logger.debug("收到挂机指令次数:{}",this.receivedCTIDropCount.get());
            logger.debug("收到INCOMING事件次数:{}",this.receivedCTIIncomingEventCount.get());
            logger.debug("=============================\r\n\r\n");
        }
    }
}
