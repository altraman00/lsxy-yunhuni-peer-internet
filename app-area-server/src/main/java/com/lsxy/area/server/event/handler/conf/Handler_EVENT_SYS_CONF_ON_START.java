package com.lsxy.area.server.event.handler.conf;

import com.lsxy.area.api.BusinessState;
import com.lsxy.area.api.BusinessStateService;
import com.lsxy.area.api.ConfService;
import com.lsxy.area.server.event.EventHandler;
import com.lsxy.area.server.service.callcenter.ConversationService;
import com.lsxy.area.server.util.NotifyCallbackUtil;
import com.lsxy.area.server.util.RecordFileUtil;
import com.lsxy.framework.core.exceptions.api.YunhuniApiException;
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

/**
 * Created by liuws on 2016/8/29.
 */
@Component
public class Handler_EVENT_SYS_CONF_ON_START extends EventHandler{

    private static final Logger logger = LoggerFactory.getLogger(Handler_EVENT_SYS_CONF_ON_START.class);

    @Autowired
    private BusinessStateService businessStateService;

    @Autowired
    private AppService appService;

    @Autowired
    private ConfService confService;

    @Autowired
    private NotifyCallbackUtil notifyCallbackUtil;

    @Autowired
    private RPCCaller rpcCaller;

    @Autowired
    private SessionContext sessionContext;

    @Autowired
    private MeetingService meetingService;

    @Autowired
    private ConversationService conversationService;

    @Override
    public String getEventName() {
        return Constants.EVENT_SYS_CONF_ON_START;
    }

    /**
     * 调用会议创建 成功事件
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
        String res_id = (String)params.get("res_id");
        if(StringUtils.isBlank(conf_id)){
            throw new InvalidParamException("conf_id is null");
        }
        BusinessState state = businessStateService.get(conf_id);
        if(state == null){
            throw new InvalidParamException("businessstate is null");
        }
        if(res_id!=null){
            businessStateService.updateResId(conf_id,res_id);
            if(state.getBusinessData().get(BusinessState.REF_RES_ID) == null){
                businessStateService.updateInnerField(conf_id,BusinessState.REF_RES_ID,res_id);
            }
        }
        if(logger.isDebugEnabled()){
            logger.info("confi_id={},state={}",conf_id,state);
        }
        if(BusinessState.TYPE_CC_CONVERSATION.equals(state.getType())){
            conversation(state,conf_id);
        }else{
            conf(state,conf_id,res_id);
        }
        return res;
    }

    public void conversation(BusinessState state,String conversationId){
        String appId = state.getAppId();

        if(StringUtils.isBlank(appId)){
            throw new InvalidParamException("没有找到对应的app信息appId={}",appId);
        }

        String initiator = conversationService.getInitiator(conversationId);
        if(initiator != null){
            try {
                conversationService.join(conversationId,initiator,null,null,null);
            } catch (YunhuniApiException e) {
                logger.info("加入交谈失败:{}",e.getCode());
                conversationService.logicExit(conversationId,initiator);
            }
        }
    }
    public void conf(BusinessState state,String conf_id,String res_id){
        String user_data = state.getUserdata();
        Map<String,String> businessData = state.getBusinessData();

        //开始通知开发者
        if(logger.isDebugEnabled()){
            logger.debug("开始发送会议创建成功通知给开发者");
        }
        if(StringUtils.isNotBlank(state.getCallBackUrl())){
            Map<String,Object> notify_data = new MapBuilder<String,Object>()
                    .putIfNotEmpty("event","conf.create.succ")
                    .putIfNotEmpty("id",conf_id)
                    .putIfNotEmpty("begin_time",System.currentTimeMillis())
                    .putIfNotEmpty("user_data",user_data)
                    .build();
            notifyCallbackUtil.postNotify(state.getCallBackUrl(),notify_data,3);
        }

        if(logger.isDebugEnabled()){
            logger.debug("会议创建成功通知发送成功");
        }
        if(logger.isDebugEnabled()){
            logger.debug("处理{}事件完成",getEventName());
        }
        ifAutoRecording(state,state.getAreaId(),businessData,res_id,conf_id);
        Meeting meeting = meetingService.findById(conf_id);
        if(meeting != null){
            meeting.setResId(res_id);
            meeting.setStartTime(new Date());
            meetingService.save(meeting);
        }
    }
    /**
     * 创建会议是否自动录音
     * @param state
     * @param businessData
     * @param res_id
     * @param conf_id
     */
    private void ifAutoRecording(BusinessState state, String areaId, Map<String, String> businessData, String res_id, String conf_id){
        if(businessData == null){
            return;
        }
        if(!Boolean.parseBoolean(businessData.get("recording"))){
            return;
        }
        Map<String,Object> params = new MapBuilder<String,Object>()
                .putIfNotEmpty("res_id",res_id)
                .putIfNotEmpty("max_seconds",businessData.get("max_seconds"))
                .putIfNotEmpty("record_file", RecordFileUtil.getRecordFileUrl(state.getTenantId(),state.getAppId()))
                .putIfNotEmpty("user_data",conf_id)
                .put("areaId",areaId)
                .build();
        try {
            RPCRequest rpcrequest = RPCRequest.newRequest(ServiceConstants.MN_CH_SYS_CONF_RECORD, params);
            if(!businessStateService.closed(conf_id)) {
                rpcCaller.invoke(sessionContext, rpcrequest);
            }
        } catch (Exception e) {
            logger.error("会议创建自动录音：",e);
        }
    }
}
