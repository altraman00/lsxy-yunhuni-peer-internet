package com.lsxy.call.center.api.service;

import com.lsxy.call.center.api.model.EnQueue;

/**
 * Created by liuws on 2016/11/14.
 */
public interface EnQueueService {

    public void lookupAgent(String tenantId, String appId,String subaccountId,String num, String callId, EnQueue enQueue,String queueType,String conversationId);

    public void lookupQueue(String tenantId, String appId,String conditionId, String agentId);

}
