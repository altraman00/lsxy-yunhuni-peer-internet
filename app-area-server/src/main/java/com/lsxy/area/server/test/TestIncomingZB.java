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

//    @Async
    public void doCallZB(String to, RPCRequest rpcrequest) {
        long timestamp = rpcrequest.getTimestamp();
        if(logger.isDebugEnabled()){
            logger.debug("请求语音呼叫指令,存入REDIS:{} - {}  - " ,to,timestamp);
        }
        redisCacheService.set(to,timestamp+"");
    }


//    @Async
    public void receivedIncoming(RPCRequest request) {
        String to = (String) request.getParameter("to_uri");
        if(StringUtil.isNotEmpty(to)){
            to = to.substring(4);
        }
        String requestTimestamp = redisCacheService.get(to);
        long now = System.currentTimeMillis();
        if(StringUtil.isNotEmpty(requestTimestamp)){
            Long ltt = Long.parseLong(requestTimestamp);
            if(logger.isDebugEnabled()){
                logger.debug("收到incoming[{}],共花费往返[{}] vs [{}]  {}ms",to,now,ltt, now- ltt);
            }
        }else{
            if(logger.isDebugEnabled()){
                logger.debug("我靠  丢了:{}",to);
            }
        }
    }
}
