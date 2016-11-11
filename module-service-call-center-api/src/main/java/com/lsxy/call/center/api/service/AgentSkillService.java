package com.lsxy.call.center.api.service;

import com.lsxy.call.center.api.model.AgentSkill;
import com.lsxy.framework.api.base.BaseService;
import java.util.List;
/**
 * Created by zhangxb on 2016/10/22.
 */
public interface AgentSkillService extends BaseService<AgentSkill> {
    List<AgentSkill> findByAgent(String tenantId, String appId, String agentId);
}
