package com.lsxy.area.server.event.handler.simplecall;

import com.lsxy.area.api.BusinessState;
import com.lsxy.area.api.BusinessStateService;
import com.lsxy.area.server.event.EventHandler;
import com.lsxy.framework.api.tenant.service.TenantService;
import com.lsxy.framework.core.utils.JSONUtil;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.RPCResponse;
import com.lsxy.framework.rpc.api.event.Constants;
import com.lsxy.framework.rpc.api.session.Session;
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
import java.util.HashMap;
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
        if(logger.isDebugEnabled()){
            logger.debug("开始处理{}事件,{}",getEventName(),request);
        }
        RPCResponse res = null;
        Map<String,Object> params = request.getParamMap();
        if(MapUtils.isEmpty(params)){
            logger.error("request.params is null");
            return res;
        }
        String call_id = (String)params.get("user_data");
        String res_id = (String)params.get("res_id");
        if(StringUtils.isBlank(call_id)){
            logger.error("call_id is null");
            return res;
        }
        BusinessState state = businessStateService.get(call_id);
        if(state == null){
            logger.error("businessstate is null");
            return res;
        }
        Map<String,Object> busniessData = state.getBusinessData();
        if(busniessData == null){
            busniessData = new HashMap<>();
            state.setBusinessData(busniessData);
        }
        //保存会话记录
        CallSession callSession = new CallSession();
        callSession.setStatus(CallSession.STATUS_CALLING);
        callSession.setApp(appService.findById(state.getAppId()));
        callSession.setTenant(tenantService.findById(state.getTenantId()));
        callSession.setRelevanceId(call_id);
        callSession.setType(CallSession.TYPE_VOICE_VOICECODE);
        callSession.setResId(res_id);
        callSessionService.save(callSession);

        CaptchaCall captchaCall = new CaptchaCall();
        captchaCall.setId(call_id);
        captchaCall.setStartTime(new Date());
        captchaCall.setEndTime(null);
        captchaCall.setFromNum((String)busniessData.get("from"));
        captchaCall.setToNum((String)busniessData.get("to"));
        captchaCall.setHangupSide(null);
        captchaCall.setResId(res_id);
        captchaCallService.save(captchaCall);

        state.setResId(res_id);
        busniessData.put("sessionid",callSession.getId());
        businessStateService.save(state);
        return res;
    }
}
