package com.lsxy.area.server.event.handler.simplecallevent;

import com.lsxy.area.api.BusinessState;
import com.lsxy.area.api.BusinessStateService;
import com.lsxy.area.server.event.EventHandler;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.RPCResponse;
import com.lsxy.framework.rpc.api.event.Constants;
import com.lsxy.framework.rpc.api.session.Session;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by liups on 2016/8/31.
 */
@Component
public class Handler_EVENT_EXT_NOTIFY_CALL_SUCCESS extends EventHandler {
    private static final Logger logger = LoggerFactory.getLogger(Handler_EVENT_EXT_NOTIFY_CALL_SUCCESS.class);

    @Autowired
    private BusinessStateService businessStateService;

    @Override
    public String getEventName() {
        return Constants.EVENT_EXT_DUO_CALLBACK_SUCCESS;
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
        if(state == null){
            logger.error("businessstate is null");
            return res;
        }
        if(res_id!=null){
            state.setResId(res_id);
            businessStateService.save(state);
        }
        return res;
    }
}
