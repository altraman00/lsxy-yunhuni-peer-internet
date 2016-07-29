package com.hesyun.app.channel.utils.rpc.server;

import com.hesyun.app.channel.utils.rpc.codec.RPCCodecFactory;
import com.hesyun.app.channel.utils.rpc.core.AbstractMinaHandler;
import com.hesyun.app.channel.utils.rpc.core.AbstractServiceHandler;
import com.hesyun.app.channel.utils.rpc.core.RPCCaller;
import com.hesyun.app.channel.utils.rpc.help.Log4jFilter;
import com.hesyun.app.channel.utils.rpc.help.SSLContextFactory;
import com.lsxy.framework.config.SystemConfig;
import org.apache.log4j.Logger;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LogLevel;
import org.apache.mina.filter.ssl.SslFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.security.GeneralSecurityException;


public class ChannelServer {
	static Logger logger = Logger.getLogger(ChannelServer.class);   

	private NioSocketAcceptor acceptor;
	
	private AbstractMinaHandler channelServerMinaHandler;

	private IoSessionContext sessionContext;
	
	private RPCCaller rpcCaller;
	
	public ChannelServer(AbstractServiceHandler serviceHandler){
		acceptor = new NioSocketAcceptor();
		acceptor.getSessionConfig().setReadBufferSize(10000);
		acceptor.getSessionConfig().setReaderIdleTime(100);
		acceptor.getSessionConfig().setReceiveBufferSize(1000000);
		acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 1);
		
		sessionContext = new IoSessionContext();
		channelServerMinaHandler = new ChannelServerMinaHandler(this,serviceHandler);
		rpcCaller = new RPCCaller();
	}
	
	
	public RPCCaller getRpcCaller() {
		return rpcCaller;
	}



	public void setRpcCaller(RPCCaller rpcCaller) {
		this.rpcCaller = rpcCaller;
	}


	/**
	 * 绑定端口，开始提供服务
	 * @throws IOException
	 * @throws GeneralSecurityException 
	 */
	public void bind() throws IOException, GeneralSecurityException{
		String port = SystemConfig.getProperty("channel.server.port", "9999");
		int intPort = Integer.parseInt(port);
		acceptor.setDefaultLocalAddress(new InetSocketAddress(intPort));
		
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
		logger.info("渠道管理器在端口"+port+"提供服务");
	}
	
	/**
	 * 反绑定
	 */
	public void unbind(){
		logger.info("渠道管理器服务端口关闭,停止提供服务");
		acceptor.unbind();
	}

	public void setChannelServerHandler(ChannelServerMinaHandler channelServerHandler) {
		this.channelServerMinaHandler = channelServerHandler;
	}

	public IoSessionContext getSessionContext() {
		return sessionContext;
	}

	public void setSessionContext(IoSessionContext sessionContext) {
		this.sessionContext = sessionContext;
	}
	

}
