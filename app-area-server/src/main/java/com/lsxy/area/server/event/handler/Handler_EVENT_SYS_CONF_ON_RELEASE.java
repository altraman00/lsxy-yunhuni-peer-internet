package com.lsxy.area.server.event.handler;

import com.lsxy.area.api.BusinessState;
import com.lsxy.area.api.BusinessStateService;
import com.lsxy.area.server.StasticsCounter;
import com.lsxy.area.server.event.EventHandler;
import com.lsxy.area.server.test.TestIncomingZB;
import com.lsxy.framework.rpc.api.RPCCaller;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.RPCResponse;
import com.lsxy.framework.rpc.api.SessionContext;
import com.lsxy.framework.rpc.api.event.Constants;
import com.lsxy.framework.rpc.api.server.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by liuws on 2016/8/29.
 */
@Component
public class Handler_EVENT_SYS_CONF_ON_RELEASE extends EventHandler{

    private static final Logger logger = LoggerFactory.getLogger(Handler_EVENT_SYS_CONF_ON_RELEASE.class);

    @Autowired
    private BusinessStateService businessStateService;


    @Override
    public String getEventName() {
        return Constants.EVENT_SYS_CONF_ON_RELEASE;
    }

    @Override
    public RPCResponse handle(RPCRequest request, Session session) {
        String conf_id = (String)request.getParamMap().get("user_data");
        BusinessState state = businessStateService.get(conf_id);
        logger.info("confi_id={},state={}",conf_id,state);
        logger.info("会议结束事件");
        return null;
    }
}
