package com.lsxy.area.server.event.handler.call;

import com.lsxy.area.api.BusinessState;
import com.lsxy.area.api.BusinessStateService;
import com.lsxy.area.server.event.EventHandler;
import com.lsxy.area.server.service.callcenter.ConversationService;
import com.lsxy.framework.core.exceptions.api.YunhuniApiException;
import com.lsxy.framework.rpc.api.RPCCaller;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.RPCResponse;
import com.lsxy.framework.rpc.api.event.Constants;
import com.lsxy.framework.rpc.api.session.Session;
import com.lsxy.framework.rpc.api.session.SessionContext;
import com.lsxy.framework.rpc.exceptions.InvalidParamException;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by liuws on 2016/8/29.
 */
@Component
public class Handler_EVENT_SYS_CALL_ON_RINGING extends EventHandler{

    private static final Logger logger = LoggerFactory.getLogger(Handler_EVENT_SYS_CALL_ON_RINGING.class);

    @Autowired
    private RPCCaller rpcCaller;

    @Autowired
    private SessionContext sessionContext;

    @Autowired
    private BusinessStateService businessStateService;

    @Autowired
    private ConversationService conversationService;

    @Override
    public String getEventName() {
        return Constants.EVENT_SYS_CALL_ON_RINGING;
    }

    @Override
    public RPCResponse handle(RPCRequest request, Session session) {
        RPCResponse res = null;
        Map<String,Object> params = request.getParamMap();
        if(MapUtils.isEmpty(params)){
            throw new InvalidParamException("request params is null");
        }
        String call_id = (String)params.get("user_data");
        String res_id = (String)params.get("res_id");
        if(StringUtils.isBlank(call_id)){
            throw new InvalidParamException("call_id is null");
        }
        BusinessState state = businessStateService.get(call_id);
        if(state == null){
            throw new InvalidParamException("businessstate is null");
        }
        if(res_id!=null){
            businessStateService.updateResId(call_id,res_id);
        }
        if(logger.isDebugEnabled()){
            logger.info("call_id={},state={}",call_id,state);
        }

        if(BusinessState.TYPE_CC_AGENT_CALL.equals(state.getType()) ||
                BusinessState.TYPE_CC_OUT_CALL.equals(state.getType())
        ){
            /**开始判断振铃前是否客户挂断了呼叫，挂断了要同时挂断被叫的坐席**/
            Map<String,String> businessData = state.getBusinessData();
            String conversation = businessData.get(ConversationService.CONVERSATION_FIELD);
            BusinessState conversationState = businessStateService.get(conversation);
            if(logger.isDebugEnabled()){
                logger.info("开始判断振铃前是否客户挂断了呼叫1:{}",conversationState);
            }
            if(conversationState == null || conversationState.getClosed()){
                conversationService.logicExit(conversation,state.getId());
                return res;
            }
            if(conversationState.getBusinessData()!=null){
                String initiator = conversationState.getBusinessData().get(ConversationService.INITIATOR_FIELD);
                if(initiator != null){
                    BusinessState initiatorState = businessStateService.get(initiator);
                    if(logger.isDebugEnabled()){
                        logger.info("开始判断振铃前是否客户挂断了呼叫2:{}",initiatorState);
                    }
                    if(initiatorState!=null && initiatorState.getClosed() != null && initiatorState.getClosed()){
                        conversationService.logicExit(conversation,state.getId());
                        return res;
                    }
                }
            }
            /**结束判断振铃前是否客户挂断了呼叫，挂断了要同时挂断被叫的坐席**/
            try {
                //加入交谈
                conversationService.join(state.getAppId(),conversation,call_id,null,null,null);
            } catch (YunhuniApiException e) {
                logger.error("将呼叫加入交谈失败",e);
            }
        }
        return res;
    }
}
