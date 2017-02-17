package com.lsxy.call.center.api.service;

import com.lsxy.framework.core.exceptions.api.YunhuniApiException;

/**
 * Created by liuws on 2017/1/9.
 */
public interface ConversationOps {

    public boolean dismiss(String subaccountId, String ip, String appId, String conversationId) throws YunhuniApiException;

    public boolean setVoiceMode(String subaccountId, String ip, String appId, String conversationId, String agentId, Integer voiceMode) throws YunhuniApiException;

    public boolean inviteAgent(String subaccountId, String ip, String appId, String conversationId, String enqueue, Integer voiceMode) throws YunhuniApiException;

    public String inviteOut(String subaccountId, String ip, String appId, String conversationId, String from,
                            String to, Integer maxDial, Integer maxDuration, Integer voiceMode) throws YunhuniApiException;
}
