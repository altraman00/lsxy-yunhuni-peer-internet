package com.lsxy.area.server.service.callcenter;

import com.lsxy.framework.cache.manager.RedisCacheService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 维护call和conversation的关系
 * 一个call 可以在多个 conversation中
 * Created by liuws on 2016/11/21.
 * 后进的在前
 */
@Component
public class CallConversationService {

    private static final Logger logger = LoggerFactory.getLogger(CallConversationService.class);

    private static final String CONVERSATION_PARTS_COUNTER_KEY_PREFIX = "callcenter.call_conversations_";

    @Autowired
    private RedisCacheService redisCacheService;

    private String key(String conversation){
        if(StringUtils.isBlank(conversation)){
            throw new IllegalArgumentException("交谈ID不能为null");
        }
        return CONVERSATION_PARTS_COUNTER_KEY_PREFIX + conversation;
    }

    public long size(String callId){
        String key = key(callId);
        return redisCacheService.zsize(key);
    }

    public void incrConversation(String callId,String conversation){
        String key = key(callId);
        redisCacheService.zadd(key,conversation,System.currentTimeMillis());
        redisCacheService.expire(key,ConversationService.EXPIRE);
    }

    public void decrConversation(String callId,String conversation){
        String key = key(callId);
        redisCacheService.zrem(key,conversation);
        redisCacheService.expire(key,ConversationService.EXPIRE);
    }

    /**
     * 按时间降序获取第一个交谈
     * @param callId
     * @return
     */
    public String head(String callId){
        String key = key(callId);
        if(this.size(callId) <= 0){
            return null;
        }
        Set<String> results = redisCacheService.zReverseRange(key,0,0);
        if(results == null || results.size() == 0){
            return null;
        }
        return results.iterator().next();
    }

    /**
     * 获取交谈成员的conversations
     * @param callId
     * @return
     */
    public Set<String> getConversations(String callId){
        String key = key(callId);
        Set<String> results = null;
        try{
            results = redisCacheService.zRange(key,0,-1);
        }catch (Throwable t){
            logger.error(String.format("获取成员的交谈失败,callId=%s",callId),t);
        }
        return results;
    }

    /**
     * 弹出交谈，并清空
     */
    public Set<String> popConversations(String callId){
        String key = key(callId);
        Set<String> results = null;
        try{
            results = redisCacheService.zRange(key,0,-1);
        }catch (Throwable t){
            logger.error(String.format("获取成员的交谈失败,callId=%s",callId),t);
        }
        clear(callId);
        return results;
    }

    public void clear(String callId){
        try{
            redisCacheService.del(key(callId));
        }catch (Throwable t){
            logger.error(String.format("删除交谈缓存失败,callId=%s",callId),t);
        }
    }
}
