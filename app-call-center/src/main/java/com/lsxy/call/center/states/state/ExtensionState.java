package com.lsxy.call.center.states.state;

import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.framework.core.utils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
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

    public Integer getLastAction(String extensionId) {
        Object obj = redisCacheService.hget(getKey(extensionId),"lastAction");
        if(obj == null){
            return null;
        }
        return Integer.parseInt(obj.toString());
    }

    public void setLastAction(String extensionId,Integer lastAction) {
        redisCacheService.hput(getKey(extensionId),"lastAction",lastAction.toString());
    }

    public Long getLastActionTime(String extensionId) {
        Object obj = redisCacheService.hget(getKey(extensionId),"lastActionTime");
        if(obj == null){
            return null;
        }
        return Long.parseLong(obj.toString());
    }

    public void setLastActionTime(String extensionId,Long lastActionTime) {
        redisCacheService.hput(getKey(extensionId),"lastActionTime",lastActionTime.toString());
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

    public Integer getLastRegisterStatus(String extensionId) {
        Object obj = redisCacheService.hget(getKey(extensionId),"lastRegisterStatus");
        if(obj == null){
            return null;
        }
        return Integer.parseInt(obj.toString());
    }

    public void setLastRegisterStatus(String extensionId,Integer lastRegisterStatus) {
        redisCacheService.hput(getKey(extensionId),"lastRegisterStatus",lastRegisterStatus.toString());
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


    public class Model implements Serializable {

        private String agent;
        private Integer lastAction;//    last_action          int comment '最近的动作',
        private Long lastActionTime;//    last_action_time     datetime comment '最近的动作发生的时间',
        private Long lastRegisterTime;//    last_register_time   datetime comment '最近注册成功的时间。注册成功后超过register_expires无新的成功注册，视为注册超时，当离线处理。',
        private Integer lastRegisterStatus;//    last_register_status int comment '最近注册状态 2xx: 成功。0表示没有任何注册',
        private Integer registerExpires;//    register_expires     int comment '注册超过该时间后，需要重新中注册。该值应出现在SIP服务器返回的Register回复消息的Expires头域中。',

        public String getAgent() {
            return agent;
        }

        public void setAgent(String agent) {
            this.agent = agent;
        }

        public Integer getLastAction() {
            return lastAction;
        }

        public void setLastAction(Integer lastAction) {
            this.lastAction = lastAction;
        }

        public Long getLastActionTime() {
            return lastActionTime;
        }

        public void setLastActionTime(Long lastActionTime) {
            this.lastActionTime = lastActionTime;
        }

        public Long getLastRegisterTime() {
            return lastRegisterTime;
        }

        public void setLastRegisterTime(Long lastRegisterTime) {
            this.lastRegisterTime = lastRegisterTime;
        }

        public Integer getLastRegisterStatus() {
            return lastRegisterStatus;
        }

        public void setLastRegisterStatus(Integer lastRegisterStatus) {
            this.lastRegisterStatus = lastRegisterStatus;
        }

        public Integer getRegisterExpires() {
            return registerExpires;
        }

        public void setRegisterExpires(Integer registerExpires) {
            this.registerExpires = registerExpires;
        }
    }
}
