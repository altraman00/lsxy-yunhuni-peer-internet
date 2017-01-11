package com.lsxy.call.center.api.service;

import com.lsxy.call.center.api.model.EnQueueResult;

/**
 * Created by zhangxb on 2016/10/22.
 */
public interface DeQueueService{

    public void success(String tenantId, String appId, String callId,String queueId,String queueType, EnQueueResult result,String conversationId) throws Exception;

    public void timeout(String tenantId, String appId, String callId,String queueId,String queueType);

    public void fail(String tenantId, String appId, String callId,String queueId,String queueType,String reason);

}
