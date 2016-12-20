package com.lsxy.call.center.api.service;

import com.lsxy.call.center.api.model.AgentActionLog;
import com.lsxy.call.center.api.model.CallCenterAgent;
import com.lsxy.framework.api.base.BaseService;

/**
 * Created by liups on 2016/11/15.
 */
public interface AgentActionLogService extends BaseService<AgentActionLog> {
    void agentLogin(CallCenterAgent agent);
    void agentLogout(CallCenterAgent agent);
}
