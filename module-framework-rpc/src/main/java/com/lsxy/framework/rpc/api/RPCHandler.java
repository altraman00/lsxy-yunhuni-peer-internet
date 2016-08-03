package com.lsxy.framework.rpc.api;

import com.lsxy.framework.rpc.api.server.Session;
import org.apache.mina.core.service.IoHandlerAdapter;

import java.util.HashMap;
import java.util.Map;


/**
 * 节点代理客户端消息处理类
 * @author Administrator
 *
 */
public interface RPCHandler {

	/**
	 * 注册监听器
	 * @param listener
	 */
	public void addRequestListener(RequestListener listener);

	/**
	 * 移除监听器
	 * @param listener
	 */
	public void removeRequestListener(RequestListener listener);


	/**
	 * 根据环境获取对应的session对象
	 * @param ctxObject
	 */
	Session getSessionInTheContextObject(Object ctxObject);
}
