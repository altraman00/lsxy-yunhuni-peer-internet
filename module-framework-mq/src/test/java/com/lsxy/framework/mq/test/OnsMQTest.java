package com.lsxy.framework.mq.test;

import com.lsxy.framework.cache.FrameworkCacheConfig;
import com.lsxy.framework.mq.FrameworkMQConfig;
import com.lsxy.framework.mq.api.MQManager;
import com.lsxy.framework.mq.api.MQService;
import com.lsxy.framework.mq.test.events.TestEvent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import javax.jms.ConnectionFactory;

/**
 * Created by tandy on 16/7/21.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Import(value={FrameworkMQConfig.class, FrameworkCacheConfig.class})
@EnableAutoConfiguration
@SpringApplicationConfiguration(OnsMQTest.class)
public class OnsMQTest {


    public static final Logger logger = LoggerFactory.getLogger(OnsMQTest.class);
    @Autowired
    private ConnectionFactory cf;

    @Autowired
    private MQService mqService;

    @Test
    public void test001(){
        Assert.notNull(cf);
        Assert.notNull(mqService);

        for (int i=0;i<100;i++){
            long startDt = System.currentTimeMillis();
            TestEvent te = new TestEvent();
            mqService.publishTopicEvent(te);

            if (logger.isDebugEnabled()){
                    logger.debug("发送消息 完成："+te.getId() + "  花费："+(System.currentTimeMillis()-startDt)+"ms");
             }

        }

    }

}
