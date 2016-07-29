package com.hesyun.app.channel.utils.rpc.core;

import java.util.HashMap;
import java.util.Map;

import org.apache.mina.core.service.IoHandlerAdapter;


/**
 * 节点代理客户端消息处理类
 * @author Administrator
 *
 */
public abstract class AbstractMinaHandler extends IoHandlerAdapter {
	protected Map<String,RequestListener> requestListeners = new HashMap<String,RequestListener>();
	
	
	/**
	 * 注册监听器
	 * @param listener
	 */
	public void addRequestListener(RequestListener listener){
		if(requestListeners.get(listener.getSessionId())==null)
			requestListeners.put(listener.getSessionId(),listener);
	}
	
	/**
	 * 移除监听器
	 * @param listener
	 */
	public void removeRequestListener(RequestListener listener){
		requestListeners.remove(listener.getSessionId());
	}
	
}
