package com.lsxy.area.server.service.ivr.handler;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsxy.area.api.BusinessState;
import com.lsxy.area.api.BusinessStateService;
import com.lsxy.call.center.api.model.CallCenter;
import com.lsxy.call.center.api.model.EnQueue;
import com.lsxy.call.center.api.service.CallCenterService;
import com.lsxy.call.center.api.service.EnQueueService;
import com.lsxy.call.center.api.utils.EnQueueDecoder;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * enqueue动作指令处理器
 * Created by liuws on 2016/11/16.
 */
@Component
public class EnqueueHandler extends ActionHandler{

    public final static String action = "enqueue";

    @Autowired
    private BusinessStateService businessStateService;

    @Reference(lazy = true,check = false,timeout = 3000)
    private EnQueueService enQueueService;

    @Reference(lazy = true,check = false,timeout = 3000)
    private CallCenterService callCenterService;

    @Override
    public String getAction() {
        return action;
    }

    @Override
    public boolean handle(String callId, Element root,String next) {
        if(logger.isDebugEnabled()){
            logger.debug("开始处理ivr动作，callId={},ivr={}",callId,getAction());
        }
        if(logger.isDebugEnabled()){
            logger.debug("开始处理ivr[{}]动作",getAction());
        }
        BusinessState state = businessStateService.get(callId);
        if(state == null){
            logger.info("没有找到call_id={}的state",callId);
            return false;
        }
        Map<String,Object> businessData = state.getBusinessData();
        if(businessData == null){
            businessData = new HashMap<>();
        }
        String xml = root.asXML();
        EnQueue enQueue = EnQueueDecoder.decode(xml);

        if(enQueue!=null){
            CallCenter callCenter = new CallCenter();
            callCenter.setTenantId(state.getTenantId());
            callCenter.setAppId(state.getAppId());
            callCenter.setType(""+CallCenter.CALL_IN);
            callCenter.setAgent(null);
            callCenter.setStartTime(new Date());
            callCenter.setFromNum((String)businessData.get("from"));
            callCenter.setToNum((String)businessData.get("to"));
            callCenter = callCenterService.save(callCenter);
            /*businessData.put("conversation_level",enQueue.getConversation_level());
            businessData.put("conversation_timeout",enQueue.getConversation_level());
            businessData.put("reserve_state",enQueue.getConversation_level());
            businessData.put("fail_overflow",enQueue.getConversation_level());
            businessData.put("wait_voice",enQueue.getConversation_level());
            businessData.put("ring_mode",enQueue.getConversation_level());
            businessData.put("ring_voice",enQueue.getConversation_level());
            businessData.put("hold_voice",enQueue.getConversation_level());
            businessData.put("play_num",enQueue.getConversation_level());
            businessData.put("pre_num_voice",enQueue.getConversation_level());
            businessData.put("post_num_voice",enQueue.getConversation_level());
            businessData.put("play_num",enQueue.getConversation_level());*/
            businessData.put("callcenter",callCenter.getId());
            state.setUserdata(enQueue.getData());
        }
        businessData.put("next",next);
        state.setBusinessData(businessData);
        businessStateService.save(state);
        enQueueService.lookupAgent(state.getTenantId(),state.getAppId(),(String)businessData.get("to"),callId,enQueue);
        return true;
    }
}
