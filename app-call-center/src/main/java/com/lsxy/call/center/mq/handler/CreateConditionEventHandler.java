package com.lsxy.call.center.mq.handler;

import com.lsxy.call.center.api.model.AgentSkill;
import com.lsxy.call.center.api.model.Condition;
import com.lsxy.call.center.api.service.AgentSkillService;
import com.lsxy.call.center.api.service.CallCenterAgentService;
import com.lsxy.call.center.api.service.ConditionService;
import com.lsxy.call.center.api.states.lock.ModifyConditionLock;
import com.lsxy.call.center.api.states.statics.ACs;
import com.lsxy.call.center.api.states.statics.CAs;
import com.lsxy.call.center.utils.ExpressionUtils;
import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.framework.mq.api.MQMessageHandler;
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
public class CreateConditionEventHandler implements MQMessageHandler<CreateConditionEvent> {

    private static final Logger logger = LoggerFactory.getLogger(CreateConditionEventHandler.class);

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
    public void handleMessage(CreateConditionEvent message) throws JMSException {
        if(logger.isDebugEnabled()){
            logger.debug("处理CallCenter.CreateConditionEvent{}",message.toJson());
        }
        if(message.getConditionId() == null ||
                message.getTenantId() == null ||
                message.getAppId() == null){
            logger.info("处理CallCenter.CreateConditionEvent出错，参数错误！");
            return;
        }
        Condition condition = conditionService.findById(message.getConditionId());
        if(condition == null){
            logger.info("处理CallCenter.CreateConditionEvent出错，条件不存在！");
            return;
        }
        //初始化CAs ACs
        long start = System.currentTimeMillis();
        List<String> agentIds = callCenterAgentService
                                        .getAgentIdsBySubaccountId(condition.getTenantId(),condition.getAppId(),condition.getSubaccountId());

        if(agentIds != null && agentIds.size() > 0){
            for (String agentId : agentIds) {
                init(agentId,condition);
            }
        }

        ModifyConditionLock lock = new ModifyConditionLock(redisCacheService,condition.getId());
        lock.unlock();
        logger.info("处理CallCenter.CreateConditionEvent耗时={}",(System.currentTimeMillis() - start));
    }

    /**
     * CAs 增加agent
     * ACs 增加condition
     * @param agentId
     * @param condition
     */
    public void init(String agentId,Condition condition){
        try{
            List<AgentSkill> skills = agentSkillService.findEnabledByAgent(agentId);
            Map<String,Integer> vars = new HashMap<>();
            for (AgentSkill skill:skills) {
                vars.put(skill.getName(),skill.getScore());
            }
            if(ExpressionUtils.execWhereExpression(condition.getWhereExpression(),vars)){
                long score = ExpressionUtils.execSortExpression(condition.getSortExpression(),vars);
                cAs.add(condition.getId(),agentId,score);
                aCs.add(agentId,condition.getId(),condition.getPriority());
            }
        }catch (Throwable t){
            logger.error("处理CallCenter.CreateConditionEvent出错",t);
        }
    }
}
