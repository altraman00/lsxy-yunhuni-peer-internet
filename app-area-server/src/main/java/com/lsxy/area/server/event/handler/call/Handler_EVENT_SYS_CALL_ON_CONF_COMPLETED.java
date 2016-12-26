package com.lsxy.area.server.event.handler.call;

import com.lsxy.area.api.BusinessState;
import com.lsxy.area.api.BusinessStateService;
import com.lsxy.area.api.ConfService;
import com.lsxy.area.server.event.EventHandler;
import com.lsxy.area.server.service.callcenter.CallCenterUtil;
import com.lsxy.area.server.service.callcenter.ConversationService;
import com.lsxy.area.server.service.ivr.IVRActionService;
import com.lsxy.area.server.util.NotifyCallbackUtil;
import com.lsxy.framework.core.utils.MapBuilder;
import com.lsxy.framework.rpc.api.RPCCaller;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.RPCResponse;
import com.lsxy.framework.rpc.api.ServiceConstants;
import com.lsxy.framework.rpc.api.event.Constants;
import com.lsxy.framework.rpc.api.session.Session;
import com.lsxy.framework.rpc.api.session.SessionContext;
import com.lsxy.framework.rpc.exceptions.InvalidParamException;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.service.AppService;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by liuws on 2016/8/30.
 */
@Component
public class Handler_EVENT_SYS_CALL_ON_CONF_COMPLETED extends EventHandler {

    private static final Logger logger = LoggerFactory.getLogger(Handler_EVENT_SYS_CALL_ON_CONF_COMPLETED.class);

    @Autowired
    private AppService appService;

    @Autowired
    private BusinessStateService businessStateService;

    @Autowired
    private NotifyCallbackUtil notifyCallbackUtil;

    @Autowired
    private ConfService confService;

    @Autowired
    private ConversationService conversationService;
    @Autowired
    private RPCCaller rpcCaller;

    @Autowired
    private SessionContext sessionContext;

    @Autowired
    private IVRActionService ivrActionService;

    @Override
    public String getEventName() {
        return Constants.EVENT_SYS_CALL_ON_CONF_COMPLETED;
    }

    /**
     * 呼叫加入会议结束的事件，需要向开发者发送离开会议通知
     * @param request
     * @param session
     * @return
     */
    @Override
    public RPCResponse handle(RPCRequest request, Session session) {
        RPCResponse res = null;
        Map<String,Object> params = request.getParamMap();
        if(MapUtils.isEmpty(params)){
            throw new InvalidParamException("request params is null");
        }
        String call_id = (String)params.get("user_data");
        if(StringUtils.isBlank(call_id)){
            throw new InvalidParamException("call_id is null");
        }
        BusinessState state = businessStateService.get(call_id);
        if(state == null){
            throw new InvalidParamException("businessstate is null");
        }

        if(logger.isDebugEnabled()){
            logger.info("call_id={},state={}",call_id,state);
        }
        if(BusinessState.TYPE_CC_AGENT_CALL.equals(state.getType()) ||
                BusinessState.TYPE_CC_OUT_CALL.equals(state.getType()) ||
                (BusinessState.TYPE_IVR_INCOMING.equals(state.getType())
                        &&  conversationService.isCC(call_id))){
            conversation(state,params,call_id);
        }else{
            conf(state,params,call_id);
        }
        return res;
    }

    private void conversation(BusinessState state, Map<String, Object> params, String call_id) {
        String appId = state.getAppId();
        Map<String,String> businessData = state.getBusinessData();
        String conversation_id = null;
        if(businessData!=null){
            conversation_id = businessData.get(CallCenterUtil.CONVERSATION_FIELD);
        }
        if(StringUtils.isBlank(conversation_id)){
            throw new InvalidParamException("没有找到对应的交谈信息callid={},conversation_id={}",call_id,conversation_id);
        }
        if(StringUtils.isBlank(appId)){
            throw new InvalidParamException("没有找到对应的app信息appId={}",appId);
        }
        conversationService.logicExit(conversation_id,call_id);
        if(logger.isDebugEnabled()){
            logger.debug("处理{}事件完成",getEventName());
        }
    }

    private void conf(BusinessState state,Map<String,Object> params,String call_id){
        String user_data = state.getUserdata();
        Map<String,String> businessData = state.getBusinessData();
        String conf_id = null;
        if(businessData!=null){
            conf_id = businessData.get("conf_id");
        }
        if(StringUtils.isBlank(conf_id)){
            throw new InvalidParamException("没有找到对应的会议信息callid={},confid={}",call_id,conf_id);
        }
        hungup(state);
        //会议成员递减
        confService.decrPart(conf_id,call_id);

        //开始通知开发者
        if(logger.isDebugEnabled()){
            logger.debug("开始发送离开会议通知给开发者");
        }
        Long begin_time = null;
        Long end_time = null;
        if(params.get("begin_time") != null){
            begin_time = (Long.parseLong(params.get("begin_time").toString())) * 1000;
        }
        if(params.get("end_time") != null){
            end_time = (Long.parseLong(params.get("end_time").toString())) * 1000;
        }

        if(StringUtils.isNotBlank(state.getCallBackUrl())){
            Map<String,Object> notify_data = new MapBuilder<String,Object>()
                    .putIfNotEmpty("event","conf.quit")
                    .putIfNotEmpty("id",conf_id)
                    .putIfNotEmpty("join_time",begin_time)
                    .putIfNotEmpty("quit_time",end_time)
                    .putIfNotEmpty("call_id",call_id)
                    .putIfNotEmpty("part_uri",null)
                    .putIfNotEmpty("user_data",user_data)
                    .build();
            notifyCallbackUtil.postNotify(state.getCallBackUrl(),notify_data,3);
        }

        if(logger.isDebugEnabled()){
            logger.debug("离开会议通知发送成功");
        }
        if(logger.isDebugEnabled()){
            logger.debug("处理{}事件完成",getEventName());
        }
    }

    private void hungup(BusinessState state){
        //非ivr发起的会议可以直接挂断
        if(BusinessState.TYPE_SYS_CONF.equals(state.getType())){
            Map<String, Object> params = new MapBuilder<String,Object>()
                    .putIfNotEmpty("res_id",state.getResId())
                    .putIfNotEmpty("user_data",state.getId())
                    .put("areaId",state.getAreaId())
                    .build();

            RPCRequest rpcrequest = RPCRequest.newRequest(ServiceConstants.MN_CH_SYS_CALL_DROP, params);
            try {
                rpcCaller.invoke(sessionContext, rpcrequest,true);
            } catch (Throwable e) {
                logger.error("调用失败",e);
            }
        }
    }
}
