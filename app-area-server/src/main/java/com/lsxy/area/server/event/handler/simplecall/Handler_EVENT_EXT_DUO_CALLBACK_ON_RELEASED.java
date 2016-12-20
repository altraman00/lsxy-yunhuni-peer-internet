package com.lsxy.area.server.event.handler.simplecall;

import com.lsxy.area.api.BusinessState;
import com.lsxy.area.api.BusinessStateService;
import com.lsxy.area.server.event.EventHandler;
import com.lsxy.area.server.util.NotifyCallbackUtil;
import com.lsxy.framework.core.utils.MapBuilder;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.RPCResponse;
import com.lsxy.framework.rpc.api.event.Constants;
import com.lsxy.framework.rpc.api.session.Session;
import com.lsxy.framework.rpc.exceptions.InvalidParamException;
import com.lsxy.yunhuni.api.session.model.CallSession;
import com.lsxy.yunhuni.api.session.model.VoiceCallback;
import com.lsxy.yunhuni.api.session.service.CallSessionService;
import com.lsxy.yunhuni.api.session.service.VoiceCallbackService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
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
        RPCResponse res = null;
        Map<String, Object> paramMap = request.getParamMap();
        String callId = (String)paramMap.get("user_data1");
        if(StringUtils.isBlank(callId)){
            throw new InvalidParamException("call_id is null");
        }
        BusinessState state = businessStateService.get(callId);
        if(state == null){
            throw new InvalidParamException("businessstate is null");
        }

        //释放资源
        businessStateService.delete(callId);

        //处理返回数据的各个时间
        Date beginTime = null;
        String begin_time = (String) paramMap.get("begin_time");
        if(StringUtils.isNotBlank(begin_time) && !"null".equals(begin_time)){
            beginTime = new Date(Long.parseLong(begin_time) * 1000);
        }
        Date answerTime = null;
        String answer_time = (String) paramMap.get("answer_time");
        if(StringUtils.isNotBlank(answer_time) && !"null".equals(answer_time)){
            answerTime = new Date(Long.parseLong(answer_time) * 1000);
        }
        Date connectTime = null;
        String connect_time = (String) paramMap.get("connect_time");
        if(StringUtils.isNotBlank(connect_time) && !"null".equals(connect_time)){
            connectTime = new Date(Long.parseLong(connect_time) * 1000);
        }
        Date endTime = null;
        String end_time = (String) paramMap.get("end_time");
        if(StringUtils.isNotBlank(end_time) && !"null".equals(end_time)){
            endTime = new Date(Long.parseLong(end_time) * 1000);
        }

        //处理双向回拔表数据
        VoiceCallback duoCall = voiceCallbackService.findById(callId);
        if(duoCall != null){
            duoCall.setStartTime(beginTime);
            duoCall.setAnswerTime(answerTime);
            duoCall.setConnectTime(connectTime);
            duoCall.setEndTime(endTime);
            voiceCallbackService.save(duoCall);
        }
        //处理会话表数据
        Map<String, String> data = state.getBusinessData();
        Set<Map.Entry<String, String>> entries = data.entrySet();
        for(Map.Entry<String,String> entry:entries){
            String sessionId = entry.getValue();
            CallSession callSession = callSessionService.findById(sessionId);
            if(callSession != null){
                callSession.setStatus(CallSession.STATUS_OVER);
                callSessionService.save(callSession);
            }
        }
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
                .put("begin_time",beginTime==null?null:beginTime.getTime())
                .put("answer_time1",answerTime==null?null:answerTime.getTime())
                .put("answer_time2",connectTime==null?null:connectTime.getTime())
                .put("end_time",endTime==null?null:endTime.getTime())
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
