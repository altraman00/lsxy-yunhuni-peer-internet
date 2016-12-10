package com.lsxy.area.agent;

import com.lsxy.framework.core.statistics.AsbstractStatisticCounter;
import com.lsxy.framework.core.statistics.MarkField;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by tandy on 16/8/9.
 */
//@Component
//@EnableScheduling
//@Profile(value = {"test"})
public class StasticsCounter extends AsbstractStatisticCounter {

    @MarkField(" 收到区域管理器请求次数")
    private AtomicInteger receivedAreaServerRequestCount = new AtomicInteger(0);
    @MarkField("发送给区域管理器的请求次数")
    private AtomicInteger sendAreaServerRequestCount = new AtomicInteger(0);

    @MarkField("收到CTI事件次数")
    private AtomicInteger receivedCTIEventCount = new AtomicInteger(0);
    @MarkField("发出CTI请求次数")
    private AtomicInteger sendCTIRequestCount = new AtomicInteger(0);

    @MarkField("收到应答指令次数")
    private AtomicInteger receivedCTIAnswerCount = new AtomicInteger(0);
    @MarkField("收到挂机指令次数")
    private AtomicInteger receivedCTIDropCount = new AtomicInteger(0);

    @MarkField("收到INCOMING事件次数")
    private AtomicInteger receivedCTIIncomingEventCount = new AtomicInteger(0);
    @MarkField("收到释放事件次数")
    private AtomicInteger receivedCTIReleaseEventCount= new AtomicInteger(0);
    @MarkField("收到拨号失败事件")
    private AtomicInteger receivedCTIDialFailedEventCount= new AtomicInteger(0);
    @MarkField("收到拨号超时事件")
    private AtomicInteger receivedCTIDialTimeOutEventCount= new AtomicInteger(0);
    @MarkField("收到拨号完成事件次数")
    private AtomicInteger receivedCTIDialCompleteEventCount= new AtomicInteger(0);

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
//        if(logger.isDebugEnabled()){
//            logger.debug("==============统计指标=========");
//            logger.debug("收到区域管理器请求次数:{}",this.receivedAreaServerRequestCount.get());
//            logger.debug("发送给区域管理器的请求次数:{}",this.sendAreaServerRequestCount.get());
//            logger.debug("收到CTI事件次数:{}",this.receivedCTIEventCount.get());
//            logger.debug("发出CTI请求次数:{}",this.sendCTIRequestCount.get());
//            logger.debug("收到应答指令次数:{}",this.receivedCTIAnswerCount.get());
//            logger.debug("收到挂机指令次数:{}",this.receivedCTIDropCount.get());
//            logger.debug("收到INCOMING事件次数:{}",this.receivedCTIIncomingEventCount.get());
//            logger.debug("收到拨号完成事件次数:{}",this.receivedCTIDialCompleteEventCount.get());
//            logger.debug("收到拨号超时事件:{}",this.receivedCTIDialTimeOutEventCount.get());
//            logger.debug("收到拨号失败事件:{}",this.receivedCTIDialFailedEventCount.get());
//            logger.debug("收到释放事件次数:{}",this.receivedCTIReleaseEventCount.get());
//
//            logger.debug("=============================\r\n\r\n");
//        }
        this.log();
    }

//    /**
//     * 重置计数器
//     */
//    public void reset() {
//        receivedAreaServerRequestCount.set(0);
//        sendAreaServerRequestCount.set(0);
//        receivedCTIEventCount.set(0);
//        sendCTIRequestCount.set(0);
//        receivedCTIAnswerCount.set(0);
//        receivedCTIDropCount.set(0);
//        receivedCTIIncomingEventCount.set(0);
//        receivedCTIDialCompleteEventCount.set(0);
//        receivedCTIDialTimeOutEventCount.set(0);
//        receivedCTIDialFailedEventCount.set(0);
//        receivedCTIReleaseEventCount.set(0);
//    }

    public AtomicInteger getReceivedCTIDialCompleteEventCount() {
        return receivedCTIDialCompleteEventCount;
    }

    public AtomicInteger getReceivedCTIReleaseEventCount() {
        return receivedCTIReleaseEventCount;
    }

    public AtomicInteger getReceivedCTIDialTimeOutEventCount() {
        return receivedCTIDialTimeOutEventCount;
    }

    public AtomicInteger getReceivedCTIDialFailedEventCount() {
        return receivedCTIDialFailedEventCount;
    }

    @Override
    public String getStatisticName() {
        return "区域代理业务指标";
    }
}
