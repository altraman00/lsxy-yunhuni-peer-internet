package com.lsxy.framework.rpc.mina.client;

import java.io.IOException;

import com.lsxy.framework.rpc.api.client.AbstractClientHandler;
import com.lsxy.framework.rpc.api.client.Client;
import com.lsxy.framework.rpc.api.server.Session;
import com.lsxy.framework.rpc.mina.server.MinaServerSessionContext;
import org.apache.mina.core.session.IoSession;

import com.lsxy.framework.rpc.mina.AbstractMinaHandler;
import com.lsxy.framework.rpc.api.RPCCaller;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.RPCResponse;
import com.lsxy.framework.rpc.api.RequestListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * 节点代理客户端消息处理类
 * @author Administrator
 *
 */
@Component
public class MinaClientHandler extends AbstractMinaHandler {

	private static final Logger logger = LoggerFactory.getLogger(MinaClientHandler.class);

	//请求对象
	@Autowired
	private RPCCaller rpcCaller;

	//命令处理句柄
	@Autowired(required = false)
	private AbstractClientHandler nodeAgentClientServiceHandler;

	@Autowired
	private ClientSessionContext sessionContext;

	@Override
	public void messageReceived(IoSession iosession, Object message)
			throws Exception {
		Session session = sessionContext.getSession((String) iosession.getAttribute("sessionid"));
		if(message instanceof RPCRequest){
			RPCRequest request = (RPCRequest) message;
			logger.debug("<<[CH]"+request);
			if(nodeAgentClientServiceHandler != null){
				RPCResponse response = nodeAgentClientServiceHandler.handleService(request,session);
				if(response!=null){
					logger.debug(">>[NM]"+response);
					session.write(response);
				}
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
