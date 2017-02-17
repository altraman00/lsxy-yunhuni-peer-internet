package com.lsxy.call.center.mq.handler;

import com.lsxy.call.center.api.service.AgentSkillService;
import com.lsxy.call.center.api.service.CallCenterAgentService;
import com.lsxy.call.center.api.service.ConditionService;
import com.lsxy.call.center.api.states.lock.ModifyConditionLock;
import com.lsxy.call.center.api.states.statics.ACs;
import com.lsxy.call.center.api.states.statics.CAs;
import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.framework.mq.api.MQMessageHandler;
import com.lsxy.framework.mq.events.callcenter.DeleteConditionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import java.util.List;

@Component
public class DeleteConditionEventHandler implements MQMessageHandler<DeleteConditionEvent> {

    private static final Logger logger = LoggerFactory.getLogger(DeleteConditionEventHandler.class);

    @Autowired
    private CallCenterAgentService callCenterAgentService;

    @Autowired
    private AgentSkillService agentSkillService;

    @Autowired
    private ConditionService conditionService;

    @Autowired
    private RedisCacheService redisCacheService;

    @Autowired
    private ACs aCs;

    @Autowired
    private CAs cAs;

    @Override
    public void handleMessage(DeleteConditionEvent message) throws JMSException {
        if(logger.isDebugEnabled()){
            logger.debug("处理CallCenter.DeleteConditionEvent{}",message.toJson());
        }
        if(message.getConditionId() == null ||
                message.getConditionId() == null ||
                message.getTenantId() == null ||
                message.getAppId() == null){
            logger.info("处理CallCenter.DeleteConditionEvent出错，参数错误！");
            return;
        }
        long start = System.currentTimeMillis();
        List<String> agentIds = callCenterAgentService
                                        .getAgentIdsByChannel(message.getTenantId(),message.getAppId(),message.getChannelId());
        //清空cAs
        cAs.delete(message.getConditionId());
        if(agentIds != null && agentIds.size() > 0){
            for (String agentId : agentIds) {
                aCs.remove(agentId,message.getConditionId());
            }
        }
        ModifyConditionLock lock = new ModifyConditionLock(redisCacheService,message.getConditionId());
        lock.unlock();
        logger.info("处理CallCenter.DeleteConditionEvent耗时={}",(System.currentTimeMillis() - start));
    }
}
