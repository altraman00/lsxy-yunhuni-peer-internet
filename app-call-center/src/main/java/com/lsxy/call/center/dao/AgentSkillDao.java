package com.lsxy.call.center.dao;

import com.lsxy.call.center.api.model.AgentSkill;
import com.lsxy.framework.api.base.BaseDaoInterface;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

import java.io.Serializable;

/**
 * 呼叫中心系统
 * Created by zhangxb on 2016/10/22.
 */
public interface AgentSkillDao extends BaseDaoInterface<AgentSkill, Serializable> {

    @Modifying
    @Query("delete from AgentSkill where agent=:agent")
    public int deleteByAgent(@Param("agent") String agent);

    @Modifying
    @Query("update AgentSkill a set enabled=:enabled where agent=:agent and name=:name")
    public int updateActiveByAgent(@Param("enabled") Boolean enabled,@Param("agent") String agent,@Param("name") String name);

    public List<AgentSkill> findByTenantIdAndAppIdAndAgentAndEnabled(String tenantId,String appId,String agent,Boolean enabled);

    List<AgentSkill> findByAgent(String agentId);

    List<AgentSkill> findByAgentIn(List<String> agentIds);

    void deleteByAgentAndName(String agent, String name);
}
