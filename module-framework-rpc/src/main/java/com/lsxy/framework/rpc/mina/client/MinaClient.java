package com.lsxy.framework.rpc.mina.client;

import com.lsxy.framework.config.SystemConfig;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.RPCResponse;
import com.lsxy.framework.rpc.api.ServiceConstants;
import com.lsxy.framework.rpc.api.client.AbstractClient;
import com.lsxy.framework.rpc.api.client.ClientBindCallback;
import com.lsxy.framework.rpc.api.client.ClientSessionContext;
import com.lsxy.framework.rpc.api.server.Session;
import com.lsxy.framework.rpc.exceptions.ClientBindException;
import com.lsxy.framework.rpc.help.Log4jFilter;
import com.lsxy.framework.rpc.help.MyLog4jFilter;
import com.lsxy.framework.rpc.help.SSLContextFactory;
import com.lsxy.framework.rpc.mina.codec.RPCCodecFactory;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LogLevel;
import org.apache.mina.filter.ssl.SslFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.security.GeneralSecurityException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * 节点代理主动连接节点管理器
 * @author tantyuo
 *
 */
@Component
public class MinaClient extends AbstractClient {
	private static final Logger logger = LoggerFactory.getLogger(MinaClient.class);

	@Autowired(required = false)
	private ClientBindCallback bindCallback;

    @Autowired
    private MinaClientHandler handler;

    @Autowired
    private ClientSessionContext sessionContext;




    public MinaClient(){
    }

	/**
	 * 连接关闭
	 */
	public void unbind(){
//		connect.dispose();
	}



    /**
     * 绑定端口，开始提供服务
     *
     * @param serverUrl
     * @throws IOException
     * @throws GeneralSecurityException
     */
    public Session doBind(String serverUrl) {
        String host = serverUrl.substring(0, serverUrl.indexOf(":"));

        int port = Integer.parseInt(serverUrl.substring(serverUrl.indexOf(":") + 1));
        String clientId = this.getClientId();
        MinaClientSession session = null;
        IoSession ioSession = null;
        try {
            IoConnector connect = new NioSocketConnector();
            connect.setConnectTimeoutMillis(3000);
            String showLog = SystemConfig.getProperty("global.rpc.mina.log", "false");
            if (showLog.equals("true")) {
                MyLog4jFilter ml = new MyLog4jFilter();
                Log4jFilter lf = new Log4jFilter(logger);
                lf.setSessionIdleLoglevel(LogLevel.DEBUG);
                connect.getFilterChain().addLast("ml", ml);
                connect.getFilterChain().addLast("logger", lf);
            }

            if (SystemConfig.getProperty("CONFIG_SERVER_SSL", "false").equals("true")) {
                SslFilter connectorTLSFilter = null;

                connectorTLSFilter = new SslFilter(SSLContextFactory.getInstance(false));

                /** 设置为客户端模式 **/
                connectorTLSFilter.setUseClientMode(true);

                /** 设置加密过滤器 **/
                connect.getFilterChain().addLast("SSL", connectorTLSFilter);
                logger.info("启用SSL安全连接");
            }

            connect.getFilterChain().addLast("codec", new ProtocolCodecFilter(new RPCCodecFactory()));
            connect.setHandler(handler);

            logger.info("正在尝试连接节点管理器" + host + ":" + port + "....");
            SocketAddress sa = new InetSocketAddress(host, port);
            ConnectFuture future = connect.connect(sa).awaitUninterruptibly();
            ioSession = future.getSession();
            RPCRequest request = new RPCRequest();
            request.setName(ServiceConstants.CH_MN_CONNECT);
            request.setParam("clientId=" + clientId);
            RPCResponse response = getRpcCaller().invokeWithReturn(ioSession, request);
            if (response.isOk()) {
                if (bindCallback != null) {
                    bindCallback.doCallback(ioSession);
                }
                //连接成功构建正式的session对象,将serverUrl作为Sessionid
                ioSession.setAttribute("sessionid",serverUrl);
                session = new MinaClientSession(serverUrl,ioSession,handler);
                logger.info("连接节点管理器【{}:{}】成功,sessionid 是  {}",host,port,session.getId());
            } else {
                String msg = new String(response.getBody(), "utf-8");
                logger.info(msg);
                ioSession.close(true);
            }

        } catch (Exception ex) {
            if (ioSession != null)
                ioSession.close(true);
            logger.info("连接" + host + ":" + port + "失败，请确认节点代理服务是否开启，10秒后再次尝试。。。。");
        }

        //连接成功后,将session丢到环境里面去
        if(session != null) {
            if(logger.isDebugEnabled()){
                logger.debug("bind success and got session id is :{}" , session.getId() );
            }

            sessionContext.putSession(session);
        }
        return session;
    }



}
