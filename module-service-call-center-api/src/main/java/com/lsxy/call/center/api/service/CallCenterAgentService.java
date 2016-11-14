package com.lsxy.call.center.api.service;

import com.lsxy.call.center.api.model.AgentSkill;
import com.lsxy.call.center.api.model.AppExtension;
import com.lsxy.call.center.api.model.CallCenterAgent;
import com.lsxy.framework.api.base.BaseService;

import java.util.List;

/**
 * Created by zhangxb on 2016/10/22.
 */
public interface CallCenterAgentService extends BaseService<CallCenterAgent> {

    public String login(String tenantId, String appId, CallCenterAgent callCenterAgent, List<AppExtension> extentions, List<AgentSkill> skills);

    public boolean logout(String agentId);

    public boolean checkInSkill(String agent,String skillName,Integer active);

    public boolean appendSkill(String tenantId,String appId,String agent,String name,Integer level,Integer active);

    public List<String> getAgentIdsByChannel(String tenantId, String appId, String channelId);
}
