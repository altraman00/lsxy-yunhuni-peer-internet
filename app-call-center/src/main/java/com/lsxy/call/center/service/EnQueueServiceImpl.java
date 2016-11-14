package com.lsxy.call.center.service;

import com.lsxy.call.center.api.model.Channel;
import com.lsxy.call.center.api.model.Condition;
import com.lsxy.call.center.api.model.EnQueue;
import com.lsxy.call.center.api.service.ChannelService;
import com.lsxy.call.center.api.service.ConditionService;
import com.lsxy.call.center.api.service.EnQueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by liuws on 2016/11/14.
 */
@Service
@com.alibaba.dubbo.config.annotation.Service
public class EnQueueServiceImpl implements EnQueueService{

    @Autowired
    private ChannelService channelService;

    @Autowired
    private ConditionService conditionService;

    /**
     * 排队,通过 dubbo返回结果给  区域管理器
     * @param tenantId
     * @param appId
     * @param callId 参与排队的callId
     * @param enQueue
     */
    @Override
    public void lookupAgent(String tenantId, String appId, String callId, EnQueue enQueue){
        if(tenantId == null){
            throw new IllegalArgumentException("tenantId 不能为null");
        }
        if(appId == null){
            throw new IllegalArgumentException("appId 不能为null");
        }
        if(callId == null){
            throw new IllegalArgumentException("callId 不能为null");
        }
        if(enQueue == null){
            throw new IllegalArgumentException("enQueue 不能为null");
        }
        Channel channel = channelService.findOne(tenantId,appId,enQueue.getChannel());
        if(channel == null){
            throw new IllegalArgumentException("通道不存在");
        }
        String conditionId = enQueue.getRoute().getCondition().getId();
        Condition condition = conditionService.findOne(tenantId,appId,conditionId);
        if(condition == null){
            throw new IllegalArgumentException("条件不存在");
        }
        //创建排队记录

        //lua脚本
    }

    /**
     * 坐席找排队
     * @param tenantId
     * @param appId
     * @param agentId
     */
    @Override
    public void lookupQueue(String tenantId, String appId,String conditionId, String agentId){

    }

}
