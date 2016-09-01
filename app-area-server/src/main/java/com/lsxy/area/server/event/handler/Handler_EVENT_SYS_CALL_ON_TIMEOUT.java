package com.lsxy.area.server.event.handler;

import com.lsxy.area.api.BusinessStateService;
import com.lsxy.area.server.event.EventHandler;
import com.lsxy.area.server.util.NotifyCallbackUtil;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.RPCResponse;
import com.lsxy.framework.rpc.api.event.Constants;
import com.lsxy.framework.rpc.api.session.Session;
import com.lsxy.yunhuni.api.app.service.AppService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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


    @Override
    public String getEventName() {
        return Constants.EVENT_SYS_CALL_ON_TIMEOUT;
    }

    @Override
    public RPCResponse handle(RPCRequest request, Session session) {
        logger.debug("开始处理{}事件,{}",getEventName(),request);
        return null;
    }
}
