package com.lsxy.area.server.event.handler.simplecall;

import com.lsxy.area.api.BusinessState;
import com.lsxy.area.api.BusinessStateService;
import com.lsxy.area.server.event.EventHandler;
import com.lsxy.framework.api.tenant.service.TenantService;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.RPCResponse;
import com.lsxy.framework.rpc.api.event.Constants;
import com.lsxy.framework.rpc.api.session.Session;
import com.lsxy.yunhuni.api.app.service.AppService;
import com.lsxy.yunhuni.api.session.model.CallSession;
import com.lsxy.yunhuni.api.session.model.CaptchaCall;
import com.lsxy.yunhuni.api.session.service.CallSessionService;
import com.lsxy.yunhuni.api.session.service.CaptchaCallService;
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
public class Handler_EVENT_EXT_VERIFY_CALL_SUCCESS extends EventHandler {
    private static final Logger logger = LoggerFactory.getLogger(Handler_EVENT_EXT_VERIFY_CALL_SUCCESS.class);

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
        return Constants.EVENT_EXT_VERIFY_CALL_SUCCESS;
    }

    @Override
    public RPCResponse handle(RPCRequest request, Session session) {
        if(logger.isDebugEnabled()){
            logger.debug("开始处理{}事件,{}",getEventName(),request);
        }
        RPCResponse res = null;
        String call_id = (String)request.getParamMap().get("user_data");
        String res_id = (String)request.getParamMap().get("res_id");
        if(StringUtils.isBlank(call_id)){
            logger.error("call_id is null");
            return res;
        }
        BusinessState state = businessStateService.get(call_id);
        Map<String,Object> busniessData = state.getBusinessData();
        if(state == null){
            logger.error("businessstate is null");
            return res;
        }
        if(res_id!=null){
            state.setResId(res_id);
            businessStateService.save(state);

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
            captchaCall.setFromNum(busniessData!=null&&busniessData.get("from")!=null?(String)busniessData.get("from"):null);
            captchaCall.setToNum(busniessData!=null&&busniessData.get("to")!=null?(String)busniessData.get("to"):null);
            captchaCall.setHangupSide(null);
            captchaCall.setResId(res_id);
            captchaCallService.save(captchaCall);
        }
        return res;
    }
}
