package com.lsxy.framework.mq.ons;

import com.aliyun.openservices.ons.jms.domain.JmsBaseConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.jms.ConnectionFactory;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by Tandy on 2016/7/21.
 */

@Component
@Configuration
@Conditional(OnsCondition.class)
public class OnsMQConfig {

    @Bean
    public ConnectionFactory getOnsConnectionFactory(){
        String accessKey = "nfgEUCKyOdVMVbqQ";
        String secretKey = "HhmxAMZ2jCrE0fTa2kh9CLXF9JPcOW";
        String consumerId = "CID_test001";
        String pid = "PID_YUNHUNI-TENANT-001";
        String url = "ons://xx:22?producerId="+pid+"&accessKey="+ accessKey +"&secretKey=" + secretKey;
        JmsBaseConnectionFactory cf = null;
        try {
            cf = new JmsBaseConnectionFactory(new URI(url));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return cf;
//
//        String PRODUCERID = "producerId";
//
//        String CONSUMERID = "consumerId";
//        consumeThreadNums
//                sendMsgTimeoutMillis
//        consumerId
//                accessKey
//        secretKey
//                appId
//        provider
//                instanceName

    }
}
