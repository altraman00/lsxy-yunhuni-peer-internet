package com.lsxy.area.server.service.ivr.handler;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsxy.area.api.BusinessState;
import com.lsxy.area.api.BusinessStateService;
import com.lsxy.area.server.service.callcenter.ConversationService;
import com.lsxy.area.server.service.ivr.IVRActionService;
import com.lsxy.area.server.util.PlayFileUtil;
import com.lsxy.call.center.api.model.CallCenter;
import com.lsxy.call.center.api.model.EnQueue;
import com.lsxy.call.center.api.service.CallCenterService;
import com.lsxy.call.center.api.service.DeQueueService;
import com.lsxy.call.center.api.service.EnQueueService;
import com.lsxy.call.center.api.utils.EnQueueDecoder;
import com.lsxy.framework.core.utils.JSONUtil;
import com.lsxy.framework.core.utils.JSONUtil2;
import com.lsxy.framework.core.utils.MapBuilder;
import com.lsxy.framework.rpc.api.RPCCaller;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.ServiceConstants;
import com.lsxy.framework.rpc.api.session.SessionContext;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * enqueue动作指令处理器
 * Created by liuws on 2016/11/16.
 */
@Component
public class EnqueueHandler extends ActionHandler{

    @Autowired
    private BusinessStateService businessStateService;

    @Reference(lazy = true,check = false,timeout = 3000)
    private EnQueueService enQueueService;

    @Reference(lazy = true,check = false,timeout = 3000)
    private CallCenterService callCenterService;

    @Autowired
    private DeQueueService deQueueService;

    @Autowired
    private RPCCaller rpcCaller;

    @Autowired
    private SessionContext sessionContext;

    @Autowired
    private PlayFileUtil playFileUtil;

    @Override
    public String getAction() {
        return "enqueue";
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
        Map<String,String> businessData = state.getBusinessData();
        String xml = root.asXML();
        EnQueue enQueue = EnQueueDecoder.decode(xml);

        if(enQueue!=null){
            if(logger.isDebugEnabled()){
                logger.debug("排队={}", JSONUtil.objectToJson(enQueue));
            }
            CallCenter callCenter = new CallCenter();
            callCenter.setTenantId(state.getTenantId());
            callCenter.setAppId(state.getAppId());
            callCenter.setType(""+CallCenter.CALL_IN);
            callCenter.setAgent(null);
            callCenter.setStartTime(new Date());
            callCenter.setFromNum(businessData.get("from"));
            callCenter.setToNum(businessData.get("to"));
            callCenter = callCenterService.save(callCenter);
            businessStateService.updateInnerField(callId,ConversationService.CALLCENTER_ID_FIELD,callCenter.getId());
            businessStateService.updateUserdata(callId,enQueue.getData());

            if(enQueue.getWait_voice()!= null){
                String playWait = enQueue.getWait_voice();
                try {
                    playWait = playFileUtil.convert(state.getTenantId(),state.getAppId(),playWait);
                    if(logger.isDebugEnabled()){
                        logger.debug("开始播放排队等待音={}", playWait);
                    }
                    Map<String, Object> params = new MapBuilder<String,Object>()
                            .putIfNotEmpty("res_id",state.getResId())
                            .putIfNotEmpty("content", JSONUtil2.objectToJson(new Object[][]{new Object[]{playWait,0,""}}))
                            .putIfNotEmpty("user_data",callId)
                            .put("finish_keys","")
                            .put("areaId",state.getAreaId())
                            .build();
                    RPCRequest rpcrequest = RPCRequest.newRequest(ServiceConstants.MN_CH_SYS_CALL_PLAY_START, params);
                    rpcCaller.invoke(sessionContext, rpcrequest);
                    businessStateService.updateInnerField(callId,ConversationService.IS_PLAYWAIT_FIELD,ConversationService.IS_PLAYWAIT_TRUE.toString());
                } catch (Throwable e) {
                    logger.error("调用失败",e);
                }
            }
        }
        businessStateService.updateInnerField(callId, IVRActionService.IVR_NEXT,next);
        try {
            enQueueService.lookupAgent(state.getTenantId(), state.getAppId(), businessData.get("to"), callId, enQueue);
        }catch (Throwable t){
            logger.error("调用呼叫中心排队失败",t);
            deQueueService.fail(state.getTenantId(),state.getAppId(),callId,"调用呼叫中心排队失败");
        }
        return true;
    }
}
