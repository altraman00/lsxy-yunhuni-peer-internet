package com.lsxy.framework.rpc.mina.client;

import com.lsxy.framework.config.SystemConfig;
import com.lsxy.framework.rpc.api.*;
import com.lsxy.framework.rpc.api.client.ClientBindCallback;
import com.lsxy.framework.rpc.api.server.AbstractServiceHandler;
import com.lsxy.framework.rpc.api.client.ClientHandler;
import com.lsxy.framework.rpc.codec.RPCCodecFactory;
import com.lsxy.framework.rpc.help.Log4jFilter;
import com.lsxy.framework.rpc.help.MyLog4jFilter;
import com.lsxy.framework.rpc.help.SSLContextFactory;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LogLevel;
import org.apache.mina.filter.ssl.SslFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.security.GeneralSecurityException;


/**
 * 节点代理主动连接节点管理器
 * @author tantyuo
 *
 */
public class MinaClient {
	private static final Logger logger = LoggerFactory.getLogger(MinaClient.class);

	// mina acceptor
	private IoConnector connect;
	
	private RPCCaller rpcCaller;
	
	private AbstractServiceHandler handler;
	
	private IoSession session;
	
	
	public IoConnector getConnect() {
		return connect;
	}

	public void setConnect(IoConnector connect) {
		this.connect = connect;
	}

	public MinaClient(RPCCaller rpcCaller , AbstractServiceHandler handler){
		this.rpcCaller = rpcCaller;
		this.handler = handler;
	}
	
	
	/**
	 * 绑定端口，开始提供服务
	 * @throws IOException
	 * @throws GeneralSecurityException 
	 */
	public void bind(String host,int port,String clientId,ClientBindCallback callback) throws IOException, GeneralSecurityException{
		connect = new NioSocketConnector();
		connect.setConnectTimeoutMillis(3000);
		String showLog = SystemConfig.getProperty("CONFIG_SERVER_MINALOG","false");
		if(showLog.equals("true")){
			MyLog4jFilter ml = new MyLog4jFilter();
			Log4jFilter lf = new Log4jFilter(logger);
			lf.setSessionIdleLoglevel(LogLevel.DEBUG);
			connect.getFilterChain().addLast("ml", ml);
			connect.getFilterChain().addLast("logger",lf);
		}
		if(SystemConfig.getProperty("CONFIG_SERVER_SSL","false").equals("true")){
			SslFilter connectorTLSFilter = new SslFilter(SSLContextFactory.getInstance(false));  
			/** 设置为客户端模式 **/  
			connectorTLSFilter.setUseClientMode(true);  
			
			/** 设置加密过滤器 **/  
			connect.getFilterChain().addLast("SSL", connectorTLSFilter);
			logger.info("启用SSL安全连接");
		}
		connect.getFilterChain().addLast("codec", new ProtocolCodecFilter(new RPCCodecFactory()));
		connect.setHandler(new ClientHandler(rpcCaller,handler));
		
		logger.info("正在尝试连接节点管理器"+host+":"+port+"....");
		SocketAddress sa = new InetSocketAddress(host,port);
		ConnectFuture future = connect.connect(sa);
		session = null;
		future.awaitUninterruptibly(); 
		try {
			session = future.getSession();
			RPCRequest request = new RPCRequest();
			request.setName(ServiceConstants.CH_MN_CONNECT);
			request.setParam("channelClientId="+clientId);
			RPCResponse response = rpcCaller.invokeWithReturn(session, request);
			if(response.isOk()){
				logger.info("连接节点管理器【"+host+":"+port+"】成功。。");
				if(callback != null)
					callback.doCallback(session);
			}else{
				String msg = new String(response.getBody(),"utf-8");
				logger.info(msg);
				session.close(true);
			}
		} catch (Exception ex) {
			if(session!=null)
				session.close(true);
			logger.info("连接"+host+":"+port+"失败，请确认节点代理服务是否开启，10秒后再次尝试。。。。");
		}
	}
	/**
	 * 连接关闭
	 */
	public void unbind(){
		connect.dispose();
	}

	public IoSession getSession() {
		return session;
	}
	
	
}
