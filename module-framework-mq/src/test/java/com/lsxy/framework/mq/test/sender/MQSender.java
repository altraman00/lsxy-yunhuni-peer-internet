package com.lsxy.framework.mq.test.sender;

import com.lsxy.framework.cache.FrameworkCacheConfig;
import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.framework.core.test.SpringBootTestCase;
import com.lsxy.framework.mq.FrameworkMQConfig;
import com.lsxy.framework.mq.api.MQService;
import com.lsxy.framework.mq.events.test.EchoEvent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import java.util.concurrent.TimeUnit;

/**
 * Created by tandy on 16/7/21.
 */

@Import(value={FrameworkMQConfig.class, FrameworkCacheConfig.class})
@SpringApplicationConfiguration(value=MQSender.class)
@RunWith(SpringJUnit4ClassRunner.class)
@EnableAutoConfiguration
@EnableJms
@ComponentScan
public class MQSender extends SpringBootTestCase{

    public static final Logger logger = LoggerFactory.getLogger(MQSender.class);

    @Autowired
    private RedisCacheService cacheService;


    @Autowired
    private MQService mqService;

    @Test
    public void test001() throws InterruptedException {
        int count = 1000;
        cacheService.del("echo");
        for(int i=0;i<count;i++){
            EchoEvent echoEvent = new EchoEvent(1);
            logger.info("send msg:{}" , echoEvent);
            mqService.publish(echoEvent);
        }
        logger.info("消息发送完毕,稍等10s");
        TimeUnit.SECONDS.sleep(10);
        String cacheEcho = cacheService.get("echo");
        logger.info("缓存值:{}",cacheEcho);
        Assert.isTrue(cacheEcho.equals("1000"));
    }

    @Override
    protected String getSystemId() {
        return "test001";
    }
}
