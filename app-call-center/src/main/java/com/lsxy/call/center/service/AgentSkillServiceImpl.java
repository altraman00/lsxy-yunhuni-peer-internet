package com.lsxy.call.center.service;

import com.lsxy.call.center.api.model.AgentSkill;
import com.lsxy.call.center.api.service.AgentSkillService;
import com.lsxy.call.center.dao.AgentSkillDao;
import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * Created by zhangxb on 2016/10/21.
 */
@Service
@com.alibaba.dubbo.config.annotation.Service
public class AgentSkillServiceImpl extends AbstractService<AgentSkill> implements AgentSkillService {

    private static final Logger logger = LoggerFactory.getLogger(AgentSkillServiceImpl.class);

    @Autowired
    private AgentSkillDao agentSkillDao;

    @Override
    public BaseDaoInterface<AgentSkill, Serializable> getDao() {
        return agentSkillDao;
    }

}
