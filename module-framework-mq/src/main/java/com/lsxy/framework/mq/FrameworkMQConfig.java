package com.lsxy.framework.mq;

import com.aliyun.openservices.ons.jms.domain.JmsBaseConnectionFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jms.annotation.EnableJms;

import javax.jms.ConnectionFactory;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by tandy on 16/7/21.
 */
@Configurable
@ComponentScan
public class FrameworkMQConfig {


}
