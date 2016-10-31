package com.lsxy.call.center.api.service;

import com.lsxy.call.center.api.model.AppExtension;

/**
 * Created by zhangxb on 2016/10/22.
 */
public interface DeQueueService{

    public void success(String tenantId, String appId, String callId, AppExtension appExtension);

    public void timeout(String tenantId, String appId, String callId);

    public void fail(String tenantId, String appId, String callId,String reason);

}
