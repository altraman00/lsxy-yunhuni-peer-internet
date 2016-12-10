package com.lsxy.call.center.service;

import com.lsxy.call.center.api.model.AgentActionLog;
import com.lsxy.call.center.api.model.CallCenterAgent;
import com.lsxy.call.center.api.service.AgentActionLogService;
import com.lsxy.call.center.dao.AgentActionLogDao;
import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * Created by liups on 2016/11/15.
 */
@Service
public class AgentActionLogServiceImpl extends AbstractService<AgentActionLog> implements AgentActionLogService {

    @Autowired
    AgentActionLogDao agentActionLogDao;

    @Override
    public BaseDaoInterface<AgentActionLog, Serializable> getDao() {
        return this.agentActionLogDao;
    }


    @Override
    @Async
    public void agentLogin(CallCenterAgent agent) {
        AgentActionLog log = new AgentActionLog(agent.getTenantId(),agent.getAppId(),agent.getChannel(),agent.getName(),AgentActionLog.ACTION_LOGIN);
        this.save(log);
    }

    @Override
    @Async
    public void agentLogout(CallCenterAgent agent) {
        AgentActionLog log = new AgentActionLog(agent.getTenantId(),agent.getAppId(),agent.getChannel(),agent.getName(),AgentActionLog.ACTION_LOGOUT);
        this.save(log);
    }
}
