package com.lsxy.framework.rpc.mina.client;

import com.lsxy.framework.rpc.api.*;
import com.lsxy.framework.rpc.api.client.AbstractClientRPCHandler;
import com.lsxy.framework.rpc.api.client.AbstractClientServiceHandler;
import com.lsxy.framework.rpc.api.client.ClientSessionContext;
import com.lsxy.framework.rpc.api.session.Session;
import com.lsxy.framework.rpc.mina.MinaCondition;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import java.io.IOException;


/**
 * 节点代理客户端消息处理类
 * @author Administrator
 *
 */
@Component
@Conditional(MinaCondition.class)
public class MinaClientHandler extends AbstractClientRPCHandler {


	private static final Logger logger = LoggerFactory.getLogger(MinaClientHandler.class);

	//请求对象
	@Autowired
	private RPCCaller rpcCaller;

	//命令处理句柄
	@Autowired(required = false)
	private AbstractClientServiceHandler serviceHandler;

	@Autowired
	private ClientSessionContext sessionContext;


	private IoHandlerAdapter ioHandler = new IoHandlerAdapter(){

		@Override
		public void messageReceived(IoSession iosession, Object message)
				throws Exception {
			Session session = getSessionInTheContextObject(iosession);
			if(message instanceof RPCRequest){
				RPCRequest request = (RPCRequest) message;
				logger.debug("<<[CH]"+request);
				if(serviceHandler != null){
					RPCResponse response = serviceHandler.handleService(request,session);
					if(response!=null){
						logger.debug(">>[NM]"+response);
						session.write(response);
					}
				}
			}
			if(message instanceof RPCResponse){
				RPCResponse response = (RPCResponse) message;
				logger.debug(">>[NM]"+response);
				rpcCaller.receivedResponse(response);
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
	};

	public IoHandlerAdapter getIoHandler() {
		return ioHandler;
	}

	@Override
	public Session getSessionInTheContextObject(Object ctxObject) {
		IoSession ioSession = (IoSession) ctxObject;
		return sessionContext.getSession((String) ioSession.getAttribute("sessionid"));
	}
}
