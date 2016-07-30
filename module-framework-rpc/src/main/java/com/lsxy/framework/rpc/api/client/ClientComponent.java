package com.lsxy.framework.rpc.api.client;

import com.lsxy.framework.rpc.api.server.AbstractServiceHandler;
import com.lsxy.framework.rpc.api.RPCCaller;
import com.lsxy.framework.config.SystemConfig;
import com.lsxy.framework.core.utils.StringUtil;
import com.lsxy.framework.rpc.mina.client.MinaClient;
import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;


/**
 * 渠道客户端监控线程
 * @author tandy
 */
@Component
public class ClientComponent extends Thread {
//	@Autowired
//	private RPCCaller rpcCaller;
//
//	private List<ClientConfig> configs = new ArrayList<>();
//	private List<Client> clients = new ArrayList<>();
//
//
//
//	/**
//	 * 尝试下一个主机
//	 */
//	public void nextHost() {
//		if (StringUtil.isNotEmpty(url) && url.startsWith("cluster(")) {
//			try {
//				String clusters = url.substring(8, url.length() - 1);
//				String hosts[] = clusters.split(",");
//				clusterIdx++;
//				if (clusterIdx >= hosts.length) {
//					clusterIdx = 0;
//				}
//
//				this.host = hosts[clusterIdx].split(":")[0];
//				this.port = Integer.parseInt(hosts[clusterIdx].split(":")[1]);
//			} catch (Exception ex) {
//			}
//		}
//	}
//
//	/**
//	 *
//	 */
//	public ClientComponent() {
//
//
//	}
//
//
//	public String getHost() {
//		return host;
//	}
//
//	public void setHost(String host) {
//		this.host = host;
//	}
//
//	public int getPort() {
//		return port;
//	}
//
//	public void setPort(int port) {
//		this.port = port;
//	}
//
//	@Override
//	public void run() {
//		while (true) {
//			try {
//				//如果没有连接，10S重新连接一次
//				Thread.sleep(10000);
//				nextHost();
//				boolean isActive = client.getConnect().isActive();
//				if (!isActive && !destoried) {
//					client.bind(host,port,clientId,onBind());
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//	}
//
//	protected ClientBindCallback onBind(){
//		return null;
//	}
//
//	@PostConstruct
//	public void startNodeAgent() throws Exception {
//		client = new MinaClient(rpcCaller, injectClientHandler());
//		//启动客户端连接
//		client.bind(host,port,clientId,onBind());
//		//启动监听程序，随时监听链接是否断开，如果端口，重连接
//		this.start();
//	}
//
//
//	@PreDestroy
//	public void destory(){
//		this.destoried = true;
//		client.unbind();
//	}
//
//	/**
//	 * 注入handler
//	 * @return
//	 */
//	protected  AbstractServiceHandler injectClientHandler(){}
//
//	public RPCCaller getRpcCaller() {
//		return rpcCaller;
//	}
//
//	public void setRpcCaller(RPCCaller rpcCaller) {
//		this.rpcCaller = rpcCaller;
//	}
//

}
