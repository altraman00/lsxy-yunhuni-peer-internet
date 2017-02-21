package com.lsxy.area.server.service.callcenter;

import com.lsxy.framework.cache.manager.RedisCacheService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 维护agentID和callid的关系
 * 一个agentID 对应一个callid
 * Created by liuws on 2016/11/21.
 */
@Component
public class AgentIdCallReference {

    private static final Logger logger = LoggerFactory.getLogger(AgentIdCallReference.class);

    private static final String KEY_PREFIX = "callcenter.reference.call_agent_";

    private static final long EXPIRE = 60 * 60 * 24;

    @Autowired
    private RedisCacheService redisCacheService;

    private String key(String agentId){
        if(StringUtils.isBlank(agentId)){
            throw new IllegalArgumentException("agentID不能为null");
        }
        return KEY_PREFIX + agentId;
    }

    public void set(String agentId,String callId){
        redisCacheService.set(key(agentId),callId,EXPIRE);
    }

    public String get(String agentId){
        return redisCacheService.get(key(agentId));
    }

    public void clear(String agentId){
        try{
            redisCacheService.del(key(agentId));
        }catch (Throwable t){
            logger.warn("删除AgentIdCallReference失败",t);
        }
    }
}
