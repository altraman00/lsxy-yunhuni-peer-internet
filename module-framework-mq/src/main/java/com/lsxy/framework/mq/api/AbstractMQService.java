package com.lsxy.framework.mq.api;

import com.lsxy.framework.core.utils.UUIDGenerator;

import java.util.Date;

/**
 * Created by Tandy on 2016/7/23.
 */
public abstract class AbstractMQService implements MQService {
    @Override
    public void publish(AbstractMQEvent event) {
        event.setId(UUIDGenerator.uuid());
        event.setTimestamp(new Date().getTime());

        publishEvent(event);
    }

    protected abstract void publishEvent(AbstractMQEvent event) ;
}
