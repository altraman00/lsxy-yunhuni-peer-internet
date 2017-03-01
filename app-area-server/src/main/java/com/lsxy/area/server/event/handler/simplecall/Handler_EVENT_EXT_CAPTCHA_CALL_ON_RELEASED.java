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
import com.lsxy.yunhuni.api.session.model.CaptchaCall;
import com.lsxy.yunhuni.api.session.service.CallSessionService;
import com.lsxy.yunhuni.api.session.service.CaptchaCallService;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * Created by liups on 2016/8/31.
 */
@Component
public class Handler_EVENT_EXT_CAPTCHA_CALL_ON_RELEASED extends EventHandler {
    private static final Logger logger = LoggerFactory.getLogger(Handler_EVENT_EXT_CAPTCHA_CALL_ON_RELEASED.class);

    @Autowired
    private BusinessStateService businessStateService;

    @Autowired
    private CallSessionService callSessionService;

    @Autowired
    private CaptchaCallService captchaCallService;

    @Autowired
    private NotifyCallbackUtil notifyCallbackUtil;

    @Override
    public String getEventName() {
        return Constants.EVENT_EXT_CAPTCHA_CALL_ON_RELEASED;
    }

    @Override
    public RPCResponse handle(RPCRequest request, Session session) {
        RPCResponse res = null;
        Map<String, Object> paramMap = request.getParamMap();
        if(MapUtils.isEmpty(paramMap)){
            throw new InvalidParamException("request.params is null");
        }
        String call_id = (String)paramMap.get("user_data");
        if(StringUtils.isBlank(call_id)){
            throw new InvalidParamException("call_id is null");
        }
        BusinessState state = businessStateService.get(call_id);
        if(state == null){
            throw new InvalidParamException("businessstate is null");
        }
        String appId = state.getAppId();
        String user_data = state.getUserdata();
        if(StringUtils.isBlank(appId)){
            throw new InvalidParamException("appId为空");
        }
        String callBackUrl = state.getCallBackUrl();
        Long begin_time = null;
        Long end_time = null;
        Long answer_time = null;
        if(StringUtils.isNotBlank(callBackUrl)){
            if(paramMap.get("begin_time") != null){
                begin_time = ((long)paramMap.get("begin_time")) * 1000;
            }
            if(paramMap.get("end_time") != null){
                end_time = ((long)paramMap.get("end_time")) * 1000;
            }
            if(paramMap.get("answer_time") != null){
                answer_time = ((long)paramMap.get("answer_time")) * 1000;
            }
            Map<String,Object> notify_data = new MapBuilder<String,Object>()
                    .putIfNotEmpty("event","captcha_call.end")
                    .putIfNotEmpty("id",call_id)
                    .putIfNotEmpty("subaccount_id",state.getSubaccountId())
                    .putIfNotEmpty("begin_time",begin_time)
                    .putIfNotEmpty("answer_time",answer_time)
                    .putIfNotEmpty("end_time",end_time)
                    .putIfNotEmpty("keys",paramMap.get("keys"))
                    .putIfNotEmpty("duration",paramMap.get("answer_time"))
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
            captchaCall.setEndTime(end_time!=null?new Date(end_time):new Date());
            if(answer_time != null){
                captchaCall.setAnswerTime(new Date(answer_time));
            }
            captchaCallService.save(captchaCall);
        }
        return res;
    }
}
