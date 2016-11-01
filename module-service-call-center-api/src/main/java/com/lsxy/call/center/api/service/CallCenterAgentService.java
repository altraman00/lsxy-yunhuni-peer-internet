package com.lsxy.call.center.api.service;

import com.lsxy.call.center.api.model.CallCenterAgent;
import com.lsxy.framework.api.base.BaseService;

/**
 * Created by zhangxb on 2016/10/22.
 */
public interface CallCenterAgentService extends BaseService<CallCenterAgent> {

    public String login(String tenantId,String appId,CallCenterAgent callCenterAgent);

    public boolean logout(String agentId);

    public boolean checkInSkill(String agent,String skillName,Integer active);

    public boolean appendSkill(String tenantId,String appId,String agent,String name,Integer level,Integer active);
}
