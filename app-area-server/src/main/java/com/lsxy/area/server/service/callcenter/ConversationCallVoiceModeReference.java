package com.lsxy.area.server.service.callcenter;

import com.lsxy.call.center.api.model.CallCenterConversationMember;
import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.framework.core.utils.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 维护呼叫在交谈中的收放音模式
 * Created by liuws on 2016/11/21.
 */
@Component
public class ConversationCallVoiceModeReference {

    private static final Logger logger = LoggerFactory.getLogger(ConversationCallVoiceModeReference.class);

    private static final String KEY_PREFIX = "callcenter.reference.conversation_%s_call_%s";

    private static final long EXPIRE = 60 * 60 * 24;

    @Autowired
    private RedisCacheService redisCacheService;

    private String key(String conversationId,String callId){
        if(StringUtils.isBlank(conversationId)){
            throw new IllegalArgumentException("conversationId不能为null");
        }
        if(StringUtils.isBlank(callId)){
            throw new IllegalArgumentException("callId不能为null");
        }
        return String.format(KEY_PREFIX,conversationId,callId);
    }

    /**
     *
     * @param conversationId
     * @param callId
     * @param voiceMode
     */
    public void set(String conversationId,String callId,Integer voiceMode){
        if(voiceMode == null){
            return;
        }
        redisCacheService.set(key(conversationId,callId),voiceMode.toString(),EXPIRE);
    }

    public Integer get(String conversationId,String callId){
        String result = redisCacheService.get(key(conversationId,callId));
        if(StringUtil.isEmpty(result)){
            return CallCenterConversationMember.MODE_DEFAULT;
        }
        return Integer.parseInt(result);
    }

    public void clear(String conversationId,String callId){
        try{
            redisCacheService.del(key(conversationId,callId));
        }catch (Throwable t){
            logger.warn("删除ConversationCallVoiceModeReference失败",t);
        }
    }
}
