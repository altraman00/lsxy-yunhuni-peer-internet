package com.lsxy.call.center.mq.handler;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsxy.call.center.api.model.AgentSkill;
import com.lsxy.call.center.api.model.Condition;
import com.lsxy.call.center.api.service.AgentSkillService;
import com.lsxy.call.center.api.service.CallCenterAgentService;
import com.lsxy.call.center.api.service.ConditionService;
import com.lsxy.call.center.api.service.DeQueueService;
import com.lsxy.call.center.states.lock.ModifyConditionLock;
import com.lsxy.call.center.states.statics.ACs;
import com.lsxy.call.center.states.statics.CAs;
import com.lsxy.call.center.utils.ExpressionUtils;
import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.framework.mq.api.MQMessageHandler;
import com.lsxy.framework.mq.events.callcenter.ModifyConditionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ModifyConditionEventHandler implements MQMessageHandler<ModifyConditionEvent> {

    private static final Logger logger = LoggerFactory.getLogger(ModifyConditionEventHandler.class);

    @Autowired
    private CallCenterAgentService callCenterAgentService;

    @Autowired
    private AgentSkillService agentSkillService;

    @Autowired
    private ConditionService conditionService;

    @Autowired
    private ACs aCs;

    @Autowired
    private CAs cAs;

    @Autowired
    private RedisCacheService redisCacheService;

    @Reference(lazy = true,check = false,timeout = 3000)
    private DeQueueService deQueueService;

    @Override
    public void handleMessage(ModifyConditionEvent message) throws JMSException {
        if(logger.isDebugEnabled()){
            logger.debug("处理CallCenter.ModifyConditionEvent{}",message.toJson());
        }

        if(message.getConditionId() == null ||
                message.getTenantId() == null ||
                message.getAppId() == null){
            logger.info("处理CallCenter.ModifyConditionEvent，参数错误！");
            return;
        }
        if(!message.ismWhere() && !message.ismSort() && !message.ismPriority()){
            logger.info("处理CallCenter.ModifyConditionEvent，没有修改表达式");
            return;
        }
        Condition condition = conditionService.findById(message.getConditionId());
        if(condition == null){
            logger.info("处理CallCenter.ModifyConditionEvent出错，条件不存在！");
            return;
        }

        //修改CAs ACs
        long start = System.currentTimeMillis();
        List<String> agentIds = callCenterAgentService
                .getAgentIdsByChannel(condition.getTenantId(),condition.getAppId(),condition.getChannelId());
        if(message.ismWhere()){//修改了where表达式才会导致cas和acs的关系发生变化，否则只是分值发生变化
            //清空cAs
            cAs.delete(condition.getId());
            if(agentIds != null && agentIds.size() > 0){
                for (String agentId : agentIds) {
                    aCs.remove(agentId,condition.getId());
                }
            }
        }
        if(agentIds != null && agentIds.size() > 0){
            for (String agentId : agentIds) {
                init(agentId,condition);
            }
        }
        ModifyConditionLock lock = new ModifyConditionLock(redisCacheService,condition.getId());
        lock.unlock();
        logger.info("处理CallCenter.ModifyConditionEvent耗时={}",(System.currentTimeMillis() - start));
    }

    /**
    * CAs 增加agent
    * ACs 增加condition
    * @param agentId
    * @param condition
    */
    public void init(String agentId,Condition condition){
        try{
            List<AgentSkill> skills = agentSkillService.findByAgent(condition.getTenantId(),condition.getAppId(),agentId);
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
            logger.error("处理CallCenter.ModifyConditionEvent出错",t);
        }
    }
}
