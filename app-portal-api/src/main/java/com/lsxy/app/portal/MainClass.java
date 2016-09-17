package com.lsxy.app.portal;

import com.lsxy.framework.FrameworkServiceConfig;
import com.lsxy.framework.api.FrameworkApiConfig;
import com.lsxy.framework.cache.FrameworkCacheConfig;
import com.lsxy.framework.mq.FrameworkMQConfig;
import com.lsxy.framework.mq.topic.MQTopicConstants;
import com.lsxy.framework.sms.FrameworkSmsConfig;
import com.lsxy.framework.sms.model.MobileCode;
import com.lsxy.framework.web.utils.WebUtils;
import com.lsxy.framework.web.web.AbstractSpringBootWebStarter;
import com.lsxy.yunhuni.YunhuniServiceConfig;
import com.lsxy.yunhuni.api.YunhuniApiConfig;
import org.slf4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.context.annotation.Import;

/**
 * Created by Tandy on 2016/6/13.
 */
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@Import({FrameworkApiConfig.class,FrameworkServiceConfig.class, FrameworkCacheConfig.class, YunhuniApiConfig.class, YunhuniServiceConfig.class,
        FrameworkSmsConfig.class, FrameworkMQConfig.class})
public class MainClass extends AbstractSpringBootWebStarter {

    public static final String systemId = "portal.api";

    public static void main(String[] args) throws Exception {
        SpringApplication.run(MainClass.class, args);
        ////////////////////////////////////////////////////////////
        Logger logger = WebUtils.logger;
        logger.info("aaaa");
        System.out.println(MQTopicConstants.TOPIC_APP_OC);
        MobileCode mobileCode = new MobileCode();
        System.out.println(mobileCode);
    }

    @Override
    public String systemId() {
        return systemId;
    }
}
