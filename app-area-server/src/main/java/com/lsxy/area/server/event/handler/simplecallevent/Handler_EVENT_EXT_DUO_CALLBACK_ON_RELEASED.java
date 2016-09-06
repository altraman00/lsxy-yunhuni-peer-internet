package com.lsxy.area.server.event.handler.simplecallevent;

import com.lsxy.area.api.BusinessState;
import com.lsxy.area.api.BusinessStateService;
import com.lsxy.area.server.event.EventHandler;
import com.lsxy.area.server.util.NotifyCallbackUtil;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.MapBuilder;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.RPCResponse;
import com.lsxy.framework.rpc.api.event.Constants;
import com.lsxy.framework.rpc.api.session.Session;
import com.lsxy.yunhuni.api.session.model.CallSession;
import com.lsxy.yunhuni.api.session.model.VoiceCallback;
import com.lsxy.yunhuni.api.session.service.CallSessionService;
import com.lsxy.yunhuni.api.session.service.VoiceCallbackService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

/**
 * Created by liups on 2016/8/31.
 */
@Component
public class Handler_EVENT_EXT_DUO_CALLBACK_ON_RELEASED extends EventHandler {
    private static final Logger logger = LoggerFactory.getLogger(Handler_EVENT_EXT_DUO_CALLBACK_ON_RELEASED.class);

    @Autowired
    private BusinessStateService businessStateService;

    @Autowired
    private NotifyCallbackUtil notifyCallbackUtil;
    @Autowired
    VoiceCallbackService voiceCallbackService;
    @Autowired
    CallSessionService callSessionService;

    @Override
    public String getEventName() {
        return Constants.EVENT_EXT_DUO_CALLBACK_ON_RELEASED;
    }

    @Override
    public RPCResponse handle(RPCRequest request, Session session) {
        if(logger.isDebugEnabled()){
            logger.debug("开始处理{}事件,{}",getEventName(),request);
        }
        RPCResponse res = null;
        Map<String, Object> paramMap = request.getParamMap();
        String callId = (String)paramMap.get("user_data1");
        if(StringUtils.isBlank(callId)){
            logger.info("call_id is null");
            return res;
        }
        BusinessState state = businessStateService.get(callId);
        if(state == null){
            logger.info("businessstate is null");
            return res;
        }
        //处理双向回拔表数据
        VoiceCallback duoCall = voiceCallbackService.findById(callId);
        if(duoCall != null){
            duoCall.setStartTime(DateUtils.parseDate((String) paramMap.get("begin_time")));
            duoCall.setAnswerTime(DateUtils.parseDate((String) paramMap.get("answer_time")));
            duoCall.setConnectTime(DateUtils.parseDate((String) paramMap.get("connect_time")));
            duoCall.setEndTime(DateUtils.parseDate((String) paramMap.get("end_time")));
            voiceCallbackService.save(duoCall);
        }
        //处理会话表数据
        Map<String, Object> data = state.getBusinessData();
        Set<Map.Entry<String, Object>> entries = data.entrySet();
        for(Map.Entry entry:entries){
            String sessionId = (String) entry.getValue();
            CallSession callSession = callSessionService.findById(sessionId);
            if(callSession != null){
                callSession.setStatus(CallSession.STATUS_OVER);
                callSessionService.save(callSession);
            }
        }
        //释放资源
        businessStateService.delete(callId);
        String appId = state.getAppId();
        String user_data = state.getUserdata();
        if(StringUtils.isBlank(appId)){
            logger.info("appId为空");
            return res;
        }
        String callBackUrl = state.getCallBackUrl();
        if(StringUtils.isBlank(callBackUrl)){
            logger.info("回调地址callBackUrl为空");
            return res;
        }
        //开始通知开发者
        if(logger.isDebugEnabled()){
            logger.debug("用户回调结束事件");
        }
        Map<String,Object> notify_data = new MapBuilder<String,Object>()
                .put("event","duo_callback.end")
                .put("id",callId)
                .put("begin_time",paramMap.get("begin_time"))
                .put("answer_time1",paramMap.get("answer_time"))
                .put("answer_time2",paramMap.get("connect_time"))
                .put("end_time",paramMap.get("end_time"))
                .put("hangup_by",paramMap.get("hangup_by"))
                .put("reason",paramMap.get("reason"))
                .put("error",paramMap.get("error"))
                .put("user_data",user_data)
                .build();
        notifyCallbackUtil.postNotify(callBackUrl,notify_data,3);


        if(logger.isDebugEnabled()){
            logger.debug("双向回拔结束事件");
        }
        if(logger.isDebugEnabled()){
            logger.debug("处理{}事件完成",getEventName());
        }
        return res;
    }
}
