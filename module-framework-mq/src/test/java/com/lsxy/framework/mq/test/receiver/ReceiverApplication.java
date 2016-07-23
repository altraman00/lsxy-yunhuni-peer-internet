package com.lsxy.framework.mq.test.receiver;

import com.lsxy.framework.cache.FrameworkCacheConfig;
import com.lsxy.framework.core.test.SpringBootTestCase;
import com.lsxy.framework.mq.FrameworkMQConfig;
import com.lsxy.framework.mq.api.MQService;
import com.lsxy.framework.mq.test.events.TestEvent;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;

import java.util.concurrent.TimeUnit;

/**
 * Created by tandy on 16/7/21.
 */

@Import(value={FrameworkMQConfig.class, FrameworkCacheConfig.class})
@SpringApplicationConfiguration(value=ReceiverApplication.class)
//@RunWith(SpringJUnit4ClassRunner.class)
@EnableAutoConfiguration
@EnableJms
public class ReceiverApplication extends SpringBootTestCase{



    public static final Logger logger = LoggerFactory.getLogger(ReceiverApplication.class);


    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(ReceiverApplication.class);

    }

}
