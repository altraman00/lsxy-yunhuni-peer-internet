package com.lsxy.call.center.api.service;

import com.lsxy.call.center.api.model.EnQueue;

/**
 * Created by liuws on 2016/11/14.
 */
public interface EnQueueService {

    public void enqueue(String tenantId, String appId, String callId, EnQueue enQueue);

}
