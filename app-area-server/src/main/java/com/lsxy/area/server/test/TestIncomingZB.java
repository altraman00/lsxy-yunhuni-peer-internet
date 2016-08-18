package com.lsxy.area.server.test;

import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.framework.core.utils.StringUtil;
import com.lsxy.framework.rpc.api.RPCRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Created by tandy on 16/8/18.
 */
@Component
public class TestIncomingZB {

    private static final Logger logger = LoggerFactory.getLogger(TestIncomingZB.class);
    @Autowired
    private RedisCacheService redisCacheService;

    @Async
    public void doCallZB(String to, RPCRequest rpcrequest) {
        if(logger.isDebugEnabled()){
            logger.debug("请求语音呼叫指令,存入REDIS:{}" ,rpcrequest);
        }
        redisCacheService.set(to,rpcrequest.toString());
    }


    @Async
    public void receivedIncoming(RPCRequest request) {
        String to = (String) request.getParameter("to_uri");
        if(StringUtil.isNotEmpty(to)){
            to = to.substring(4);
        }
        String requestSTring = redisCacheService.get(to);
        if(logger.isDebugEnabled()){
            logger.debug("收到incomint ,到缓存里面查到数据:{}",requestSTring);
        }
    }
}
