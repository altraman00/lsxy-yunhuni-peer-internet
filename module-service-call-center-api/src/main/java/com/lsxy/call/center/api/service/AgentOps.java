package com.lsxy.call.center.api.service;

import com.lsxy.framework.core.exceptions.api.YunhuniApiException;

/**
 * Created by liuws on 2017/1/9.
 */
public interface AgentOps {

    public void reject(String ip,String appId,String name,String queueId,String userData) throws YunhuniApiException;

}
