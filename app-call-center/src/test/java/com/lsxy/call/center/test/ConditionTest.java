package com.lsxy.call.center.test;

import com.lsxy.call.center.CallCenterMainClass;
import com.lsxy.call.center.api.model.Channel;
import com.lsxy.call.center.api.model.Condition;
import com.lsxy.call.center.api.service.ChannelService;
import com.lsxy.call.center.api.service.ConditionService;
import com.lsxy.framework.config.Constants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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

    static {
        //将 spring boot 的默认配置文件设置为系统配置文件
        System.setProperty("spring.config.location","classpath:"+ Constants.DEFAULT_CONFIG_FILE);
    }

    @Test
    public void test() throws InterruptedException {
        Channel channel = new Channel();
        channel.setTenantId("40288aca574060400157406339080002");
        channel.setAppId("40288aca574060400157406427f20005");
        channel.setRemark("通道1");
        channel = channelService.save(channel);

        Condition condition = new Condition();
        condition.setTenantId("40288aca574060400157406339080002");
        condition.setAppId("40288aca574060400157406427f20005");
        condition.setChannelId(channelService.pageList(1,1).getResult().get(0).getId());
        condition.setWhereExpression("(get(\"haha0\") + get(\"haha1\")) > 60;");
        condition.setSortExpression("get(\"haha0\") + get(\"haha1\");");
        condition.setPriority(10);
        condition.setQueueTimeout(33);
        condition.setFetchTimeout(45);
        condition.setRemark("条件1");
        conditionService.save(condition);

        Thread.sleep(50000);
    }
}
