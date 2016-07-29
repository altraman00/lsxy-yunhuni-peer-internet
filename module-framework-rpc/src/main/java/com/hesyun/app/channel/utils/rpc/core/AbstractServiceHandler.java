package com.hesyun.app.channel.utils.rpc.core;

import org.apache.mina.core.session.IoSession;

import com.hesyun.app.channel.utils.rpc.server.ChannelServer;


/**
 * 处理请求操作
 * @author tandy
 *
 */
public abstract  class AbstractServiceHandler {
	
	private ChannelServer channelServer;
	/**
	 *处理请求
	 * @param request
	 * @param session
	 * @return
	 * @throws Exception
	 */
	public abstract RPCResponse handleService(RPCRequest request,IoSession session);
	public ChannelServer getChannelServer() {
		return channelServer;
	}
	public void setChannelServer(ChannelServer channelServer) {
		this.channelServer = channelServer;
	}

	
	
	
}
