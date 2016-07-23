package com.lsxy.framework.mq.test.sender;

import com.lsxy.framework.cache.FrameworkCacheConfig;
import com.lsxy.framework.core.test.SpringBootTestCase;
import com.lsxy.framework.mq.FrameworkMQConfig;
import com.lsxy.framework.mq.api.MQService;
import com.lsxy.framework.mq.test.events.TestEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.jms.annotation.EnableJms;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * Created by tandy on 16/7/21.
 */

@Import(value={FrameworkMQConfig.class, FrameworkCacheConfig.class})
@SpringApplicationConfiguration(value=MQSender.class)
//@RunWith(SpringJUnit4ClassRunner.class)
@EnableAutoConfiguration
@EnableJms
@ComponentScan
public class MQSender extends SpringBootTestCase{

    public static final Logger logger = LoggerFactory.getLogger(MQSender.class);

//
//    @Autowired
//    private ConnectionFactory cf;

    @Autowired
    private MQService mqService;

//    @Autowired
//    private JmsTemplate jmsTemplate;

//    @Autowired
//    private Receiver receiver;

//    @Test
    @PostConstruct
    public void sendTest() throws InterruptedException {
//        Assert.notNull(cf);
//        Assert.notNull(mqService);


//        while(true){
//            jmsTemplate.send("test_yunhuni_topic_framework_tenant", session -> {
//                return session.createTextMessage("ping!:"+ new Date().getTime());
//            });
//            System.out.println("send message ok");
//            TimeUnit.SECONDS.sleep(1);
//        }

        int count = 1;
        while(count-->0){

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            long startDt = System.currentTimeMillis();
            TestEvent te = new TestEvent();
            mqService.publish(te);

            if (logger.isDebugEnabled()){
                    logger.debug("发送消息 完成："+te.getId() + "  花费："+(System.currentTimeMillis()-startDt)+"ms");
             }

        }

    }

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(MQSender.class);
        ctx.close();
    }

}
