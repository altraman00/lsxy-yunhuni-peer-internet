package com.hesyun.app.channel.utils.rpc.client;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.mina.core.session.IoSession;

import com.hesyun.app.channel.utils.rpc.core.AbstractMinaHandler;
import com.hesyun.app.channel.utils.rpc.core.AbstractServiceHandler;
import com.hesyun.app.channel.utils.rpc.core.RPCCaller;
import com.hesyun.app.channel.utils.rpc.core.RPCRequest;
import com.hesyun.app.channel.utils.rpc.core.RPCResponse;
import com.hesyun.app.channel.utils.rpc.core.RequestListener;


/**
 * 节点代理客户端消息处理类
 * @author Administrator
 *
 */
public class ChannelClientMinaHandler extends AbstractMinaHandler {
	private static Logger logger = Logger.getLogger(ChannelClientMinaHandler.class);

	//请求对象
	private RPCCaller rpcCaller;
	//命令处理句柄
	private AbstractServiceHandler nodeAgentClientServiceHandler;
	
	public ChannelClientMinaHandler(RPCCaller rpcCaller,AbstractServiceHandler nodeAgentClientServiceHandler) {
		this.rpcCaller = rpcCaller;
		this.nodeAgentClientServiceHandler = nodeAgentClientServiceHandler;
	}

	public RPCCaller getRpcCaller() {
		return rpcCaller;
	}

	public void setRpcCaller(RPCCaller rpcCaller) {
		this.rpcCaller = rpcCaller;
	}
	
	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		if(message instanceof RPCRequest){
			RPCRequest request = (RPCRequest) message;
			logger.debug("<<[CH]"+request);
			RPCResponse response = nodeAgentClientServiceHandler.handleService(request,session);
			if(response!=null){
				logger.debug(">>[NM]"+response);
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
				rpcCaller.putResponse(response);
			}
			
		}
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		// TODO Auto-generated method stub
		super.messageSent(session, message);
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		super.sessionClosed(session);
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		super.sessionCreated(session);
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		super.sessionOpened(session);
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		if(cause instanceof IOException){
			logger.info("控制中心服务器连接断开，请检查网络连接是否正常，或者控制中心已停止服务，请联系管理员");
			//需要停止服务端口
//			NodeAgentServer server = (NodeAgentServer) SpringContextUtil.getBean("nodeAgentServer");
//			server.unbind();
//			NodeAgentContext.getInstance().resetNodeAgentContext();
		}else{
			logger.info("=========================NM服务出错啦======================");
			cause.printStackTrace();
			
		}
	}
	
}
