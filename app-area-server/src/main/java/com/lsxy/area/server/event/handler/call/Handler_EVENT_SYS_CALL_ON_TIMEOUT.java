package com.lsxy.area.server.event.handler.call;

import com.lsxy.area.api.BusinessState;
import com.lsxy.area.api.BusinessStateService;
import com.lsxy.area.server.event.EventHandler;
import com.lsxy.area.server.service.ivr.IVRActionService;
import com.lsxy.area.server.util.NotifyCallbackUtil;
import com.lsxy.framework.core.utils.MapBuilder;
import com.lsxy.framework.core.utils.StringUtil;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.RPCResponse;
import com.lsxy.framework.rpc.api.event.Constants;
import com.lsxy.framework.rpc.api.session.Session;
import com.lsxy.framework.rpc.exceptions.InvalidParamException;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.service.AppService;
import com.lsxy.yunhuni.api.session.model.CallSession;
import com.lsxy.yunhuni.api.session.service.CallSessionService;
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
public class Handler_EVENT_SYS_CALL_ON_TIMEOUT extends EventHandler{

    private static final Logger logger = LoggerFactory.getLogger(Handler_EVENT_SYS_CALL_ON_TIMEOUT.class);

    @Autowired
    private BusinessStateService businessStateService;

    @Autowired
    private AppService appService;

    @Autowired
    private NotifyCallbackUtil notifyCallbackUtil;

    @Autowired
    private CallSessionService callSessionService;

    @Autowired
    private IVRActionService ivrActionService;

    @Override
    public String getEventName() {
        return Constants.EVENT_SYS_CALL_ON_TIMEOUT;
    }

    @Override
    public RPCResponse handle(RPCRequest request, Session session) {
        RPCResponse res = null;
        Map<String,Object> params = request.getParamMap();
        if(MapUtils.isEmpty(params)){
            throw new InvalidParamException("request params is null");
        }
        String call_id = (String)params.get("user_data");
        if(StringUtils.isBlank(call_id)){
            throw new InvalidParamException("call_id is null");
        }
        BusinessState state = businessStateService.get(call_id);
        if(state == null){
            throw new InvalidParamException("businessstate is null");
        }

        if(logger.isDebugEnabled()){
            logger.info("call_id={},state={}",call_id,state);
        }

        //更新会话记录状态
        CallSession callSession = callSessionService.findById((String)state.getBusinessData().get("sessionid"));
        if(callSession != null){
            callSession.setStatus(CallSession.STATUS_EXCEPTION);
            callSessionService.save(callSession);
        }

        if(BusinessState.TYPE_IVR_DIAL.equals(state.getType())){//ivr拨号失败需要继续ivr
            Map<String,Object> businessData = state.getBusinessData();
            if(businessData != null){
                String ivr_call_id = (String)businessData.get("ivr_call_id");
                if(StringUtil.isNotEmpty(ivr_call_id)){
                    App app = appService.findById(state.getAppId());
                    Map<String,Object> notify_data = new MapBuilder<String,Object>()
                            .putIfNotEmpty("event","ivr.connect_end")
                            .putIfNotEmpty("id",ivr_call_id)
                            .putIfNotEmpty("begin_time",System.currentTimeMillis())
                            .putIfNotEmpty("end_time",System.currentTimeMillis())
                            .putIfNotEmpty("error","dial error")
                            .build();
                    if(notifyCallbackUtil.postNotifySync(app.getUrl(),notify_data,null,3)){
                        ivrActionService.doAction(ivr_call_id);
                    }
                }
            }
        }
        return res;
    }
}
