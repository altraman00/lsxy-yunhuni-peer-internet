package com.lsxy.call.center.states;

import com.lsxy.framework.cache.manager.RedisCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by liuws on 2016/11/11.
 */
@Component
public class CAs {
    private static final Logger logger = LoggerFactory.getLogger(CAs.class);

    private static final String PREFIXED_KEY = "callcenter.cas_";

    @Autowired
    private RedisCacheService redisCacheService;

    private String getKey(String conditionId) {
        return PREFIXED_KEY + conditionId;
    }

    public void add(String conditionId, String agentId, long score) {
        String key = getKey(conditionId);
        redisCacheService.zadd(key, agentId, score);
    }
}

