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
import com.lsxy.call.center.api.states.state.AgentState;
import com.lsxy.call.center.api.states.state.ExtensionState;
import com.lsxy.framework.config.Constants;
import com.lsxy.framework.core.utils.UUIDGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;
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

    @Autowired
    private ExtensionState extensionState;

    @Autowired
    private AgentState agentState;

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

        String skill_prefix = UUIDGenerator.uuid();
        for (int i = 0; i < 10; i++) {
            CallCenterAgent agent = new CallCenterAgent();
            agent.setTenantId(channel.getTenantId());
            agent.setAppId(channel.getAppId());
            agent.setChannel(channel.getId());
            agent.setNum(""+new Random(100000).nextInt());
            agent.setName("hehe"+new Random(100000).nextInt());
            agent = callCenterAgentService.save(agent);
            for (int j = 0; j < 10 ; j++) {
                AgentSkill skill = new AgentSkill();
                skill.setTenantId(channel.getTenantId());
                skill.setAppId(channel.getAppId());
                skill.setAgent(agent.getId());
                skill.setName(skill_prefix + j);
                skill.setScore(new Random().nextInt(100));
                skill.setEnabled(true);
                agentSkillService.save(skill);
            }
            String exid = UUIDGenerator.uuid();
            extensionState.setAgent(exid,agent.getId());
            extensionState.setLastRegisterTime(exid,new Date().getTime());
            extensionState.setRegisterExpires(exid,10000000);
            agentState.setExtension(agent.getId(),exid);
            agentState.setLastRegTime(agent.getId(),new Date().getTime());
            agentState.setLastTime(agent.getId(),new Date().getTime());
            agentState.setState(agent.getId(),CallCenterAgent.STATE_IDLE);
        }
        Condition condition = new Condition();
        condition.setTenantId(channel.getTenantId());
        condition.setAppId(channel.getAppId());
        condition.setChannelId(channel.getId());
        condition.setWhereExpression("(get(\""+skill_prefix+"0\") + get(\""+skill_prefix+"1\")) > 60;");
        condition.setSortExpression("get(\""+skill_prefix+"0\") + get(\""+skill_prefix+"1\");");
        condition.setPriority(10);
        condition.setQueueTimeout(33);
        condition.setFetchTimeout(45);
        condition.setRemark("条件1");
        condition = conditionService.save(condition);
        Thread.sleep(50000);
    }
}
