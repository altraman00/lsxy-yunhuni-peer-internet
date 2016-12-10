package com.lsxy.call.center.api.service;

import com.lsxy.call.center.api.model.EnQueueResult;

/**
 * Created by zhangxb on 2016/10/22.
 */
public interface DeQueueService{

    public void success(String tenantId, String appId, String callId,String queueId, EnQueueResult result) throws Exception;

    public void timeout(String tenantId, String appId, String callId,String queueId);

    public void fail(String tenantId, String appId, String callId,String queueId,String reason);

}
