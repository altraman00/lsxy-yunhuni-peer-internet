package com.lsxy.area.server.test;

import com.lsxy.area.server.AreaServerMainClass;
import com.lsxy.framework.config.Constants;
import com.lsxy.framework.mq.api.MQService;
import com.lsxy.framework.mq.events.agentserver.IVRPauseActionEvent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by liuws on 2016/10/25.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(AreaServerMainClass.class)
public class MQServiceTest {

    static {
        //将 spring boot 的默认配置文件设置为系统配置文件
        System.setProperty("spring.config.location","classpath:"+ Constants.DEFAULT_CONFIG_FILE);
    }
    @Autowired
    @Qualifier("actMQService")
    private MQService mqService;

    @Test
    public void test() throws InterruptedException {
        System.out.println(mqService);
        mqService.publish(new IVRPauseActionEvent("123456789gge",1000000));
    }

}
