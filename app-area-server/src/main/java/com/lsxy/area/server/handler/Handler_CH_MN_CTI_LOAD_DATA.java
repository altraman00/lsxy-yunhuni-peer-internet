package com.lsxy.area.server.handler;

import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.RPCResponse;
import com.lsxy.framework.rpc.api.ServiceConstants;
import com.lsxy.framework.rpc.api.handler.RpcRequestHandler;
import com.lsxy.framework.rpc.api.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


/**
 * Created by tandy on 16/12/23.
 * 收到CTI负载数据
 * 记录日志，用于日志统计和跟踪
 */
@Component
public class Handler_CH_MN_CTI_LOAD_DATA extends RpcRequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(Handler_CH_MN_CTI_LOAD_DATA.class);
    @Override
    public String getEventName() {
        return ServiceConstants.CH_MN_CTI_LOAD_DATA;
    }

    /**
     * @param request
     *  node=%s&load=%s&cinNumber=%s&coutNumber=%s&cinCount=%s&coutCount=%s
     * @param session
     * @return
     */
    @Override
    public RPCResponse handle(RPCRequest request, Session session) {
        if(logger.isDebugEnabled()){
            logger.debug("收到CTI负载数据：[node:{},load:{},cinNum:{},coutNum:{},cinCnt:{},coutCnt:{}]",
                    request.getParameter("node"),
                    request.getParameter("load"),
                    request.getParameter("cinNumber"),
                    request.getParameter("coutNumber"),
                    request.getParameter("cinCount"),
                    request.getParameter("coutCount")
            );
        }
        return null;
    }
}
