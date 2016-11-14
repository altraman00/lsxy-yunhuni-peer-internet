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
 * 坐席状态管理
 * Created by liuws on 2016/11/11.
 */
@Component
public class AgentState {
    private static final Logger logger = LoggerFactory.getLogger(AgentState.class);

    private static final String STATE_PREFIXED_KEY = "callcenter.agent.state_";

    @Autowired
    private RedisCacheService redisCacheService;

    private String getKey(String agentId){
        return STATE_PREFIXED_KEY + agentId;
    }

    public Model get(String agentId){
        Map map =redisCacheService.hgetAll(getKey(agentId));
        if(map == null){
            return null;
        }
        Model model = new Model();
        BeanUtils.setPropertys(model,map);
        return model;
    }

    public String getExtension(String agentId) {
        Object obj = redisCacheService.hget(getKey(agentId),"extension");
        if(obj == null){
            return null;
        }
        return obj.toString();
    }

    public void setExtension(String agentId,String extension) {
        redisCacheService.hput(getKey(agentId),"extension",extension);
    }

    public String getState(String agentId) {
        Object obj = redisCacheService.hget(getKey(agentId),"state");
        if(obj == null){
            return null;
        }
        return obj.toString();
    }

    public void setState(String agentId,String state) {
        redisCacheService.hput(getKey(agentId),"state",state);
    }

    public long getLastRegTime(String agentId) {
        Object obj = redisCacheService.hget(getKey(agentId),"lastRegTime");
        if(obj == null){
            return 0L;
        }
        return Long.parseLong(obj.toString());
    }

    public void setLastRegTime(String agentId,long lastRegTime) {
        redisCacheService.hput(getKey(agentId),"lastRegTime",lastRegTime);
    }

    public long getLastTime(String agentId) {
        Object obj = redisCacheService.hget(getKey(agentId),"lastTime");
        if(obj == null){
            return 0L;
        }
        return Long.parseLong(obj.toString());
    }

    public void setLastTime(String agentId,long lastTime) {
        redisCacheService.hput(getKey(agentId),"lastTime",lastTime);
    }


    public class Model implements Serializable{

        private String extension;

        private String state;

        private long lastRegTime;

        private long lastTime;

        public String getExtension() {
            return extension;
        }

        public void setExtension(String extension) {
            this.extension = extension;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public long getLastRegTime() {
            return lastRegTime;
        }

        public void setLastRegTime(long lastRegTime) {
            this.lastRegTime = lastRegTime;
        }

        public long getLastTime() {
            return lastTime;
        }

        public void setLastTime(long lastTime) {
            this.lastTime = lastTime;
        }
    }
}
