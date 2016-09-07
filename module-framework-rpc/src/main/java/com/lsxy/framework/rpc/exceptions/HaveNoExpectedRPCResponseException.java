package com.lsxy.framework.rpc.exceptions;

import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.RPCResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by tandy on 16/8/11.
 */
public class HaveNoExpectedRPCResponseException extends Exception {
    private static final Logger logger = LoggerFactory.getLogger(HaveNoExpectedRPCResponseException.class);
    public HaveNoExpectedRPCResponseException(RPCRequest request, RPCResponse response) {
        logger.error("没有得到期待的请求返回值:[{}] vs [{}]",response,request);
    }
}
