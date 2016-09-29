package com.lsxy.area.server.event.handler.simplecall;

import com.lsxy.area.api.BusinessState;
import com.lsxy.area.api.BusinessStateService;
import com.lsxy.area.server.event.EventHandler;
import com.lsxy.area.server.util.NotifyCallbackUtil;
import com.lsxy.framework.core.utils.JSONUtil;
import com.lsxy.framework.core.utils.MapBuilder;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.RPCResponse;
import com.lsxy.framework.rpc.api.event.Constants;
import com.lsxy.framework.rpc.api.session.Session;
import com.lsxy.yunhuni.api.app.service.AppService;
import com.lsxy.yunhuni.api.product.enums.ProductCode;
import com.lsxy.yunhuni.api.session.model.CallSession;
import com.lsxy.yunhuni.api.session.service.CallSessionService;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

/**
 * Created by liuws on 2016/8/29.
 */
@Component
public class Handler_EVENT_EXT_CALL_ON_TIMEOUT extends EventHandler{

    private static final Logger logger = LoggerFactory.getLogger(Handler_EVENT_EXT_CALL_ON_TIMEOUT.class);

    @Autowired
    private BusinessStateService businessStateService;

    @Autowired
    private NotifyCallbackUtil notifyCallbackUtil;

    @Autowired
    CallSessionService callSessionService;

    @Override
    public String getEventName() {
        return Constants.EVENT_EXT_CALL_ON_TIMEOUT;
    }

    @Override
    public RPCResponse handle(RPCRequest request, Session session) {

        if(logger.isDebugEnabled()){
            logger.debug("开始处理{}事件,{}",getEventName(),request);
        }
        RPCResponse res = null;
        Map<String, Object> params = request.getParamMap();
        if(MapUtils.isEmpty(params)){
            logger.error("request.params is null");
            return res;
        }
        if(logger.isDebugEnabled()){
            logger.debug("返回数据map,{}", JSONUtil.objectToJson(params));
        }
        String callId = (String)params.get("user_data");
        if(StringUtils.isBlank(callId)){
            callId = (String)params.get("user_data1");
            if(StringUtils.isBlank(callId)){
                callId = (String)params.get("user_data2");
                if(StringUtils.isBlank(callId)){
                    logger.error("businessstate is null");
                    return res;
                }
            }
        }
        BusinessState state = businessStateService.get(callId);
        if(state == null){
            logger.error("businessstate is null");
            return res;
        }

        ProductCode productCode = ProductCode.changeApiCmdToProductCode(state.getType());
        String event;
        switch (productCode){
            case duo_call:{
                Map<String, Object> data = state.getBusinessData();
                Set<Map.Entry<String, Object>> entries = data.entrySet();
                for(Map.Entry entry:entries){
                    String sessionId = (String) entry.getValue();
                    CallSession callSession = callSessionService.findById(sessionId);
                    if(callSession != null){
                        callSession.setStatus(CallSession.STATUS_EXCEPTION);
                        callSessionService.save(callSession);
                    }
                }
                event = "duo_callback.end";
                break;
            }
            case notify_call:{
                //处理会话表数据
                CallSession callSession = callSessionService.findById((String)state.getBusinessData().get("sessionid"));
                if(callSession != null){
                    callSession.setStatus(CallSession.STATUS_EXCEPTION);
                    callSessionService.save(callSession);
                }
                event = "notify_call.end";
                break;
            }
            case captcha_call:
                //处理会话表数据
                CallSession callSession = callSessionService.findById((String)state.getBusinessData().get("sessionid"));
                if(callSession != null){
                    callSession.setStatus(CallSession.STATUS_EXCEPTION);
                    callSessionService.save(callSession);
                }
                event = "verify_call.end";
                break;
            default:
                return res;
        }

        //释放资源
        businessStateService.delete(callId);

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
                .put("event",event)
                .put("id",callId)
                .put("error","调用失败")
                .put("user_data",state.getUserdata())
                .build();
        notifyCallbackUtil.postNotify(callBackUrl,notify_data,3);

        if(logger.isDebugEnabled()){
            logger.debug("处理{}事件完成",event);
        }
        return res;
    }
}
