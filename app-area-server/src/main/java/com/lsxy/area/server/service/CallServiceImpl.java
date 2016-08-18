package com.lsxy.area.server.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsxy.area.api.CallService;
import com.lsxy.area.api.exceptions.InvokeCallException;
import com.lsxy.area.server.StasticsCounter;
import com.lsxy.framework.core.utils.UUIDGenerator;
import com.lsxy.framework.rpc.api.RPCCaller;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.ServiceConstants;
import com.lsxy.framework.rpc.api.server.ServerSessionContext;
import com.lsxy.framework.rpc.api.server.Session;
import com.lsxy.framework.rpc.exceptions.RightSessionNotFoundExcepiton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by tandy on 16/8/18.
 */
@Component
@Service
public class CallServiceImpl implements CallService {

    private static final Logger logger = LoggerFactory.getLogger(CallServiceImpl.class);

    @Autowired(required = false)
    private StasticsCounter cs;

    @Autowired
    private RPCCaller rpcCaller;

    @Autowired
    private ServerSessionContext sessionContext;

    @Override
    public String call(String from, String to, int maxAnswerSec, int maxRingSec) throws InvokeCallException {

        String callid = UUIDGenerator.uuid();
        String params = "to=%s&from=%s&maxAnswerSec=%d&maxRingSec=%d&callid=%s";
        params = String.format(params, to, from, maxAnswerSec, maxRingSec, callid);

        try {
            //找到合适的区域代理
            Session session = sessionContext.getRightSession();
            if (session != null) {

                RPCRequest rpcrequest = RPCRequest.newRequest(ServiceConstants.MN_CH_SYS_CALL, params);
                try {
                    if (logger.isDebugEnabled()) {
                        logger.debug("发送SYS_CALL指令到区域:{}", rpcrequest);
                    }

                    /*发送给区域的请求次数计数*/
                    if (cs != null) cs.getSendAreaNodeRequestCount().incrementAndGet();

                    rpcCaller.invoke(session, rpcrequest);

                    /*呼叫API调用次数计数*/
                    if(cs!=null)cs.getSendAreaNodeSysCallCount().incrementAndGet();
                } catch (Exception e) {
                    logger.error("消息发送到区域失败:{}", rpcrequest);
                    throw new InvokeCallException("消息发送到区域失败:" + rpcrequest);
                }
            } else {
                logger.error("没有找到合适的区域代理处理该请求:sys.call=>{}", params);
                throw new InvokeCallException("没有找到合适的区域代理处理该请求:sys.call=>" + params);
            }
            return callid;
        }catch(RightSessionNotFoundExcepiton ex){
            throw new InvokeCallException(ex.getMessage());
        }
    }
}
