package com.lsxy.framework.rpc.api.client;

import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.RPCResponse;
import com.lsxy.framework.rpc.api.server.Session;


/**
 * 处理请求操作
 * @author tandy
 *
 */
public abstract  class AbstractClientHandler {

	/**
	 *处理请求
	 * @param request
	 * @param session
	 * @return
	 * @throws Exception
	 */
	public abstract RPCResponse handleService(RPCRequest request, Session session);

	
	
	
}
