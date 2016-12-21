package com.lsxy.framework.mq.events.callcenter;

import com.lsxy.framework.mq.api.AbstractMQEvent;
import com.lsxy.framework.mq.topic.MQTopicConstants;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2016/12/20.
 */
public class CallCenterIncrCostEvent extends AbstractMQEvent {

    private String callCenterId;

    private BigDecimal cost;

    public CallCenterIncrCostEvent(){}

    public CallCenterIncrCostEvent(String callCenterId,BigDecimal cost){
        this.callCenterId = callCenterId;
        this.cost = cost;
    }

    @Override
    public String getTopicName() {
        return MQTopicConstants.TOPIC_CALL_CENTER;
    }

    public String getCallCenterId() {
        return callCenterId;
    }

    public void setCallCenterId(String callCenterId) {
        this.callCenterId = callCenterId;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }
}
