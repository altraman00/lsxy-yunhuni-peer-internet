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

    public AtomicInteger getReceivedGWRequestCount() {
        return receivedGWRequestCount;
    }

    public AtomicInteger getSendGWResponseCount() {
        return sendGWResponseCount;
    }
}
