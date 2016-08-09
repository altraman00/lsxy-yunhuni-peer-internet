package com.lsxy.area.server;

import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by tandy on 16/8/9.
 */
@Component
public class StasticsCounter {

    //收到API网关  请求消息次数
    private AtomicInteger receivedGWRequestCount = new AtomicInteger(0);
    //发送给API网关消息次数
    private AtomicInteger sendGWResponseCount = new AtomicInteger(0);

    //发送给区域请求次数
    private AtomicInteger sendAreaNodeRequestCount = new AtomicInteger(0);

    //收到区域的请求次数
    private AtomicInteger receivedAreaNodeRequestCount = new AtomicInteger(0);

    //发送SYS.CALL指令给区域的次数
    private AtomicInteger sendAreaNodeSysCallCount = new AtomicInteger(0);

    //收到CTI事件次数
    private AtomicInteger sendAreaNodeCTIEventCount = new AtomicInteger(0);

    //收到CTI incoming事件次数
    private AtomicInteger sendAreaNodeInComingEventCount= new AtomicInteger(0);


    public AtomicInteger getSendAreaNodeSysCallCount() {
        return sendAreaNodeSysCallCount;
    }

    public AtomicInteger getSendAreaNodeCTIEventCount() {
        return sendAreaNodeCTIEventCount;
    }

    public AtomicInteger getSendAreaNodeInComingEventCount() {
        return sendAreaNodeInComingEventCount;
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
}
