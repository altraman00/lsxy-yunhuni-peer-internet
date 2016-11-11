package com.lsxy.call.center.states;

import com.lsxy.framework.cache.manager.RedisCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 管理坐席的条件的zset
 * key callcenter.acs_agentid
 *     item ==>  cid:priority
 */
@Component
public class ACs {
    private static final Logger logger = LoggerFactory.getLogger(ACs.class);

    private static final String PREFIXED_KEY = "callcenter.acs_";

    @Autowired
    private RedisCacheService redisCacheService;

    private String getKey(String agentId){
        return PREFIXED_KEY + agentId;
    }

    public void add(String agentId,String conditionId,Integer priority){
        String key = getKey(agentId);
        redisCacheService.zadd(key,conditionId,priority);
    }

}
