package com.lsxy.area.server.service.ivr.handler;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsxy.area.api.BusinessState;
import com.lsxy.area.api.BusinessStateService;
import com.lsxy.call.center.api.model.CallCenter;
import com.lsxy.call.center.api.model.EnQueue;
import com.lsxy.call.center.api.service.CallCenterService;
import com.lsxy.call.center.api.service.DeQueueService;
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

    @Autowired
    private DeQueueService deQueueService;

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
            businessData.put("callcenter",callCenter.getId());
            state.setUserdata(enQueue.getData());

            if(enQueue.getWait_voice()!= null){
                //TODO 播放排队等待音
            }
        }
        businessData.put("next",next);
        state.setBusinessData(businessData);
        businessStateService.save(state);
        try {
            enQueueService.lookupAgent(state.getTenantId(), state.getAppId(), (String) businessData.get("to"), callId, enQueue);
        }catch (Throwable t){
            logger.error("调用呼叫中心排队失败",t);
            deQueueService.fail(state.getTenantId(),state.getAppId(),callId,"调用呼叫中心排队失败");
        }
        return true;
    }
}
