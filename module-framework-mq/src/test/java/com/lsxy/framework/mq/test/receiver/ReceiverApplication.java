package com.lsxy.framework.mq.test.receiver;

import com.lsxy.framework.cache.FrameworkCacheConfig;
import com.lsxy.framework.core.test.SpringBootTestCase;
import com.lsxy.framework.mq.FrameworkMQConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.jms.annotation.EnableJms;

/**
 * Created by tandy on 16/7/21.
 */

@Import(value={FrameworkMQConfig.class, FrameworkCacheConfig.class})
@SpringApplicationConfiguration(value=ReceiverApplication.class)
@EnableAutoConfiguration
@EnableJms
public class ReceiverApplication extends SpringBootTestCase{

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(ReceiverApplication.class);

    }

}
