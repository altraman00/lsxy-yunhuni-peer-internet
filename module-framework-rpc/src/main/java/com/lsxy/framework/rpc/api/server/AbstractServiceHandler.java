package com.lsxy.framework.rpc.api.server;

import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.RPCResponse;
import org.apache.mina.core.session.IoSession;

import com.lsxy.framework.rpc.mina.server.MinaRemoteServer;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * 处理请求操作
 * @author tandy
 *
 */
public abstract  class AbstractServiceHandler {

	@Autowired
	private MinaRemoteServer channelServer;
	/**
	 *处理请求
	 * @param request
	 * @param session
	 * @return
	 * @throws Exception
	 */
	public abstract RPCResponse handleService(RPCRequest request, IoSession session);
	public MinaRemoteServer getChannelServer() {
		return channelServer;
	}
	public void setChannelServer(MinaRemoteServer channelServer) {
		this.channelServer = channelServer;
	}

	
	
	
}
