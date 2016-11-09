package com.lsxy.call.center.service;

import com.lsxy.call.center.api.model.Condition;
import com.lsxy.call.center.api.service.ConditionService;
import com.lsxy.call.center.dao.ConditionDao;
import com.lsxy.call.center.utils.ExpressionUtils;
import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.mq.api.AbstractMQEvent;
import com.lsxy.framework.mq.api.MQService;
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
        if(!ExpressionUtils.invalidSortExpression(condition.getSortExpression())){
            throw new IllegalArgumentException();
        }
        if(!ExpressionUtils.invalidWhereExpression(condition.getWhereExpression())){
            throw new IllegalArgumentException();
        }
        AbstractMQEvent event = null;
        if(condition.getId() != null){
            Condition oldCondition = this.findById(condition.getId());
            oldCondition.setWhereExpression(condition.getWhereExpression());
            oldCondition.setSortExpression(condition.getSortExpression());
            oldCondition.setPriority(condition.getPriority());
            oldCondition.setQueueTimeout(condition.getQueueTimeout());
            oldCondition.setFetchTimeout(condition.getFetchTimeout());
            oldCondition.setRemark(condition.getRemark());
            condition = oldCondition;
            //修改条件事件
        }else{
            //创建条件事件
        }
        condition = super.save(condition);
        mqService.publish(event);
        return condition;
    }
}
