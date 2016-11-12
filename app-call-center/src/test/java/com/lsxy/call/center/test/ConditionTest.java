package com.lsxy.call.center.test;

import com.lsxy.call.center.CallCenterMainClass;
import com.lsxy.call.center.api.model.AgentSkill;
import com.lsxy.call.center.api.model.CallCenterAgent;
import com.lsxy.call.center.api.model.Channel;
import com.lsxy.call.center.api.model.Condition;
import com.lsxy.call.center.api.service.AgentSkillService;
import com.lsxy.call.center.api.service.CallCenterAgentService;
import com.lsxy.call.center.api.service.ChannelService;
import com.lsxy.call.center.api.service.ConditionService;
import com.lsxy.framework.config.Constants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;
import java.util.Random;

/**
 * Created by liuws on 2016/11/10.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(CallCenterMainClass.class)
public class ConditionTest {

    @Autowired
    private ConditionService conditionService;

    @Autowired
    private ChannelService channelService;

    @Autowired
    private CallCenterAgentService callCenterAgentService;

    @Autowired
    private AgentSkillService agentSkillService;

    static {
        //将 spring boot 的默认配置文件设置为系统配置文件
        System.setProperty("spring.config.location","classpath:"+ Constants.DEFAULT_CONFIG_FILE);
    }

    @Test
    public void test() throws InterruptedException {
        List<Channel> cs = channelService.pageList(1,1).getResult();
        Channel channel = cs!=null && cs.size()>0 ?cs.get(0):null;
        if(channel == null){
            channel = new Channel();
            channel.setTenantId("40288aca574060400157406339080002");
            channel.setAppId("40288aca574060400157406427f20005");
            channel.setRemark("通道1");
            channel = channelService.save(channel);
        }

        CallCenterAgent agent = new CallCenterAgent();
        agent.setTenantId(channel.getTenantId());
        agent.setAppId(channel.getAppId());
        agent.setChannelId(channel.getId());
        agent.setAgentNum(""+new Random(100000).nextInt());
        agent.setAgentNo("hehe"+new Random(100000).nextInt());
        agent = callCenterAgentService.save(agent);

        for (int i = 0; i < 10 ; i++) {
            AgentSkill skill = new AgentSkill();
            skill.setTenantId(channel.getTenantId());
            skill.setAppId(channel.getAppId());
            skill.setAgent(agent.getId());
            skill.setName("haha" + i);
            skill.setLevel(new Random().nextInt(100));
            skill.setActive(1);
            agentSkillService.save(skill);
        }
        Condition condition = new Condition();
        condition.setTenantId(channel.getTenantId());
        condition.setAppId(channel.getAppId());
        condition.setChannelId(channel.getId());
        condition.setWhereExpression("(get(\"haha0\") + get(\"haha1\")) > 60;");
        condition.setSortExpression("get(\"haha0\") + get(\"haha1\");");
        condition.setPriority(10);
        condition.setQueueTimeout(33);
        condition.setFetchTimeout(45);
        condition.setRemark("条件1");
        condition = conditionService.save(condition);
        Thread.sleep(1);
        condition.setWhereExpression("(get(\"haha0\") + get(\"haha1\")) > 1000;");
        conditionService.save(condition);
        Thread.sleep(50000);
    }
}
