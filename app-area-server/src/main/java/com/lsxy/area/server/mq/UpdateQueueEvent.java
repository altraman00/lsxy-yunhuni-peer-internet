package com.lsxy.area.server.mq;

import com.lsxy.framework.mq.api.AbstractDelayMQEvent;
import com.lsxy.framework.mq.api.AbstractMQEvent;
import com.lsxy.framework.mq.topic.MQTopicConstants;

/**
 * Created by liuws on 2016/12/20.
 */
public class UpdateQueueEvent extends AbstractDelayMQEvent {

    private String queue;

    private String queueId;

    public UpdateQueueEvent(){}

    public UpdateQueueEvent(String queue, String queueId){
        this.queue = queue;
        this.queueId = queueId;
    }

    @Override
    public String getTopicName() {
        return MQTopicConstants.TOPIC_AREA_SERVER;
    }

    public String getQueue() {
        return queue;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }

    public String getQueueId() {
        return queueId;
    }

    public void setQueueId(String queueId) {
        this.queueId = queueId;
    }
}
