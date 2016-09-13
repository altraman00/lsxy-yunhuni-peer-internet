package com.lsxy.area.server.mq.handler;

import com.lsxy.framework.mq.api.AbstractDelayMQEvent;

/**
 * Created by liuws on 2016/9/13.
 */
public class IVRPauseActionEvent extends AbstractDelayMQEvent{
    @Override
    public String getTopicName() {
        return null;
    }
}
