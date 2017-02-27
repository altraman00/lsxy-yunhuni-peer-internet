package com.lsxy.call.center.api.service;

import com.lsxy.call.center.api.model.CallCenterConversationDetail;
import com.lsxy.framework.core.exceptions.api.YunhuniApiException;

import java.util.List;

/**
 * Created by liuws on 2017/1/9.
 */
public interface AgentOps {

    public void reject(String ip,String appId,String name,String queueId,String userData) throws YunhuniApiException;


    public boolean callOut(String ip,String appId,String name,String from,String to,Integer maxDialSeconds,Integer maxAnswerSeconds) throws YunhuniApiException;


    public boolean callAgent(String ip,String appId,String name,String from,String enqueueXml,Integer maxDialSeconds,Integer maxAnswerSeconds) throws YunhuniApiException;

    public boolean setVoiceMode(String appId,String ip,String name,String conversationId,Integer mode) throws YunhuniApiException;

    public boolean enter(String appId,String ip,String name,String conversationId,Integer mode,Boolean holding) throws YunhuniApiException;

    public boolean exit(String appId,String ip,String name,String conversationId) throws YunhuniApiException;

    public List<CallCenterConversationDetail> conversations(String appId, String ip, String name) throws YunhuniApiException;
}
