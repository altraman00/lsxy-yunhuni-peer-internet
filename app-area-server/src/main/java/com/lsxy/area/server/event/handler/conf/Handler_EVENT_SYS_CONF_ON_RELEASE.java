package com.lsxy.area.server.event.handler.conf;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsxy.area.api.BusinessState;
import com.lsxy.area.api.BusinessStateService;
import com.lsxy.area.api.ConfService;
import com.lsxy.area.server.event.EventHandler;
import com.lsxy.area.server.service.callcenter.CallCenterUtil;
import com.lsxy.area.server.util.NotifyCallbackUtil;
import com.lsxy.call.center.api.model.CallCenterConversation;
import com.lsxy.call.center.api.service.CallCenterConversationService;
import com.lsxy.framework.core.utils.MapBuilder;
import com.lsxy.framework.mq.api.MQService;
import com.lsxy.framework.mq.events.callcenter.ConversationCompletedEvent;
import com.lsxy.framework.rpc.api.RPCCaller;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.RPCResponse;
import com.lsxy.framework.rpc.api.ServiceConstants;
import com.lsxy.framework.rpc.api.event.Constants;
import com.lsxy.framework.rpc.api.session.Session;
import com.lsxy.framework.rpc.api.session.SessionContext;
import com.lsxy.framework.rpc.exceptions.InvalidParamException;
import com.lsxy.yunhuni.api.app.service.AppService;
import com.lsxy.yunhuni.api.session.model.Meeting;
import com.lsxy.yunhuni.api.session.service.MeetingService;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * Created by liuws on 2016/8/29.
 */
@Component
public class Handler_EVENT_SYS_CONF_ON_RELEASE extends EventHandler{

    private static final Logger logger = LoggerFactory.getLogger(Handler_EVENT_SYS_CONF_ON_RELEASE.class);

    @Autowired
    private RPCCaller rpcCaller;

    @Autowired
    private SessionContext sessionContext;

    @Autowired
    private BusinessStateService businessStateService;

    @Autowired
    private NotifyCallbackUtil notifyCallbackUtil;

    @Autowired
    private AppService appService;

    @Autowired
    private MeetingService meetingService;

    @Autowired
    private ConfService confService;

    @Reference(lazy = true,check = false,timeout = 3000)
    private CallCenterConversationService callCenterConversationService;

    @Autowired
    private CallCenterUtil callCenterUtil;

    @Autowired
    private MQService mqService;

    @Override
    public String getEventName() {
        return Constants.EVENT_SYS_CONF_ON_RELEASE;
    }

    /**
     * 处理会议解散事件
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
        String conf_id = (String)params.get("user_data");
        if(StringUtils.isBlank(conf_id)){
            throw new InvalidParamException("conf_id is null");
        }
        BusinessState state = businessStateService.get(conf_id);
        if(state == null){
            throw new InvalidParamException("businessstate is null");
        }

        businessStateService.delete(conf_id);

        if(logger.isDebugEnabled()){
            logger.info("confi_id={},state={}",conf_id,state);
        }
        if(BusinessState.TYPE_CC_CONVERSATION.equals(state.getType())){
            conversation(state,params,conf_id);
        }else{
            conf(state,params,conf_id);
        }

        return res;
    }

    private void conversation(BusinessState state, Map<String, Object> params, String conversation_id) {
        CallCenterConversation conversation = null;
        try{
            conversation = callCenterConversationService.findById(conversation_id);
            if(conversation != null){
                CallCenterConversation updateCallCenterConversation = new CallCenterConversation();
                updateCallCenterConversation.setEndTime(new Date());
                updateCallCenterConversation.setState(CallCenterConversation.STATE_COMPLETED);
                callCenterConversationService.update(conversation.getId(),updateCallCenterConversation);
            }

        }catch (Throwable t){
            logger.warn("更新交谈记录失败",t);
            mqService.publish(new ConversationCompletedEvent(conversation_id));
        }
        callCenterUtil.conversationEndEvent(state.getCallBackUrl(),conversation_id,
                CallCenterUtil.CONVERSATION_TYPE_QUEUE,
                conversation!=null?conversation.getStartTime().getTime():null,null,null,null,null,null,null);
    }

    private void conf(BusinessState state,Map<String,Object> params,String conf_id){
        String user_data = state.getUserdata();
        Map<String,String> businessData = state.getBusinessData();
        Boolean auto_hangup = Boolean.FALSE;
        if(businessData!=null){
            auto_hangup = Boolean.parseBoolean(businessData.get("auto_hangup"));
        }
        if(auto_hangup != null && auto_hangup){
            handupParts(conf_id);
        }
        //开始通知开发者
        if(logger.isDebugEnabled()){
            logger.debug("开始发送会议解散通知给开发者");
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
                    .putIfNotEmpty("event","conf.end")
                    .putIfNotEmpty("id",conf_id)
                    .putIfNotEmpty("begin_time",begin_time)
                    .putIfNotEmpty("end_time",end_time)
                    .putIfNotEmpty("end_by",null)
                    .putIfNotEmpty("record_files",null)
                    .putIfNotEmpty("user_data",user_data)
                    .build();
            notifyCallbackUtil.postNotify(state.getCallBackUrl(),notify_data,3);
        }

        if(logger.isDebugEnabled()){
            logger.debug("会议解散通知发送成功");
        }
        if(logger.isDebugEnabled()){
            logger.debug("处理{}事件完成",getEventName());
        }

        try{
            Meeting meeting = meetingService.findById(conf_id);
            if(meeting!=null){
                meeting.setEndTime(new Date());
                meetingService.save(meeting);
            }
        }catch (Throwable t){
            logger.error("更新会议记录失败",t);
        }
    }
    private void handupParts(String confId) {
        logger.info("开始处理会议={}解散自动挂断与会方",confId);
        Set<String> parts = confService.popParts(confId);
        if(parts!=null && parts.size()>0){
            for (String callId : parts) {
                handup(callId);
            }
        }
        logger.info("处理完成，会议={}解散自动挂断与会方",confId);
    }

    private void handup(String callId){
        BusinessState state = businessStateService.get(callId);
        if(state == null){
            logger.info("会议结束自动挂断与会方={}失败,state=null",callId);
            return;
        }
        String res_id = state.getResId();
        if(res_id == null){
            logger.info("会议结束自动挂断与会方={}失败,res_id=null",callId);
            return;
        }
        if(state.getClosed() !=null && state.getClosed()){
            logger.info("会议结束自动挂断与会方={}失败,与会方已经挂断了",callId);
            return;
        }
        try {
            Map<String, Object> params = new MapBuilder<String,Object>()
                    .putIfNotEmpty("res_id",res_id)
                    .putIfNotEmpty("user_data",callId)
                    .put("areaId",state.getAreaId())
                    .build();
            RPCRequest rpcrequest = RPCRequest.newRequest(ServiceConstants.MN_CH_SYS_CALL_DROP, params);
            rpcCaller.invoke(sessionContext, rpcrequest,true);
        } catch (Throwable e) {
            logger.error("会议结束自动挂断与会方={}失败",e);
        }
    }
}
