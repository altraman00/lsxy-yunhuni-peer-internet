package com.hesyun.app.channel.utils.rpc.server;

import com.hesyun.app.channel.utils.rpc.core.*;
import com.lsxy.framework.core.utils.StringUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * 服务器端处理类
 * @author tantyue
 *
 */
public class ChannelServerMinaHandler extends AbstractMinaHandler {
	
	private AbstractServiceHandler serviceHandler;
	
	private ChannelServer channelServer;
	
	private Log logger = LogFactory.getLog(ChannelServerMinaHandler.class);
	public ChannelServerMinaHandler(ChannelServer channelServer , AbstractServiceHandler serviceHandler) {
		serviceHandler.setChannelServer(channelServer);
		this.serviceHandler = serviceHandler;
		this.channelServer = channelServer;
	}

	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		IoSessionContext sessionContext = this.channelServer.getSessionContext();
		logger.debug("exceptionCaught:"+cause.getMessage());
		if(cause instanceof IOException){
			InetSocketAddress addr = (InetSocketAddress) session.getRemoteAddress();
			String ip = addr.getAddress().getHostAddress();
			String nodeAgentId = (String) session.getAttribute("channelClientId");
			if(StringUtil.isNotEmpty(nodeAgentId)){
				sessionContext.removeSession(nodeAgentId);
				logger.debug("节点代理【"+nodeAgentId+"】【"+ip+"】断开连接");
			}
		}
	}
	
	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		
		if(message instanceof RPCRequest){
			RPCRequest request = (RPCRequest) message;
			
			logger.debug("<<"+request);
			RPCResponse response = serviceHandler.handleService(request,session);
			if(response!=null){
				logger.debug(">>"+response);
				session.write(response); 
			}
		}
		if(message instanceof RPCResponse){
			RPCResponse response = (RPCResponse) message;
			logger.debug(">>[NM]"+response);
			RequestListener rl = this.requestListeners.get(response.getSessionid());
			if(rl != null){
				rl.recivedResponse(response);
				this.removeRequestListener(rl);
			}else{
				logger.debug("收到响应【"+response.getSessionid()+"】");
				this.channelServer.getRpcCaller().putResponse(response);
			}
			
		}
	}
	
	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		if(message instanceof RPCRequest){
			
		}
		logger.debug("发送消息："+message);
	}
	
	@Override
	public void sessionClosed(IoSession session) throws Exception {
		logger.debug("SESSION关闭:"+session);
		String nodeAgentId = (String) session.getAttribute("channelClientId");
		IoSessionContext sessionContext = this.channelServer.getSessionContext();
		if(StringUtil.isNotEmpty(nodeAgentId)){
			sessionContext.removeSession(nodeAgentId);
			InetSocketAddress addr = (InetSocketAddress) session.getRemoteAddress();
			String ip = addr.getAddress().getHostAddress();
			logger.debug("节点代理【"+nodeAgentId+"】【"+ip+"】断开连接");
		}
		
		
	}
	@Override
	public void sessionCreated(IoSession session) throws Exception {
		logger.debug("SESSION创建:"+session);
	}
	
	@Override
	public void sessionIdle(IoSession session, IdleStatus status)
			throws Exception {
//		System.out.println("会话空闲");
	}
	@Override
	public void sessionOpened(IoSession session) throws Exception {
		InetSocketAddress addr = (InetSocketAddress) session.getRemoteAddress();
		String ip = addr.getAddress().getHostAddress();
		logger.info("渠道节点【"+ip+"】正在尝试连接控制中心");
	}

	
}

