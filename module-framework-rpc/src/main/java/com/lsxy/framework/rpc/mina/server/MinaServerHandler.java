package com.lsxy.framework.rpc.mina.server;

import com.lsxy.framework.core.utils.StringUtil;
import com.lsxy.framework.rpc.api.*;
import com.lsxy.framework.rpc.api.server.AbstractServiceHandler;
import com.lsxy.framework.rpc.api.server.Session;
import com.lsxy.framework.rpc.mina.AbstractMinaHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * 服务器端处理类
 * @author tantyue
 *
 */
@Component
public class MinaServerHandler extends AbstractMinaHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(MinaServerHandler.class);

	@Autowired
	private AbstractServiceHandler serviceHandler;

	@Autowired
	private MinaRemoteServer remoteServer;
	
	public MinaServerHandler() {
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		MinaServerSessionContext sessionContext = this.remoteServer.getSessionContext();
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
	public void messageReceived(IoSession iosession, Object message)
			throws Exception {
		
		if(message instanceof RPCRequest){
			RPCRequest request = (RPCRequest) message;
            RPCResponse response = null;
			if(request.getName().equals(ServiceConstants.CH_MN_CONNECT)){
			    response = process_CH_MN_CONNECT(iosession,request);
            }else{
                Session session = remoteServer.getSessionContext().getSession(iosession.hashCode());
                response = serviceHandler.handleService(request,session);
            }

			logger.debug("<<"+request);
			if(response!=null){
				logger.debug(">>"+response);
				iosession.write(response);
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
				this.remoteServer.getRpcCaller().putResponse(response);
			}
			
		}
	}

    /**
     * 处理客户端连接命令
     * @param request
     * @return
     */
    private RPCResponse process_CH_MN_CONNECT(IoSession iosession ,RPCRequest request) {
        RPCResponse response = RPCResponse.buildResponse(request);

        InetSocketAddress addr  = (InetSocketAddress) iosession.getRemoteAddress();
        if(logger.isDebugEnabled()){
            logger.debug("有连接接入 {}:{}" , addr.getAddress().getHostAddress(),addr.getPort());
        }

        //TODO 需要验证IP白名单,允许指定IP的客户端连接
        response.setMessage(RPCResponse.STATE_OK);
		String clientId = request.getParameter("clientId");
		iosession.setAttribute("clientId",clientId);
        Session session = new MinaServerSession(iosession,this);
        this.remoteServer.getSessionContext().putSession(session);
        return response;
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
		MinaServerSessionContext sessionContext = this.remoteServer.getSessionContext();
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

