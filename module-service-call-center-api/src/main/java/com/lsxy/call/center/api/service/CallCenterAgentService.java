package com.lsxy.call.center.api.service;

import com.lsxy.call.center.api.model.CallCenterAgent;
import com.lsxy.framework.api.base.BaseService;
import com.lsxy.framework.core.exceptions.api.YunhuniApiException;
import com.lsxy.framework.core.utils.Page;

import java.util.List;

/**
 * Created by zhangxb on 2016/10/22.
 */
public interface CallCenterAgentService extends BaseService<CallCenterAgent> {

    String login(CallCenterAgent agent) throws YunhuniApiException;

    void logout(String tenantId, String appId, String agentName, boolean force) throws YunhuniApiException;

    void keepAlive(String appId, String agentName) throws YunhuniApiException;

    boolean checkInSkill(String agent,String skillName,Boolean enable);

    boolean appendSkill(String tenantId,String appId,String agent,String name,Integer level,Boolean active);

    List<String> getAgentIdsByChannel(String tenantId, String appId, String channelId);

    CallCenterAgent get(String appId, String agentName) throws YunhuniApiException;

    Page getPage(String appId, Integer pageNo, Integer pageSize) throws YunhuniApiException;

    void extension(String appId, String agentName,String extensionId) throws YunhuniApiException;

    void state(String appId, String agentName, String state) throws YunhuniApiException;
}
