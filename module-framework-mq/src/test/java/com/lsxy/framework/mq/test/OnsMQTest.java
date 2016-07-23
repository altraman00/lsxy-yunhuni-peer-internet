package com.lsxy.framework.mq.test;

import com.lsxy.framework.cache.FrameworkCacheConfig;
import com.lsxy.framework.core.test.SpringBootTestCase;
import com.lsxy.framework.mq.FrameworkMQConfig;
import com.lsxy.framework.mq.api.MQManager;
import com.lsxy.framework.mq.api.MQService;
import com.lsxy.framework.mq.test.events.TestEvent;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.config.SimpleJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * Created by tandy on 16/7/21.
 */

@Import(value={FrameworkMQConfig.class, FrameworkCacheConfig.class})
@SpringApplicationConfiguration(value=OnsMQTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@EnableAutoConfiguration
@EnableJms
public class OnsMQTest extends SpringBootTestCase{

    public static final Logger logger = LoggerFactory.getLogger(OnsMQTest.class);

//
//    @Autowired
//    private ConnectionFactory cf;

    @Autowired
    private MQService mqService;

    @Autowired
    private JmsTemplate jmsTemplate;

//    @Autowired
//    private Receiver receiver;

    @Test
    public void test001() throws InterruptedException {
//        Assert.notNull(cf);
//        Assert.notNull(mqService);


//        while(true){
//            jmsTemplate.send("test_yunhuni_topic_framework_tenant", session -> {
//                return session.createTextMessage("ping!:"+ new Date().getTime());
//            });
//            System.out.println("send message ok");
//            TimeUnit.SECONDS.sleep(1);
//        }

        while(true){
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            long startDt = System.currentTimeMillis();
            TestEvent te = new TestEvent();
            mqService.publishTopicEvent(te);

            if (logger.isDebugEnabled()){
                    logger.debug("发送消息 完成："+te.getId() + "  花费："+(System.currentTimeMillis()-startDt)+"ms");
             }

        }

    }

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(OnsMQTest.class);
    }

}
