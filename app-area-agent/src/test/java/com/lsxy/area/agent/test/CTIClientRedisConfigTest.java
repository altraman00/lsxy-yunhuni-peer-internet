package com.lsxy.area.agent.test;

import com.lsxy.area.agent.AreaAgentMainClass;
import com.lsxy.area.agent.cti.CTIClientContext;
import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.framework.config.Constants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by tandy on 16/12/21.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(AreaAgentMainClass.class)
public class CTIClientRedisConfigTest {
    static {
        //将 spring boot 的默认配置文件设置为系统配置文件
        System.setProperty("spring.config.location","classpath:"+ Constants.DEFAULT_CONFIG_FILE);
    }

    @Autowired
    private RedisCacheService cacheService;

    @Autowired
    private CTIClientContext ctiClientContext;

    @Test
    public void test001(){
        HashSet serverList = new HashSet();
        Set<String> ipscNodes = cacheService.smembers("hesong:ipsc:cluster:nodeid");
        ipscNodes.forEach((snode)->{
            Object obj = cacheService.hget("hesong:ipsc:cluster:node:"+snode,"ip");
            String sServer = obj.toString();
            serverList.add(sServer.substring(0,sServer.indexOf("-")));
        });

        serverList.forEach((server)->{
            System.out.println(server);
        });
    }

    @Test
    public void test002(){
        ctiClientContext.loadConfig();
    }
}
