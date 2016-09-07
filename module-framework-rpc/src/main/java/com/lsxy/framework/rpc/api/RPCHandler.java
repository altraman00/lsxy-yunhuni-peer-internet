package com.lsxy.framework.rpc.api;

import com.lsxy.framework.rpc.api.session.Session;


/**
 * 节点代理客户端消息处理类
 * @author Administrator
 *
 */
public abstract class RPCHandler {



	/**
	 * 根据环境获取对应的session对象
	 * @param ctxObject
	 */
	public abstract Session getSessionInTheContextObject(Object ctxObject);


}
