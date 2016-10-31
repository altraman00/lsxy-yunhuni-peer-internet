package com.lsxy.call.center.test;

import com.lsxy.call.center.CallCenterMainClass;
import com.lsxy.call.center.api.model.AgentSkill;
import com.lsxy.call.center.api.model.AppExtension;
import com.lsxy.call.center.api.model.CallCenterAgent;
import com.lsxy.call.center.api.service.AppExtensionService;
import com.lsxy.call.center.api.service.CallCenterAgentService;
import com.lsxy.framework.config.Constants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Liuws on 2016/7/14.
 * 会议测试
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(CallCenterMainClass.class)
public class CallcenterTest {

    @Autowired
    private CallCenterAgentService callCenterAgentService;

    @Autowired
    private AppExtensionService appExtensionService;

    static {
        //将 spring boot 的默认配置文件设置为系统配置文件
        System.setProperty("spring.config.location","classpath:"+ Constants.DEFAULT_CONFIG_FILE);
    }
    @Test
    public void test(){
        callCenterAgentService.login("","",null);
    }

    @Test
    public void test2(){
        AppExtension appExtension = new AppExtension();
        appExtension.setEnabled(1);
        appExtension.setTenantId("40288ac9575612a30157561c7ff50004");
        appExtension.setAppId("40288ac957e1812e0157e18a994e0000");
        appExtension.setName("123456");
        appExtension.setType(AppExtension.TYPE_SIP);
        appExtension.setUser("123456");
        appExtension.setPassword("123456");
        appExtension.setSecret("");
        appExtension.setRegistrar("");
        appExtension.setRegisterExpires(180);
        appExtension.setLastRegisterTime(null);
        appExtension.setLastRegisterStatus(null);
        appExtensionService.register(appExtension);
    }

    @Test
    public void test3(){
        CallCenterAgent callCenterAgent = new CallCenterAgent();
        callCenterAgent.setName("test1");
        callCenterAgent.setExtentions(appExtensionService.pageList(1,10).getResult());

        List<AgentSkill> skills = new ArrayList<>();
        for(int i =0;i<10;i++){
            AgentSkill skill = new AgentSkill();
            skill.setActive(1);
            skill.setName("haha"+i);
            skills.add(skill);
        }
        callCenterAgent.setSkills(skills);
        String agentId = callCenterAgentService.login("40288ac9575612a30157561c7ff50004","40288ac957e1812e0157e18a994e0000",callCenterAgent);
    }

    @Test
    public void test4(){
        callCenterAgentService.logout(callCenterAgentService.pageList(1,1).getResult().get(0).getId());
    }
}
