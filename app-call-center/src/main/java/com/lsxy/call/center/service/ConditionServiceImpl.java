package com.lsxy.call.center.service;

import com.lsxy.call.center.api.model.Channel;
import com.lsxy.call.center.api.model.Condition;
import com.lsxy.call.center.api.service.ChannelService;
import com.lsxy.call.center.api.service.ConditionService;
import com.lsxy.call.center.dao.ConditionDao;
import com.lsxy.call.center.utils.ExpressionUtils;
import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.mq.api.AbstractMQEvent;
import com.lsxy.framework.mq.api.MQService;
import com.lsxy.framework.mq.events.callcenter.CreateConditionEvent;
import com.lsxy.framework.mq.events.callcenter.ModifyConditionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * Created by zhangxb on 2016/10/21.
 */
@Service
@com.alibaba.dubbo.config.annotation.Service
public class ConditionServiceImpl extends AbstractService<Condition> implements ConditionService {

    private static final Logger logger = LoggerFactory.getLogger(ConditionServiceImpl.class);

    @Autowired
    private ConditionDao conditionDao;

    @Autowired
    private ChannelService channelService;

    @Autowired
    private MQService mqService;

    @Override
    public BaseDaoInterface<Condition, Serializable> getDao() {
        return conditionDao;
    }

    @Override
    public Condition save(Condition condition){
        if(condition == null){
            throw new NullPointerException();
        }
        if(condition.getTenantId() == null){
            throw new IllegalArgumentException("tenantId 不能为null");
        }
        if(condition.getAppId() == null){
            throw new IllegalArgumentException("appId 不能为null");
        }
        if(condition.getChannelId() == null){
            throw new IllegalArgumentException("channelId 不能为null");
        }
        if(!ExpressionUtils.invalidSortExpression(condition.getSortExpression())){
            throw new IllegalArgumentException("sort expression 错误");
        }
        if(!ExpressionUtils.invalidWhereExpression(condition.getWhereExpression())){
            throw new IllegalArgumentException("where expression 错误");
        }
        //通道是否存在
        Channel channel = channelService.findById(condition.getChannelId());
        if(channel == null){
            throw new IllegalArgumentException("channel 不存在");
        }
        AbstractMQEvent event = null;
        if(condition.getId() != null){
            boolean modify_where = false;
            boolean modify_sort = false;
            boolean modify_priority = false;

            Condition oldCondition = this.findById(condition.getId());
            modify_where = !(oldCondition.getWhereExpression() == null?"":oldCondition.getWhereExpression())
                                .equals(condition.getWhereExpression() == null?"":condition.getWhereExpression());
            modify_sort = !(oldCondition.getSortExpression() == null?"":oldCondition.getSortExpression())
                                .equals(condition.getSortExpression() == null?"":condition.getSortExpression());
            modify_priority = Integer.compare(oldCondition.getPriority() == null ? 0 : oldCondition.getPriority(),
                                              condition.getPriority() == null ? 0 : condition.getPriority()) != 0;

            oldCondition.setWhereExpression(condition.getWhereExpression());
            oldCondition.setSortExpression(condition.getSortExpression());
            oldCondition.setPriority(condition.getPriority());
            oldCondition.setQueueTimeout(condition.getQueueTimeout());
            oldCondition.setFetchTimeout(condition.getFetchTimeout());
            oldCondition.setRemark(condition.getRemark());
            condition = oldCondition;
            condition = super.save(condition);
            //修改条件事件
            event = new ModifyConditionEvent(condition.getId(),condition.getTenantId(),condition.getAppId(),
                                                modify_where,modify_sort,modify_priority);
        }else{
            condition = super.save(condition);
            //创建条件事件
            event = new CreateConditionEvent(condition.getId(),condition.getTenantId(),condition.getAppId());
        }
        mqService.publish(event);
        return condition;
    }

}
