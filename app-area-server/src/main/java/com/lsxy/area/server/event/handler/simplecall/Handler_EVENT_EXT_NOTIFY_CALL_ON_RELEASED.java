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
import com.lsxy.yunhuni.api.session.model.CallSession;
import com.lsxy.yunhuni.api.session.model.CaptchaCall;
import com.lsxy.yunhuni.api.session.model.NotifyCall;
import com.lsxy.yunhuni.api.session.service.CallSessionService;
import com.lsxy.yunhuni.api.session.service.CaptchaCallService;
import com.lsxy.yunhuni.api.session.service.NotifyCallService;
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
public class Handler_EVENT_EXT_NOTIFY_CALL_ON_RELEASED extends EventHandler {
    private static final Logger logger = LoggerFactory.getLogger(Handler_EVENT_EXT_NOTIFY_CALL_ON_RELEASED.class);

    @Autowired
    private BusinessStateService businessStateService;

    @Autowired
    private NotifyCallbackUtil notifyCallbackUtil;

    @Autowired
    NotifyCallService notifyCallService;
    @Autowired
    CallSessionService callSessionService;

    @Autowired
    private CaptchaCallService captchaCallService;

    @Override
    public String getEventName() {
        return Constants.EVENT_EXT_NOTIFY_CALL_ON_RELEASED;
    }

    @Override
    public RPCResponse handle(RPCRequest request, Session session) {
        if(logger.isDebugEnabled()){
            logger.debug("开始处理{}事件,{}",getEventName(),request);
        }
        RPCResponse res = null;
        Map<String, Object> paramMap = request.getParamMap();
        String callId = (String)paramMap.get("user_data");
        if(StringUtils.isBlank(callId)){
            logger.info("call_id is null");
            return res;
        }
        BusinessState state = businessStateService.get(callId);
        if(state == null){
            logger.info("businessstate is null");
            return res;
        }

        if(state.getType().equalsIgnoreCase("notify_call")){
            return notifyCall(callId,paramMap,state);
        }

        if(state.getType().equalsIgnoreCase("verify_call")){
            return verifyCall(callId,paramMap,state);
        }
        return res;
    }

    private RPCResponse verifyCall(String call_id, Map<String, Object> paramMap, BusinessState state) {
        RPCResponse res = null;
        String appId = state.getAppId();
        String user_data = state.getUserdata();
        if(StringUtils.isBlank(appId)){
            logger.info("appId为空");
            return res;
        }
        String callBackUrl = state.getCallBackUrl();

        //开始通知开发者
        if(logger.isDebugEnabled()){
            logger.debug("用户回调结束事件");
        }
        Date beginTime = null;
        Date answerTime = null;
        Date endTime = null;
        String begin_time = (String) paramMap.get("begin_time");
        if(StringUtils.isNotBlank(begin_time) && !"null".equals(begin_time)){
            beginTime = new Date(Long.parseLong(begin_time) * 1000);
        }
        String answer_time = (String) paramMap.get("answer_time");
        if(StringUtils.isNotBlank(answer_time) && !"null".equals(answer_time)){
            answerTime = new Date(Long.parseLong(answer_time) * 1000);
        }
        String end_time = (String) paramMap.get("end_time");
        if(StringUtils.isNotBlank(end_time) && !"null".equals(end_time)){
            endTime = new Date(Long.parseLong(end_time) * 1000);
        }

        if(StringUtils.isNotBlank(callBackUrl)){
            Map<String,Object> notify_data = new MapBuilder<String,Object>()
                    .putIfNotEmpty("event","verify_call.end")
                    .putIfNotEmpty("id",call_id)
                    .putIfNotEmpty("begin_time",beginTime==null?null:beginTime.getTime())
                    .putIfNotEmpty("answer",answerTime==null?null:answerTime.getTime())
                    .putIfNotEmpty("end_time",endTime==null?null:endTime.getTime())
                    .putIfNotEmpty("hangup_by",paramMap.get("dropped_by"))
                    .putIfNotEmpty("reason",paramMap.get("reason"))
                    .putIfNotEmpty("error",paramMap.get("error"))
                    .putIfNotEmpty("user_data",user_data)
                    .build();
            notifyCallbackUtil.postNotify(callBackUrl,notify_data,3);
        }
        if(logger.isDebugEnabled()){
            logger.debug("语音验证码结束事件");
        }
        if(logger.isDebugEnabled()){
            logger.debug("处理{}事件完成",getEventName());
        }

        //更新会话状态
        callSessionService.updateStatusByRelevanceId(call_id, CallSession.STATUS_OVER);

        //更新语音验证码记录表状态
        CaptchaCall captchaCall = captchaCallService.findById(call_id);
        if(captchaCall!=null){
            captchaCall.setHangupSide((String)paramMap.get("dropped_by"));
            captchaCall.setEndTime(endTime!=null?endTime:new Date());
            if(answerTime != null){
                captchaCall.setAnswerTime(answerTime);
            }
            captchaCallService.save(captchaCall);
        }
        businessStateService.delete(call_id);
        return res;
    }

    private RPCResponse notifyCall(String callId,Map<String, Object> paramMap,BusinessState state){
        RPCResponse res = null;
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
        Date endTime = null;
        String end_time = (String) paramMap.get("end_time");
        if(StringUtils.isNotBlank(end_time) && !"null".equals(end_time)){
            endTime = new Date(Long.parseLong(end_time) * 1000);
        }
        //处理语音通知表数据
        NotifyCall notifyCall = notifyCallService.findById(callId);
        if(notifyCall != null){
            notifyCall.setStartTime(beginTime);
            notifyCall.setAnswerTime(answerTime);
            notifyCall.setEndTime(endTime);
            notifyCall.setHangupSide((String) paramMap.get("dropped_by"));
            notifyCallService.save(notifyCall);
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
                .put("event","notify_call.end")
                .put("id",callId)
                .put("begin_time",beginTime==null?null:beginTime.getTime())
                .put("answer_time",answerTime==null?null:answerTime.getTime())
                .put("end_time",endTime==null?null:endTime.getTime())
                .put("dropped_by",paramMap.get("dropped_by"))
                .put("reason",paramMap.get("reason"))
                .put("error",paramMap.get("error"))
                .put("user_data",user_data)
                .build();
        notifyCallbackUtil.postNotify(callBackUrl,notify_data,3);
        if(logger.isDebugEnabled()){
            logger.debug("通知外呼结束事件");
        }
        if(logger.isDebugEnabled()){
            logger.debug("处理{}事件完成",getEventName());
        }
        return res;
    }
}
