package com.lsxy.framework.mq.api;

/**
 * Created by Tandy on 2016/7/21.
 */
public interface MQService {

    public void publish(AbstractMQEvent event);
}
