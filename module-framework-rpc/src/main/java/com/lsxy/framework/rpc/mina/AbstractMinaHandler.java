package com.lsxy.framework.rpc.mina;

import com.lsxy.framework.rpc.api.RPCHandler;
import com.lsxy.framework.rpc.api.RequestListener;
import org.apache.mina.core.service.IoHandlerAdapter;

import java.util.HashMap;
import java.util.Map;


/**
 * 节点代理客户端消息处理类
 * @author Administrator
 *
 */
public abstract class AbstractMinaHandler extends IoHandlerAdapter implements RPCHandler{

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
