package com.lsxy.app.api.gateway;

import com.lsxy.framework.core.statistics.AsbstractStatisticCounter;
import com.lsxy.framework.core.statistics.MarkField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by tandy on 16/8/9.
 */
@Component
@EnableScheduling
@Profile(value = {"local","test","development"})
public class StasticsCounter extends AsbstractStatisticCounter {
    private static final Logger logger = LoggerFactory.getLogger(StasticsCounter.class);
    //收到区域管理器响应次数
    @MarkField("收到区域管理器响应次数")
    private AtomicInteger receivedGWResponseCount = new AtomicInteger(0);
    //发送给区域管理器的消息次数
    @MarkField("发送给区域管理器的消息次数")
    private AtomicInteger sendGWRequestCount = new AtomicInteger(0);

    public AtomicInteger getReceivedGWResponseCount() {
        return receivedGWResponseCount;
    }

    public AtomicInteger getSendGWRequestCount() {
        return sendGWRequestCount;
    }

    @Scheduled(fixedDelay=10000)
    public void doOutStatistic(){
//        if(logger.isDebugEnabled()){
//            logger.debug("==============统计指标=========");
//            logger.debug("共计发送请求数:{}",this.getSendGWRequestCount().get());
//            logger.debug("共计收到请求回复数:{}",this.getReceivedGWResponseCount());
//            logger.debug("=============================\r\n\r\n");
//        }
        this.log();
    }

    @Override
    public String getStatisticName() {
        return "API网关业务指标";
    }


//    /**
//     * 重置计数器
//     */
//    public void reset() {
//        if(logger.isDebugEnabled()){
//            logger.debug("重置所有计数器");
//        }
//        receivedGWResponseCount.set(0);
//        sendGWRequestCount.set(0);
//    }
}
