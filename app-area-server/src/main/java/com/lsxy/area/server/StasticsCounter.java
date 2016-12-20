package com.lsxy.area.server;

import com.lsxy.framework.core.statistics.AsbstractStatisticCounter;
import com.lsxy.framework.core.statistics.MarkField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by tandy on 16/8/9.
 */
//@Component
//@EnableScheduling
//@Profile(value = {"test"})
public class StasticsCounter extends AsbstractStatisticCounter {

    private static final Logger logger = LoggerFactory.getLogger(StasticsCounter.class);
    
    //收到API网关  请求消息次数
    @MarkField("收到API网关请求消息次数")
    private AtomicInteger receivedGWRequestCount = new AtomicInteger(0);

    //发送给API网关消息次数
    @MarkField("发送给API网关消息次数")
    private AtomicInteger sendGWResponseCount = new AtomicInteger(0);

    @MarkField("发送给区域请求次数")
    private AtomicInteger sendAreaNodeRequestCount = new AtomicInteger(0);

    @MarkField("收到区域的请求次数")
    private AtomicInteger receivedAreaNodeRequestCount = new AtomicInteger(0);

    @MarkField("发送SYS.CALL指令给区域的次数")
    private AtomicInteger sendAreaNodeSysCallCount = new AtomicInteger(0);

    @MarkField("收到CTI事件次数")
    private AtomicInteger receivedAreaNodeCTIEventCount = new AtomicInteger(0);

    @MarkField("收到CTI incoming事件次数")
    private AtomicInteger receivedAreaNodeInComingEventCount = new AtomicInteger(0);

    @MarkField("发送[接听]指令给区域的次数")
    private AtomicInteger sendAreaNodeSysAnswerCount = new AtomicInteger(0);

    @MarkField("发送[挂机]指令给区域的次数")
    private AtomicInteger sendAreaNodeSysDropCount = new AtomicInteger(0);

    public AtomicInteger getSendAreaNodeSysAnswerCount() {
        return sendAreaNodeSysAnswerCount;
    }

    public AtomicInteger getSendAreaNodeSysDropCount() {
        return sendAreaNodeSysDropCount;
    }

    public AtomicInteger getSendAreaNodeSysCallCount() {
        return sendAreaNodeSysCallCount;
    }

    public AtomicInteger getReceivedAreaNodeCTIEventCount() {
        return receivedAreaNodeCTIEventCount;
    }

    public AtomicInteger getReceivedAreaNodeInComingEventCount() {
        return receivedAreaNodeInComingEventCount;
    }

    public AtomicInteger getReceivedGWRequestCount() {
        return receivedGWRequestCount;
    }

    public AtomicInteger getSendGWResponseCount() {
        return sendGWResponseCount;
    }

    public AtomicInteger getSendAreaNodeRequestCount() {
        return sendAreaNodeRequestCount;
    }

    public AtomicInteger getReceivedAreaNodeRequestCount() {
        return receivedAreaNodeRequestCount;
    }

    @Scheduled(fixedDelay=10000)
    public void doOutStatistic(){
//        if(logger.isDebugEnabled()){
//            logger.debug("==============统计指标=========");
//            logger.debug("收到API网关请求消息次数:{}",receivedGWRequestCount.get());
//            logger.debug("发送给API网关消息次数:{}",sendGWResponseCount.get());
//            logger.debug("发送给区域请求次数:{}",sendAreaNodeRequestCount.get());
//            logger.debug("收到区域的请求次数:{}",receivedAreaNodeRequestCount.get());
//            logger.debug("发送SYS.CALL指令给区域的次数:{}",sendAreaNodeSysCallCount.get());
//            logger.debug("收到CTI事件次数:{}",receivedAreaNodeCTIEventCount.get());
//            logger.debug("收到CTI incoming事件次数:{}",receivedAreaNodeInComingEventCount.get());
//            logger.debug("发送[接听]指令给区域的次数:{}",sendAreaNodeSysAnswerCount.get());
//            logger.debug("发送[挂机]指令给区域的次数:{}",sendAreaNodeSysDropCount.get());
//            logger.debug("=============================\r\n\r\n");
//        }
        this.log();
    }

    @Override
    public String getStatisticName() {
        return "区域管理器业务指标";
    }

//    /**
//     * 重置统计计数
//     */
//    public void reset() {
//        receivedGWRequestCount.set(0);
//        sendGWResponseCount.set(0);
//        sendAreaNodeRequestCount.set(0);
//        receivedAreaNodeRequestCount.set(0);
//        sendAreaNodeSysCallCount.set(0);
//        receivedAreaNodeCTIEventCount.set(0);
//        receivedAreaNodeInComingEventCount.set(0);
//        sendAreaNodeSysAnswerCount.set(0);
//        sendAreaNodeSysDropCount.set(0);
//    }
}
