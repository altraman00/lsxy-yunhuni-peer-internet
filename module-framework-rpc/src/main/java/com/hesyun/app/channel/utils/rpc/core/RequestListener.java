package com.hesyun.app.channel.utils.rpc.core;


/**
 * 请求监听
 * @author sds
 */
public abstract class RequestListener {
	private RPCRequest request;
//	public RequestListener(RPCRequest request){
//		this.request = request;
//	}
	/**
	 * 获取到响应
	 * @param request
	 */
	public abstract void recivedResponse(RPCResponse response) throws Exception;
	
	public String getSessionId(){
		return request.getSessionid();
	}
	
	public void setRequest(RPCRequest request){
		this.request = request;
	}
}
