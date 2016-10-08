package com.lsxy.framework.mq.api;

import com.lsxy.framework.core.utils.UUIDGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Created by tandy on 16/8/7.
 */
public class MQEventBuilder {
    private static final Logger logger = LoggerFactory.getLogger(MQEventBuilder.class);

    public static  <T> T  build(Class<T> mqclazz){
        AbstractMQEvent mqevent = null;
        try {
            mqevent = (AbstractMQEvent) mqclazz.newInstance();
            mqevent.setTimestamp(new Date().getTime());
            mqevent.setId(UUIDGenerator.uuid());
        } catch (InstantiationException e) {
            logger.error("异常",e);
        } catch (IllegalAccessException e) {
            logger.error("异常",e);
        }
        return (T) mqevent;
    }
}
