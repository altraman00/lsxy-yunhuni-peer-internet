package com.lsxy.call.center.dao;

import com.lsxy.call.center.api.model.AgentSkill;
import com.lsxy.framework.api.base.BaseDaoInterface;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
    @Query("update AgentSkill a set active=:active where agent=:agent and name=:name")
    public int updateActiveByAgent(@Param("active") Integer active,@Param("agent") String agent,@Param("name") String name);

}
