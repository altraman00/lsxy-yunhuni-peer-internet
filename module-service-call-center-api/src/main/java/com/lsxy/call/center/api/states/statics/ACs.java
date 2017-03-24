package com.lsxy.call.center.api.states.statics;

import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.framework.core.utils.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Set;

/**
 * 管理坐席的条件，按优先级排序
 * key callcenter.acs_agentid
 *     item ==>  cid:priority
 */
@Component
public class ACs {
    private static final Logger logger = LoggerFactory.getLogger(ACs.class);

    private static final String PREFIXED_KEY = "callcenter.acs_";

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

    public void add(String agentId,String conditionId,Integer priority){
        redisCacheService.zadd(getKey(agentId),conditionId,priority == null?0:priority);
    }

    public void remove(String agentId,String... conditionIds){
        redisCacheService.zrem(getKey(agentId),conditionIds);
    }

    public Set<String> getAll(String agentId){
        return redisCacheService.zRange(getKey(agentId), 0L, -1L);
    }
    public Page getPage(String agentId, Integer pageNo, Integer pageSize){
        Integer score1 = 0;
        Integer score2 = Integer.MAX_VALUE;
        Long count = redisCacheService.zCount(getKey(agentId),score1.doubleValue(),score2.doubleValue());
        Integer start = (pageNo-1)*pageSize;
        Set<String> set = redisCacheService.zRange(getKey(agentId),start.longValue(),pageSize.longValue());
        return new Page( start,count.intValue(),pageSize,new ArrayList(set));
    }
}
