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
 * 管理条件下的坐席，按分值排序
 * Created by liuws on 2016/11/11.
 */
@Component
public class CAs {
    private static final Logger logger = LoggerFactory.getLogger(CAs.class);

    private static final String PREFIXED_KEY = "callcenter.cas_";

    @Autowired
    private RedisCacheService redisCacheService;

    public static String getPrefixed(){
        return PREFIXED_KEY;
    }

    public static String getKey(String conditionId) {
        return PREFIXED_KEY + conditionId;
    }


    public void delete(String conditionId){
        redisCacheService.del(getKey(conditionId));
    }

    public void add(String conditionId, String agentId, Long score) {
        redisCacheService.zadd(getKey(conditionId), agentId, score == null ? 0: score);
    }

    public void remove(String conditionId,String... agentIds){
        redisCacheService.zrem(getKey(conditionId),agentIds);
    }

    public Set<String> getAll(String conditionId){
        return redisCacheService.zRange(getKey(conditionId), 0L, -1L);
    }
    public Page getPage(String conditionId,Integer pageNo,Integer pageSize){
        Integer score1 = 0;
        Integer score2 = Integer.MAX_VALUE;
        Long count = redisCacheService.zCount(getKey(conditionId),score1.doubleValue(),score2.doubleValue());
        Integer start = (pageNo-1)*pageSize;
        Set<String> set = redisCacheService.zRange(getKey(conditionId),start.longValue(),pageSize.longValue());
        return new Page( start,count.intValue(),pageSize,new ArrayList(set));
    }
}

