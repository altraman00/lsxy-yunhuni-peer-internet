package com.lsxy.call.center.mq.handler;

import com.lsxy.call.center.api.model.AgentSkill;
import com.lsxy.call.center.api.model.Condition;
import com.lsxy.call.center.api.service.AgentSkillService;
import com.lsxy.call.center.api.service.CallCenterAgentService;
import com.lsxy.call.center.api.service.CallCenterService;
import com.lsxy.call.center.api.service.ConditionService;
import com.lsxy.call.center.states.lock.ModifyConditionLock;
import com.lsxy.call.center.states.statics.ACs;
import com.lsxy.call.center.states.statics.CAs;
import com.lsxy.call.center.utils.ExpressionUtils;
import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.framework.mq.api.MQMessageHandler;
import com.lsxy.framework.mq.events.callcenter.CallCenterIncrCostEvent;
import com.lsxy.framework.mq.events.callcenter.CreateConditionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CallCenterIncrCostEventHandler implements MQMessageHandler<CallCenterIncrCostEvent> {

    private static final Logger logger = LoggerFactory.getLogger(CallCenterIncrCostEventHandler.class);

    @Autowired
    private CallCenterService callCenterService;

    @Override
    public void handleMessage(CallCenterIncrCostEvent message) throws JMSException {
        if(logger.isDebugEnabled()){
            logger.debug("处理CallCenterIncrCostEvent{}",message.toJson());
        }
        callCenterService.incrCost(message.getCallCenterId(),message.getCost());
    }
}
