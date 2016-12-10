package com.lsxy.area.server.event.handler.call;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsxy.area.api.BusinessState;
import com.lsxy.area.api.BusinessStateService;
import com.lsxy.area.api.ConfService;
import com.lsxy.area.server.event.EventHandler;
import com.lsxy.area.server.service.callcenter.CallCenterUtil;
import com.lsxy.area.server.service.callcenter.ConversationService;
import com.lsxy.area.server.util.NotifyCallbackUtil;
import com.lsxy.call.center.api.service.CallCenterConversationService;
import com.lsxy.framework.core.utils.MapBuilder;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.RPCResponse;
import com.lsxy.framework.rpc.api.event.Constants;
import com.lsxy.framework.rpc.api.session.Session;
import com.lsxy.framework.rpc.exceptions.InvalidParamException;
import com.lsxy.yunhuni.api.app.service.AppService;
import com.lsxy.yunhuni.api.session.service.CallSessionService;
import com.lsxy.yunhuni.api.session.service.MeetingMemberService;
import com.lsxy.yunhuni.api.session.service.MeetingService;
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
public class Handler_EVENT_SYS_CALL_CONF_ENTER_FAIL extends EventHandler{

    private static final Logger logger = LoggerFactory.getLogger(Handler_EVENT_SYS_CALL_CONF_ENTER_FAIL.class);

    @Autowired
    private AppService appService;

    @Autowired
    private BusinessStateService businessStateService;

    @Autowired
    private NotifyCallbackUtil notifyCallbackUtil;

    @Autowired
    private MeetingService meetingService;

    @Autowired
    private MeetingMemberService meetingMemberService;

    @Autowired
    private CallSessionService callSessionService;

    @Autowired
    private ConfService confService;

    @Autowired
    private ConversationService conversationService;

    @Reference(lazy = true,check = false,timeout = 3000)
    private CallCenterConversationService callCenterConversationService;


    @Override
    public String getEventName() {
        return Constants.EVENT_SYS_CALL_CONF_ENTER_FAIL;
    }

    /**
     * 接收到加入会议失败事件，需要向开发者发送通知
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
            throw new InvalidParamException("call_id=null");
        }
        BusinessState state = businessStateService.get(call_id);
        if(state == null){
            throw new InvalidParamException("businessstate is null");
        }

        if(logger.isDebugEnabled()){
            logger.debug("call_id={},state={}",call_id,state);
        }
        if(BusinessState.TYPE_CC_AGENT_CALL.equals(state.getType()) ||
            BusinessState.TYPE_CC_OUT_CALL.equals(state.getType()) ||
            (BusinessState.TYPE_IVR_INCOMING.equals(state.getType())
                    &&  conversationService.isCC(call_id))){
            conversation(state,call_id);
        }else{
            conf(state,call_id);
        }

        return res;
    }

    public void conversation(BusinessState state,String call_id){
        String appId = state.getAppId();
        Map<String,String> businessData = state.getBusinessData();
        String conversation_id = null;
        if(businessData!=null){
            conversation_id = businessData.get(CallCenterUtil.CONVERSATION_FIELD);
        }
        if(StringUtils.isBlank(conversation_id)){
            throw new InvalidParamException("没有找到对应的交谈信息callid={},conversationid={}",call_id,conversation_id);
        }

        if(StringUtils.isBlank(appId)){
            throw new InvalidParamException("没有找到对应的app信息appId={}",appId);
        }
        conversationService.logicExit(conversation_id,call_id);
    }

    public void conf(BusinessState state,String call_id){
        String user_data = state.getUserdata();
        Map<String,String> businessData = state.getBusinessData();
        String conf_id = null;
        if(businessData!=null){
            conf_id = businessData.get("conf_id");
        }
        if(StringUtils.isBlank(conf_id)){
            throw new InvalidParamException("没有找到对应的会议信息callid={},confid={}",call_id,conf_id);
        }

        //开始通知开发者
        if(logger.isDebugEnabled()){
            logger.debug("开始发送会议加入失败通知给开发者");
        }
        if(StringUtils.isNotBlank(state.getCallBackUrl())){
            Map<String,Object> notify_data = new MapBuilder<String,Object>()
                    .putIfNotEmpty("event","conf.joined.fail")
                    .putIfNotEmpty("id",conf_id)
                    .putIfNotEmpty("call_id",call_id)
                    .putIfNotEmpty("user_data",user_data)
                    .build();
            notifyCallbackUtil.postNotify(state.getCallBackUrl(),notify_data,3);
        }
        if(logger.isDebugEnabled()){
            logger.debug("会议加入失败通知发送成功");
        }
        if(logger.isDebugEnabled()){
            logger.debug("处理{}事件完成",getEventName());
        }
    }
}
