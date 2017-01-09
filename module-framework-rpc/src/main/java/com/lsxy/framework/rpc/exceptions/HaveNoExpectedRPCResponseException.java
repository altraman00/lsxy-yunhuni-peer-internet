package com.lsxy.framework.rpc.exceptions;

import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.RPCResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by tandy on 16/8/11.
 */
public class HaveNoExpectedRPCResponseException extends Exception {
    private RPCRequest request;    //发出的请求对象
    private RPCResponse response;  //当前返回的响应对象
    private static final Logger logger = LoggerFactory.getLogger(HaveNoExpectedRPCResponseException.class);
    public HaveNoExpectedRPCResponseException(RPCRequest request, RPCResponse response) {
        super("没有得到期待的请求返回值[" + request + "] vs [" + response + "]");
        this.request = request;
        this.response = response;
    }
}
