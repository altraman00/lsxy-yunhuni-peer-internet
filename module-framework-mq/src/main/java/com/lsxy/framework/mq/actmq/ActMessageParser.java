package com.lsxy.framework.mq.actmq;

import com.lsxy.framework.mq.api.MQEvent;
import com.lsxy.framework.mq.api.MQMessageParser;
import com.lsxy.framework.mq.exceptions.InvalidMQEventMessageException;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * Created by Tandy on 2016/7/23.
 */
@Component
@ConditionalOnProperty(value = "global.mq.provider", havingValue = "actmq", matchIfMissing = false)
public class ActMessageParser implements MQMessageParser {
    private static final Logger logger = LoggerFactory.getLogger(ActMessageParser.class);
    @Override
    public MQEvent parse(String message) throws InvalidMQEventMessageException {
        return buildFromBase64(message);
    }


    /**
     * 对象从base64反向构建
     * @param base64
     * @return
     */
    public MQEvent buildFromBase64(String base64){
        byte[] bytes = Base64.decodeBase64(base64);
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        MQEvent event = null;
        try {
            ObjectInputStream ois = new ObjectInputStream(bais);
            Object obj = ois.readObject();
            event = (MQEvent) obj;
        } catch (IOException | ClassNotFoundException e) {
            logger.error("IO异常",e);
        }
        return event;
    }

}
