package com.lsxy.framework.mq.ons;

import com.aliyun.openservices.ons.api.PropertyKeyConst;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * Created by Tandy on 2016/7/21.
 */

@Component
@Configuration
@Conditional(OnsCondition.class)
public class OnsMQConfig {

    @Value("${global.mq.ons.ak}")
    private String accessKey;

    @Value("${global.mq.ons.sk}")
    private String secretKey;

    @Value("${global.mq.ons.cid}")
    private String consumerId;

    @Value("${global.mq.ons.pid}")
    private String producerId;


    public Properties getOnsProperties(){
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.ConsumerId, this.consumerId);
        properties.put(PropertyKeyConst.AccessKey, this.accessKey);
        properties.put(PropertyKeyConst.SecretKey, this.secretKey);
        properties.put(PropertyKeyConst.ProducerId,this.producerId);
        return properties;
    }


    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(String consumerId) {
        this.consumerId = consumerId;
    }

    public String getProducerId() {
        return producerId;
    }

    public void setProducerId(String producerId) {
        this.producerId = producerId;
    }

    //    @Bean
//    public ConnectionFactory getOnsConnectionFactory(){
//        String accessKey = "nfgEUCKyOdVMVbqQ";
//        String secretKey = "HhmxAMZ2jCrE0fTa2kh9CLXF9JPcOW";
//        String consumerId = "CID_YUNHUNI-TENANT-001";
//        String pid = "PID_YUNHUNI-TENANT-001";
//        String url = "ons://sender:22?consumerId="+consumerId+"&producerId="+pid+"&accessKey="+ accessKey +"&secretKey=" + secretKey;
//        JmsBaseConnectionFactory cf = null;
//        try {
//            cf = new JmsBaseConnectionFactory(new URI(url));
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
//        return cf;
////
////        String PRODUCERID = "producerId";
////
////        String CONSUMERID = "consumerId";
////        consumeThreadNums
////                sendMsgTimeoutMillis
////        consumerId
////                accessKey
////        secretKey
////                appId
////        provider
////                instanceName
//
//    }
}
