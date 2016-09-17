package com.lsxy.area.server.util;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by liuws on 2016/9/14.
 */
@Component
public class ConfUtil {

    /**最大与会数**/
    public static final int MAX_PARTS = 10;

    /**key的过期时间 秒**/
    public static final int EXPIRE = 60 * 60 * 12;

    private static final String CONF_PARTS_COUNTER_KEY_PREFIX = "conf_parts_";

    @Autowired
    private RedisTemplate redisTemplate;

    private String key(String confId){
        if(StringUtils.isBlank(confId)){
            throw new IllegalArgumentException("会议ID不能为null");
        }
        return CONF_PARTS_COUNTER_KEY_PREFIX + confId;
    }
    /**
     * 判断是否达到最大与会数
     * @param confId
     * @return
     */
    public boolean outOfParts(String confId){
        String key = key(confId);
        return redisTemplate.opsForSet().size(key) >= MAX_PARTS;
    }

    /**
     * 增加会议成员
     * @param confId
     */
    public void incrPart(String confId,String callId){
        String key = key(confId);
        redisTemplate.opsForSet().add(key,callId);
        redisTemplate.expire(key,EXPIRE, TimeUnit.SECONDS);
    }

    /**
     * 减少会议成员
     * @param confId
     */
    public void decrPart(String confId,String callId){
        String key = key(confId);
        redisTemplate.opsForSet().remove(key,callId);
        redisTemplate.expire(key,EXPIRE, TimeUnit.SECONDS);
    }

    /**
     * 获取会议成员的call_id
     * @param confId
     * @return
     */
    public List<String> getParts(String confId){
        String key = key(confId);
        return (List<String>)redisTemplate.opsForSet().members(key);
    }
}

