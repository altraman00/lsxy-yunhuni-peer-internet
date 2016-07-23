package com.lsxy.framework.mq.test.sender;

import com.lsxy.framework.cache.FrameworkCacheConfig;
import com.lsxy.framework.mq.FrameworkMQConfig;
import com.lsxy.framework.mq.actmq.ActMQConfig;
import com.lsxy.framework.mq.api.MQService;
import com.lsxy.framework.mq.test.events.TestEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.jms.annotation.EnableJms;

import java.util.concurrent.TimeUnit;

import static com.lsxy.framework.core.web.SpringContextUtil.getBean;

/**
 * Created by Tandy on 2016/7/22.
 */
@SpringBootApplication
@Import(value={FrameworkMQConfig.class, FrameworkCacheConfig.class})
//@EnableScheduling
@EnableAutoConfiguration
@EnableJms
public class JmsPublishSubscribeApplication {
    public static final Logger logger = LoggerFactory.getLogger(JmsPublishSubscribeApplication.class);
//    @Autowired
//    private MQService mqService;

    public static void main(String[] args) throws InterruptedException {
        ConfigurableApplicationContext ctx = SpringApplication.run(JmsPublishSubscribeApplication.class, args);
        MQService mqService = ctx.getBean(MQService.class);

       while(true){
            long startDt = System.currentTimeMillis();
            TestEvent te = new TestEvent();
            mqService.publishTopicEvent(te);

            if (logger.isDebugEnabled()){
                logger.debug("发送消息 完成："+te.getId() + "  花费："+(System.currentTimeMillis()-startDt)+"ms");
            }

           TimeUnit.SECONDS.sleep(1);
        }
    }
}
