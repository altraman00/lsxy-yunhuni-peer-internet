package com.lsxy.area.server.event.handler;

import com.lsxy.area.api.BusinessStateService;
import com.lsxy.area.server.event.EventHandler;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.RPCResponse;
import com.lsxy.framework.rpc.api.event.Constants;
import com.lsxy.framework.rpc.api.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by liuws on 2016/8/29.
 */
@Component
public class Handler_EVENT_SYS_ON_CHAN_CLOSED extends EventHandler{

    private static final Logger logger = LoggerFactory.getLogger(Handler_EVENT_SYS_ON_CHAN_CLOSED.class);

    @Autowired
    private BusinessStateService businessStateService;

    @Override
    public String getEventName() {
        return Constants.SYS_ON_CHAN_CLOSED;
    }

    @Override
    public RPCResponse handle(RPCRequest request, Session session) {
        logger.info("正在处理{}",getEventName());
        Object cdr = request.getParamMap().get("args");
        if(logger.isDebugEnabled()){
            logger.info("开始处理CDR数据：{}",cdr);
        }
        return null;
    }
}
