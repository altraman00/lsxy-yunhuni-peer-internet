package com.lsxy.framework.rpc.mina.server;

import com.lsxy.framework.rpc.api.*;
import com.lsxy.framework.rpc.api.server.SessionContext;
import com.lsxy.framework.rpc.api.server.RemoteServer;
import com.lsxy.framework.rpc.mina.codec.RPCCodecFactory;
import com.lsxy.framework.rpc.exceptions.RemoteServerStartException;
import com.lsxy.framework.rpc.help.Log4jFilter;
import com.lsxy.framework.rpc.help.SSLContextFactory;
import com.lsxy.framework.config.SystemConfig;

import com.lsxy.framework.rpc.mina.AbstractMinaHandler;
import com.lsxy.framework.rpc.mina.MinaCondition;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LogLevel;
import org.apache.mina.filter.ssl.SslFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.security.GeneralSecurityException;



@Component
@Conditional(MinaCondition.class)
public class MinaRemoteServer implements RemoteServer{
private static final Logger logger = LoggerFactory.getLogger(MinaRemoteServer.class);
	private NioSocketAcceptor acceptor;

    @Autowired
	private AbstractMinaHandler channelServerMinaHandler;

	private SessionContext sessionContext;
	
	private RPCCaller rpcCaller;

	//服务端口号
	private Integer serverPort;

	public MinaRemoteServer(){
		acceptor = new NioSocketAcceptor();
		acceptor.getSessionConfig().setReadBufferSize(10000);
		acceptor.getSessionConfig().setReaderIdleTime(100);
		acceptor.getSessionConfig().setReceiveBufferSize(1000000);
		acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 1);
		
		sessionContext = new SessionContext();
		rpcCaller = new RPCCaller();
	}
	
	
	public RPCCaller getRpcCaller() {
		return rpcCaller;
	}


	/**
	 * 绑定端口，开始提供服务
	 * @throws IOException
	 * @throws GeneralSecurityException 
	 */
	public void bind() throws IOException, GeneralSecurityException{
		acceptor.setDefaultLocalAddress(new InetSocketAddress(serverPort));
		
		acceptor.setHandler(channelServerMinaHandler);
		String showLog = SystemConfig.getProperty("channel.server.minalog","false");
		if(showLog.equals("true")){
			Log4jFilter lf = new Log4jFilter(logger); 
			lf.setSessionIdleLoglevel(LogLevel.ERROR);
			acceptor.getFilterChain().addLast("logger",lf);
		}
		if(SystemConfig.getProperty("channel.server.ssl","false").equals("true")){
			SslFilter sslFilter = new SslFilter(SSLContextFactory.getInstance(true));
			acceptor.getFilterChain().addLast("sslFilter", sslFilter);  
			logger.info("启用采用SSL安全连接");
		}
		acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new RPCCodecFactory()));
		acceptor.bind();
		logger.info("渠道管理器在端口"+serverPort+"提供服务");
	}
	
	/**
	 * 反绑定
	 */
	public void unbind(){
		logger.info("渠道管理器服务端口关闭,停止提供服务");
		acceptor.unbind();
	}

	public void setChannelServerHandler(MinaServerHandler channelServerHandler) {
		this.channelServerMinaHandler = channelServerHandler;
	}

	public SessionContext getSessionContext() {
		return sessionContext;
	}

	public void setSessionContext(SessionContext sessionContext) {
		this.sessionContext = sessionContext;
	}


//	public static void main(String[] args) throws IOException, GeneralSecurityException {
//		MinaRemoteServer cs = new MinaRemoteServer(new AbstractServiceHandler() {
//			@Override
//			public RPCResponse handleService(RPCRequest request, IoSession session) {
//				return null;
//			}
//		});
//		cs.bind();
//	}

    @Override
    public void startServer() throws RemoteServerStartException {
        try {
            this.bind();
        } catch (IOException | GeneralSecurityException e) {
            e.printStackTrace();
            throw new RemoteServerStartException(e);
        }
    }

	@Override
	public void setServerPort(Integer port) {
		this.serverPort = port;
	}
}
