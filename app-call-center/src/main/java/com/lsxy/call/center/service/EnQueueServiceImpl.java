package com.lsxy.call.center.service;

import com.lsxy.call.center.api.model.CallCenterQueue;
import com.lsxy.call.center.api.model.Channel;
import com.lsxy.call.center.api.model.Condition;
import com.lsxy.call.center.api.model.EnQueue;
import com.lsxy.call.center.api.service.CallCenterQueueService;
import com.lsxy.call.center.api.service.ChannelService;
import com.lsxy.call.center.api.service.ConditionService;
import com.lsxy.call.center.api.service.EnQueueService;
import com.lsxy.call.center.states.statics.ACs;
import com.lsxy.call.center.states.statics.CAs;
import com.lsxy.call.center.states.statics.CQs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

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

    @Autowired
    private CallCenterQueueService callCenterQueueService;

    @Autowired
    private CAs cAs;

    @Autowired
    private ACs aCs;

    @Autowired
    private CQs cQs;

    /**
     * 排队,通过 dubbo返回结果给  区域管理器
     * @param tenantId
     * @param appId
     * @param callId 参与排队的callId
     * @param enQueue
     */
    @Override
    public void lookupAgent(String tenantId, String appId,String num, String callId, EnQueue enQueue){
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
        if(!condition.getChannelId().equals(channel.getAppId())){
            throw new IllegalArgumentException("条件-通道不匹配");
        }
        //创建排队记录
        CallCenterQueue queue = new CallCenterQueue();
        queue.setTenantId(tenantId);
        queue.setAppId(appId);
        queue.setCondition(conditionId);
        queue.setStartTime(new Date());
        //TODO 这个id 存啥
        queue.setRelevanceId("");
        queue.setNum(num);
        queue.setOriginCallId(callId);
        callCenterQueueService.save(queue);
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
