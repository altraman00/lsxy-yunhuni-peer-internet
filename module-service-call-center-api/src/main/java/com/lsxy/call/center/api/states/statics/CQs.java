package com.lsxy.call.center.api.states.statics;

import com.lsxy.framework.cache.manager.RedisCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 条件下的排队，按排队时间排序
 * Created by liuws on 2016/11/11.
 */
@Component
public class CQs {
    private static final Logger logger = LoggerFactory.getLogger(CQs.class);

    private static final String PREFIXED_KEY = "callcenter.cqs_";

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

    public void add(String conditionId, String queueId) {
        String key = getKey(conditionId);
        redisCacheService.zadd(key, queueId, System.currentTimeMillis());
    }

    public boolean exists(String conditionId, String queueId){
        String key = getKey(conditionId);
        Double score = redisCacheService.zScore(key, queueId);
        return score != null;
    }

    public void remove(String conditionId,String... queueIds){
        redisCacheService.zrem(getKey(conditionId),queueIds);
    }
}
