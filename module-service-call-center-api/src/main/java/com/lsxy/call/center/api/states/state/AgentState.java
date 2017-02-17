package com.lsxy.call.center.api.states.state;

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

    //TODO 坐席报道过期时间 方便测试用24小时
    public static final int REG_EXPIRE = 1000 * 60 * 60 * 24;

    @Autowired
    private RedisCacheService redisCacheService;

    public static String getPrefixed(){
        return STATE_PREFIXED_KEY;
    }

    public static String getKey(String agentId){
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

    public void delete(String agentId){
        redisCacheService.del(getKey(agentId));
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

    public Long getLastRegTime(String agentId) {
        Object obj = redisCacheService.hget(getKey(agentId),"lastRegTime");
        if(obj == null){
            return null;
        }
        return Long.parseLong(obj.toString());
    }

    public void setLastRegTime(String agentId,Long lastRegTime) {
        redisCacheService.hput(getKey(agentId),"lastRegTime",lastRegTime.toString());
    }

    public Long getLastTime(String agentId) {
        Object obj = redisCacheService.hget(getKey(agentId),"lastTime");
        if(obj == null){
            return null;
        }
        return Long.parseLong(obj.toString());
    }

    public void setLastTime(String agentId,Long lastTime) {
        redisCacheService.hput(getKey(agentId),"lastTime",lastTime.toString());
    }


    public class Model implements Serializable{

        private String extension;

        private String state;

        private Long lastRegTime;

        private Long lastTime;

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

        public Long getLastRegTime() {
            return lastRegTime;
        }

        public void setLastRegTime(Long lastRegTime) {
            this.lastRegTime = lastRegTime;
        }

        public Long getLastTime() {
            return lastTime;
        }

        public void setLastTime(Long lastTime) {
            this.lastTime = lastTime;
        }
    }
}
