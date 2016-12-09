package com.lsxy.call.center.states.state;

import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.framework.core.utils.BeanUtils;
import com.lsxy.framework.core.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 分机状态管理
 * Created by liuws on 2016/11/11.
 */
@Component
public class ExtensionState {
    private static final Logger logger = LoggerFactory.getLogger(ExtensionState.class);

    private static final String STATE_PREFIXED_KEY = "callcenter.extension.state_";

    @Autowired
    private RedisCacheService redisCacheService;

    public static String getPrefixed(){
        return STATE_PREFIXED_KEY;
    }

    public static String getKey(String extensionId){
        return STATE_PREFIXED_KEY + extensionId;
    }

    public Model get(String extensionId){
        Map map =redisCacheService.hgetAll(getKey(extensionId));
        if(map == null){
            return null;
        }
        Model model = new Model();
        BeanUtils.setPropertys(model,map);
        return model;
    }

    public String getAgent(String extensionId) {
        Object obj = redisCacheService.hget(getKey(extensionId),"agent");
        if(obj == null){
            return null;
        }
        return obj.toString();
    }

    public void setAgent(String extensionId,String agent) {
        redisCacheService.hput(getKey(extensionId),"agent",agent);
    }

    public void deleteAgent(String extensionId) {
        redisCacheService.hdel(getKey(extensionId),"agent");
    }

    public Long getLastRegisterTime(String extensionId) {
        Object obj = redisCacheService.hget(getKey(extensionId),"lastRegisterTime");
        if(obj == null){
            return null;
        }
        return Long.parseLong(obj.toString());
    }

    public void setLastRegisterTime(String extensionId,Long lastRegisterTime) {
        redisCacheService.hput(getKey(extensionId),"lastRegisterTime",lastRegisterTime.toString());
    }

    public Integer getRegisterExpires(String extensionId) {
        Object obj = redisCacheService.hget(getKey(extensionId),"registerExpires");
        if(obj == null){
            return null;
        }
        return Integer.parseInt(obj.toString());
    }

    public void setRegisterExpires(String extensionId,Integer registerExpires) {
        redisCacheService.hput(getKey(extensionId),"registerExpires",registerExpires.toString());
    }

    public boolean getEnable(String extensionId) {
        Object obj = redisCacheService.hget(getKey(extensionId),"enable");
        if(obj == null){
            return false;
        }
        return obj.toString().equals(Model.ENABLE_TRUE);
    }

    public void setEnable(String extensionId,String enable) {
        redisCacheService.hput(getKey(extensionId),"enable",enable);
    }

    public void setAll(String extensionId,Model model){
        Map<String,String> map = new HashMap();
        if(StringUtil.isNotBlank(model.getAgent())){
            map.put("agent", model.getAgent());
        }
        if(model.getLastRegisterTime() != null){
            map.put("lastRegisterTime",model.getLastRegisterTime().toString());
        }
        if(model.getRegisterExpires() != null){
            map.put("registerExpires",model.getRegisterExpires().toString());
        }
        if(model.getEnable() != null){
            map.put("enable",model.getEnable().toString());
        }
        redisCacheService.hputAll(getKey(extensionId),map);
    }

    public void delete(String extensionId) {
        redisCacheService.del(getKey(extensionId));
    }

    public class Model implements Serializable {
        public static final String ENABLE_TRUE = "1";
        public static final String ENABLE_FALSE = "0";

        private String agent;
        private Long lastRegisterTime;//    last_register_time   datetime comment '最近注册成功的时间。注册成功后超过register_expires无新的成功注册，视为注册超时，当离线处理。',
        private Integer registerExpires;//    register_expires     int comment '注册超过该时间后，需要重新中注册。该值应出现在SIP服务器返回的Register回复消息的Expires头域中。',
        private String enable;
        public String getAgent() {
            return agent;
        }

        public void setAgent(String agent) {
            this.agent = agent;
        }

        public Long getLastRegisterTime() {
            return lastRegisterTime;
        }

        public void setLastRegisterTime(Long lastRegisterTime) {
            this.lastRegisterTime = lastRegisterTime;
        }

        public Integer getRegisterExpires() {
            return registerExpires;
        }

        public void setRegisterExpires(Integer registerExpires) {
            this.registerExpires = registerExpires;
        }

        public String getEnable() {
            return enable;
        }

        public void setEnable(String enable) {
            this.enable = enable;
        }
    }
}
