package com.lsxy.area.server.event.handler.simplecall;

import com.lsxy.area.api.BusinessState;
import com.lsxy.area.api.BusinessStateService;
import com.lsxy.area.server.event.EventHandler;
import com.lsxy.framework.api.tenant.service.TenantService;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.RPCResponse;
import com.lsxy.framework.rpc.api.event.Constants;
import com.lsxy.framework.rpc.api.session.Session;
import com.lsxy.framework.rpc.exceptions.InvalidParamException;
import com.lsxy.yunhuni.api.app.service.AppService;
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
public class Handler_EVENT_EXT_CAPTCHA_CALL_SUCCESS extends EventHandler {
    private static final Logger logger = LoggerFactory.getLogger(Handler_EVENT_EXT_CAPTCHA_CALL_SUCCESS.class);

    @Autowired
    private BusinessStateService businessStateService;

    @Autowired
    private CallSessionService callSessionService;

    @Autowired
    private CaptchaCallService captchaCallService;

    @Autowired
    private AppService appService;

    @Autowired
    private TenantService tenantService;

    @Override
    public String getEventName() {
        return Constants.EVENT_EXT_CAPTCHA_CALL_SUCCESS;
    }

    @Override
    public RPCResponse handle(RPCRequest request, Session session) {
        RPCResponse res = null;
        Map<String,Object> params = request.getParamMap();
        if(MapUtils.isEmpty(params)){
            throw new InvalidParamException("request.params is null");
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
        businessStateService.updateResId(call_id,res_id);
        Map<String,String> busniessData = state.getBusinessData();
        //保存会话记录
        CallSession callSession = new CallSession();
        callSession.setFromNum(busniessData.get("from"));
        callSession.setToNum(busniessData.get("to"));
        callSession.setStatus(CallSession.STATUS_CALLING);
        callSession.setAppId(state.getAppId());
        callSession.setTenantId(state.getTenantId());
        callSession.setRelevanceId(call_id);
        callSession.setType(CallSession.TYPE_VOICE_VOICECODE);
        callSession.setResId(res_id);
        callSessionService.save(callSession);

        CaptchaCall captchaCall = new CaptchaCall();
        captchaCall.setId(call_id);
        captchaCall.setAppId(state.getAppId());
        captchaCall.setTenantId(state.getTenantId());
        captchaCall.setStartTime(new Date());
        captchaCall.setEndTime(null);
        captchaCall.setFromNum(busniessData.get("from"));
        captchaCall.setToNum(busniessData.get("to"));
        captchaCall.setHangupSide(null);
        captchaCall.setResId(res_id);
        captchaCallService.save(captchaCall);
        businessStateService.updateInnerField(call_id,BusinessState.SESSIONID,callSession.getId());
        return res;
    }
}
