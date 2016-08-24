package com.lsxy.framework.rpc.exceptions;

import com.lsxy.framework.rpc.api.RPCRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 请求超时异常
 * @author Administrator
 *
 */
public class RequestTimeOutException extends Exception {

	private static final Logger logger = LoggerFactory.getLogger(RequestTimeOutException.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    public RequestTimeOutException(RPCRequest request) {
    	logger.error("RPC请求超时:"+request);
    }
}
