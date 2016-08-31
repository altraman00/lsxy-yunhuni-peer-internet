package com.lsxy.framework.rpc.mina.client;

import com.lsxy.framework.config.SystemConfig;
import com.lsxy.framework.rpc.api.client.AbstractClient;
import com.lsxy.framework.rpc.api.client.ClientSessionContext;
import com.lsxy.framework.rpc.api.session.Session;
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
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.security.GeneralSecurityException;


/**
 * 节点代理主动连接节点管理器
 * @author tantyuo
 *
 */
@Component
//@Conditional(MinaCondition.class)
@ConditionalOnProperty(value = "global.rpc.provider", havingValue = "mina", matchIfMissing = false)
public class MinaClient extends AbstractClient {
	private static final Logger logger = LoggerFactory.getLogger(MinaClient.class);

    @Autowired
    private MinaClientHandler handler;

    @Autowired
    private ClientSessionContext sessionContext;

    private String serverUrl;

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
    @Override
    public Session doBind(String serverUrl) throws ClientBindException {

        this.serverUrl = serverUrl;

        String host = getHost();
        int port = this.getPort();

        MinaClientSession session = null;
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
            connect.setHandler(handler.getIoHandler());

            logger.info("正在尝试连接节点管理器" + host + ":" + port + "....");
            SocketAddress sa = new InetSocketAddress(host, port);
            ConnectFuture future = connect.connect(sa).awaitUninterruptibly();
            IoSession ioSession = future.getSession();

            session = new MinaClientSession(ioSession,handler,serverUrl);

            //发送连接命令
            doConnect(session);

        } catch (Exception ex) {
            ex.printStackTrace();
            if (session != null)
                session.close(true);
            logger.info("连接" + host + ":" + port + "失败，请确认节点代理服务是否开启，10秒后再次尝试。。。。");
            throw new ClientBindException(ex);
        }

        return session;
    }

    private int getPort() {
        assert this.serverUrl!=null;
        return Integer.parseInt(serverUrl.substring(serverUrl.indexOf(":") + 1));
    }

    private String getHost() {
        assert this.serverUrl!=null;
        return serverUrl.substring(0, serverUrl.indexOf(":"));
    }




}
