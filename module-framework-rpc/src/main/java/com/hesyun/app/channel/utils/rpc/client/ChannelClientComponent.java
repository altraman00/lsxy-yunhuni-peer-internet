package com.hesyun.app.channel.utils.rpc.client;

import com.hesyun.app.channel.utils.rpc.core.AbstractServiceHandler;
import com.hesyun.app.channel.utils.rpc.core.RPCCaller;
import com.lsxy.framework.config.SystemConfig;
import com.lsxy.framework.core.utils.StringUtil;
import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;


/**
 * 渠道客户端监控线程
 * @author tandy
 */
public abstract class ChannelClientComponent extends Thread {
	@Autowired
	private RPCCaller rpcCaller;
	
	//渠道客户端
	private ChannelClient client;
	
	private String host;
	private int port;
	private String clientId;
	private String url;
	
	private int clusterIdx=-1;
	private boolean destoried = false; 

	public IoSession getIoSession(){
		return client.getSession();
	}
	
	/**
	 * 尝试下一个主机
	 */
	public void nextHost() {
		if (StringUtil.isNotEmpty(url) && url.startsWith("cluster(")) {
			try {
				String clusters = url.substring(8, url.length() - 1);
				String hosts[] = clusters.split(",");
				clusterIdx++;
				if (clusterIdx >= hosts.length) {
					clusterIdx = 0;
				}

				this.host = hosts[clusterIdx].split(":")[0];
				this.port = Integer.parseInt(hosts[clusterIdx].split(":")[1]);
			} catch (Exception ex) {
			}
		}
	}

	/**
	 * 
	 */
	public ChannelClientComponent() {
		host = SystemConfig.getProperty("channel.server.host");
		port=Integer.parseInt(SystemConfig.getProperty("channel.server.port"));
		url = SystemConfig.getProperty("channel.server.url");
		clientId = SystemConfig.getProperty("channel.client.id");
		nextHost();
	}

	
	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	@Override
	public void run() {
		while (true) {
			try {
				//如果没有连接，10S重新连接一次
				Thread.sleep(10000);
				nextHost();
				boolean isActive = client.getConnect().isActive();
				if (!isActive && !destoried) {
					client.bind(host,port,clientId,onBind());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	protected abstract ChannelClientBindCallback onBind();

	@PostConstruct
	public void startNodeAgent() throws Exception {
		client = new ChannelClient(rpcCaller, injectClientHandler());
		//启动客户端连接
		client.bind(host,port,clientId,onBind());
		//启动监听程序，随时监听链接是否断开，如果端口，重连接
		this.start();
	}
	
	
	@PreDestroy
	public void destory(){
		this.destoried = true;
		client.unbind();
	}

	/**
	 * 注入handler
	 * @return
	 */
	protected abstract AbstractServiceHandler injectClientHandler();

	public RPCCaller getRpcCaller() {
		return rpcCaller;
	}

	public void setRpcCaller(RPCCaller rpcCaller) {
		this.rpcCaller = rpcCaller;
	}
	

}
