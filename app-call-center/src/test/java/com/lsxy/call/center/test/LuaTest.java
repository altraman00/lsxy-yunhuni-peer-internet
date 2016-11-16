package com.lsxy.call.center.test;

import com.lsxy.call.center.CallCenterMainClass;
import com.lsxy.call.center.states.lock.AgentLock;
import com.lsxy.call.center.states.state.AgentState;
import com.lsxy.call.center.states.state.ExtensionState;
import com.lsxy.call.center.states.statics.CAs;
import com.lsxy.call.center.utils.Lua;
import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.framework.config.Constants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by liuws on 2016/11/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(CallCenterMainClass.class)
public class LuaTest {

    @Autowired
    private RedisCacheService redisCacheService;

    static {
        //将 spring boot 的默认配置文件设置为系统配置文件
        System.setProperty("spring.config.location","classpath:"+ Constants.DEFAULT_CONFIG_FILE);
    }


    @Test
    public void test(){
        String script = "local acs = redis.call('ZREVRANGE',KEYS[1],0,-1)\n" +
                "local result = nil\n" +
                "for i=0,#acs do  \n" +
                "    local agent_state = redis.call('HGET',KEYS[2]..acs[i+1],'hehe')\n" +
                "\tresult = agent\n" +
                "\tbreak\n" +
                "end \n" +
                "return result";

        System.out.println(redisCacheService.eval(script,2, CAs.getKey("40288ae25865c111015865c146d1000b"), AgentState.getPrefixed()));
    }

    @Test
    public void test1(){
        System.out.println(System.currentTimeMillis());
        System.out.println(redisCacheService.eval(Lua.LOOKUPAGENT,4,
                CAs.getKey("40288ae25867b181015867b1a9e7000c"),
                AgentState.getPrefixed(), ExtensionState.getPrefixed(), AgentLock.getPrefixed()));
    }
    @Test
    public void  test2(){
        System.out.println(redisCacheService.eval("return redis.call('HGETALL','callcenter.agent.state_40288ae2586715c601586715f5cd0000')"));;
    }

    public static void main(String[] args) {
        System.out.println(System.currentTimeMillis());
    }
}
