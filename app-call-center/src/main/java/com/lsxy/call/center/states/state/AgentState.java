package com.lsxy.call.center.states.state;

import com.lsxy.framework.cache.manager.RedisCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 坐席状态管理
 * Created by liuws on 2016/11/11.
 */
@Component
public class AgentState {
    private static final Logger logger = LoggerFactory.getLogger(AgentState.class);

    private static final String STATE_PREFIXED_KEY = "callcenter.agent.state_";

    @Autowired
    private RedisCacheService redisCacheService;

    private String getKey(String agentId){
        return STATE_PREFIXED_KEY + agentId;
    }
}
