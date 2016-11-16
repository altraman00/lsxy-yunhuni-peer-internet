package com.lsxy.framework.mq.events.portal;

import com.lsxy.framework.mq.api.AbstractMQEvent;

import static com.lsxy.framework.mq.topic.MQTopicConstants.TOPIC_APP_PORTAL;

/**
 * Created by zhangxb on 2016/10/20.
 */
public class SendActivePasswordSuccessEvent extends AbstractMQEvent {
    private String key;
    private String password;
    public SendActivePasswordSuccessEvent(){
    }

    public SendActivePasswordSuccessEvent(String key, String password) {
        this.key = key;
        this.password = password;
    }

    @Override
    public String getTopicName() {
        return TOPIC_APP_PORTAL;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
