package com.lsxy.call.center.api.service;

import com.lsxy.framework.core.exceptions.api.YunhuniApiException;

/**
 * Created by liuws on 2017/1/9.
 */
public interface AgentOps {

    public void reject(String ip,String appId,String name,String queueId,String userData) throws YunhuniApiException;


    public boolean callOut(String ip,String appId,String name,String from,String to,Integer maxDialSeconds,Integer maxAnswerSeconds) throws YunhuniApiException;


    public boolean callAgent(String ip,String appId,String name,String from,String enqueueXml,Integer maxDialSeconds,Integer maxAnswerSeconds) throws YunhuniApiException;

}
