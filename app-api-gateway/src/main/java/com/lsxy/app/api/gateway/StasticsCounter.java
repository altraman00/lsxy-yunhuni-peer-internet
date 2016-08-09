package com.lsxy.app.api.gateway;

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
    //收到区域管理器响应次数
    private AtomicInteger receivedGWResponseCount = new AtomicInteger(0);
    //发送给区域管理器的消息次数
    private AtomicInteger sendGWRequestCount = new AtomicInteger(0);

    public AtomicInteger getReceivedGWResponseCount() {
        return receivedGWResponseCount;
    }

    public AtomicInteger getSendGWRequestCount() {
        return sendGWRequestCount;
    }

    @Scheduled(fixedDelay=5000)
    public void doOutStatistic(){
        if(logger.isDebugEnabled()){
            logger.debug("==============统计指标=========");
            logger.debug("共计发送请求数:{}",this.getSendGWRequestCount().get());
            logger.debug("共计收到请求回复数:{}",this.getReceivedGWResponseCount());
            logger.debug("=============================\r\n\r\n");
        }
    }
}
