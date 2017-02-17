package com.lsxy.call.center.api.states.statics;

import com.lsxy.framework.cache.manager.RedisCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 指定坐席的排队，按优先级排序
 * key callcenter.acs_agentid
 *     item ==>  cid:priority
 */
@Component
public class AQs {

    private static final Logger logger = LoggerFactory.getLogger(AQs.class);

    private static final String PREFIXED_KEY = "callcenter.aqs_";

    @Autowired
    private RedisCacheService redisCacheService;

    public static String getPrefixed(){
        return PREFIXED_KEY;
    }

    public static String getKey(String agentId){
        return PREFIXED_KEY + agentId;
    }

    public void delete(String agentId){
        redisCacheService.del(getKey(agentId));
    }

    public void add(String agentId,String queueId,Integer priority){
        redisCacheService.zadd(getKey(agentId),queueId,priority == null?0:priority);
    }

    public boolean exists(String agentId, String queueId){
        String key = getKey(agentId);
        Double score = redisCacheService.zScore(key, queueId);
        return score != null;
    }

    public void remove(String agentId,String... queueIds){
        redisCacheService.zrem(getKey(agentId),queueIds);
    }

    public Set<String> getAll(String agentId){
        return redisCacheService.zRange(getKey(agentId), 0L, -1L);
    }

}
