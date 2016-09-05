package com.lsxy.framework.mq;

import com.alibaba.fastjson.annotation.JSONField;
import com.lsxy.framework.core.statistics.AsbstractStatisticCounter;
import com.lsxy.framework.core.statistics.MarkField;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by tandy on 16/8/10.
 */
@Component
@Profile(value={"no"})
@EnableScheduling
public class MQStasticCounter extends AsbstractStatisticCounter {

    //发送消息计数器
    @MarkField("发送消息次数")
    private AtomicInteger sendMQCount = new AtomicInteger(0);

    @MarkField("收到消息次数")
    private AtomicInteger receivedMQCount = new AtomicInteger(0);


    public AtomicInteger getSendMQCount() {
        return sendMQCount;
    }

    public AtomicInteger getReceivedMQCount() {
        return receivedMQCount;
    }

    public static void main(String[] args) {
        MQStasticCounter m = new MQStasticCounter();
        m.log();
    }

    @Scheduled(fixedDelay=15000)
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
        return "MQ消息统计指标";
    }
}
