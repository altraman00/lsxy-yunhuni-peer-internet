package com.lsxy.call.center.service;

import com.lsxy.call.center.api.model.AgentSkill;
import com.lsxy.call.center.api.service.AgentSkillService;
import com.lsxy.call.center.dao.AgentSkillDao;
import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhangxb on 2016/10/21.
 */
@Service
@com.alibaba.dubbo.config.annotation.Service
public class AgentSkillServiceImpl extends AbstractService<AgentSkill> implements AgentSkillService {

    private static final Logger logger = LoggerFactory.getLogger(AgentSkillServiceImpl.class);

    @Autowired
    private AgentSkillDao agentSkillDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public BaseDaoInterface<AgentSkill, Serializable> getDao() {
        return agentSkillDao;
    }


    @Override
    public List<AgentSkill> findEnabledByAgent(String agentId){
        String sql = "select name,score from db_lsxy_bi_yunhuni.tb_bi_call_center_agent_skill " +
                "where agent='"+agentId+"' and enabled = 1 ";

        return jdbcTemplate.query(sql, new Object[]{}, new BeanPropertyRowMapper<AgentSkill>(AgentSkill.class));
    }

    @Override
    public void deleteByAgent(String agentId) {
        agentSkillDao.deleteByAgent(agentId);
    }

    @Override
    public List<AgentSkill> findAllByAgent(String agentId) {
        return agentSkillDao.findByAgent(agentId);
    }

    @Override
    public List<AgentSkill> findAllByAgents(List<String> agentIds) {
        return agentSkillDao.findByAgentIn(agentIds);
    }

    @Override
    public void deleteByAgentAndName(String agent, String name) {
        agentSkillDao.deleteByAgentAndName(agent,name);
    }


}
