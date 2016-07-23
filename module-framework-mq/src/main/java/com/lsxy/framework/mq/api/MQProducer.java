package com.lsxy.framework.mq.api;

/**
 * Created by Tandy on 2016/7/23.
 */
public interface MQProducer {
    public void publishEvent(MQEvent event) ;

    public void init();

    public void destroy();
}
