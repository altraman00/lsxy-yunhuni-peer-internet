package com.lsxy.framework.mq.api;

import com.lsxy.framework.core.utils.UUIDGenerator;

import java.util.Date;

/**
 * Created by tandy on 16/8/7.
 */
public class MQEventBuilder {
    public static  <T> T  build(Class<T> mqclazz){
        AbstractMQEvent mqevent = null;
        try {
            mqevent = (AbstractMQEvent) mqclazz.newInstance();
            mqevent.setTimestamp(new Date().getTime());
            mqevent.setId(UUIDGenerator.uuid());
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return (T) mqevent;
    }
}
