package com.lsxy.call.center.test;

import com.lsxy.call.center.CallCenterMainClass;
import com.lsxy.framework.cache.utils.Lua;
import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.framework.config.Constants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liups on 2016/11/30.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(CallCenterMainClass.class)
public class AgentLoginTest {

    @Autowired
    private RedisCacheService redisCacheService;

    static {
        //将 spring boot 的默认配置文件设置为系统配置文件
        System.setProperty("spring.config.location","classpath:"+ Constants.DEFAULT_CONFIG_FILE);
    }

    @Test
    public void testAgentState(){
//        redisCacheService.eval(Lua.AGENTLOGIN,1,"callcenter.agent.state_testAgentState","{\"extension\" ,\"asdfa\", \"state\", \"online\", \"lastRegTime\", \"1358748549\" ,\"lastTime\" ,\"1358748549\"}");
        redisCacheService.eval(Lua.AGENTLOGIN,5,
                "callcenter.agent.state_testAgentState",
                "callcenter.extension.state_testExtState",
                "callcenter.aCs.aaaaaa",
                "callcenter.cAs.bbbbbb",
                "callcenter.cAs.cccccc",
                "extension,asdfa,lastRegTime,1358748549,lastTime,1358748549",
                "online",
                "aaaaaabbbbbbccccc",
                "123,bbbbbb,234,cccccc",
                "333,aaaaaa",
                "234,aaaaaa"
        );
    }

}
