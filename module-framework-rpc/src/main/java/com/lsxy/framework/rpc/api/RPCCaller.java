package com.lsxy.framework.rpc.api;

import com.lsxy.framework.config.SystemConfig;
import com.lsxy.framework.rpc.api.session.Session;
import com.lsxy.framework.rpc.api.session.SessionContext;
import com.lsxy.framework.rpc.exceptions.*;
import com.lsxy.framework.rpc.queue.FixQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 远程调用工具方法
 * 
 * @author win7desk
 * 
 */
@Component("rpcCaller")
public class RPCCaller {
	private static final Logger logger = LoggerFactory.getLogger(RPCCaller.class);
	
	private Map<String,RPCRequest> requestMap = new HashMap<String,RPCRequest>();
	private Map<String,RPCResponse> responseMap = new HashMap<String, RPCResponse>();

	//注册的监听
	protected Map<String,RequestListener> requestListeners = new HashMap<String,RequestListener>();

	@Autowired
	private FixQueue fixQueue;

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


	public void putResponse(RPCResponse response){
		responseMap.put(response.getSessionid(), response);
	}

	/**
	 * 根据sessionid找到对应的请求对象
	 * @param sessionid
	 * @return
     */
	public RPCRequest getRequest(String sessionid) {
		return this.requestMap.get(sessionid);
	}

	/**
	 * 收到了响应对象
	 * @param response
	 */
	public void receivedResponse(RPCResponse response) {
		if(logger.isDebugEnabled()){
			logger.debug(">>"+response);
		}
		RPCRequest request = this.getRequest(response.getSessionid());
		this.fireRequestListener(response);
		if(request != null){
			this.putResponse(response);
//			if(logger.isDebugEnabled()){
//				if(!request.getName().equals(ServiceConstants.CH_MN_HEARTBEAT_ECHO) || SystemConfig.getProperty("area.agent.log.show.heartbeat","true").equals("true")) {
//					logger.debug("通知请求对象该醒了:{}", request);
//				}
//			}
			synchronized (request){
				request.notify();
			}
		}else{
			logger.error("收到一个匹配不到请求对象的响应对象:{}",response);
		}
	}

	/**
	 * 触发指定返回对应请求的处理监听
	 * @param response
     */
	private void fireRequestListener(RPCResponse response) {
		RequestListener rl = this.requestListeners.get(response.getSessionid());
		if(rl!=null){
			try {
				rl.recivedResponse(response);
				this.removeRequestListener(rl);
			} catch (Exception e) {
				logger.error("RequestListener为空",e);
			}
		}
	}


	/**
	 * 调用远程服务，无返回值,不重试
	 * @param sessionContext 允许外接session为空,将为空判断放入方法体主要是为了统一处理会话丢失导致的消息丢失,将消息丢入修正队列
	 * @throws RequestWriteException
	 */
	public void invoke(SessionContext sessionContext, RPCRequest request) throws RightSessionNotFoundExcepiton, SessionWriteException {
		invoke(sessionContext, request,false);
	}

	/**
	 * 调用远程服务，无返回值
	 * @param sessionContext
	 * @param request
	 * @param retry 是否重试
	 * @throws RightSessionNotFoundExcepiton
	 * @throws SessionWriteException
     */
	public void invoke(SessionContext sessionContext, RPCRequest request,boolean retry) throws RightSessionNotFoundExcepiton, SessionWriteException {
		try{
			Session session = sessionContext.getRightSession(request);
			//如果session为空
			if(session == null) {
				logger.error("RPC连接会话不存在,无法发送请求,请求消息丢入修正队列:{}", request);
				throw new RightSessionNotFoundExcepiton("会话不存在："+request.getSessionid());
			}
			invoke(session,request);
		} catch (SessionWriteException | RightSessionNotFoundExcepiton e) {
			if(retry){
				logger.error("RPC请求发送失败,请求消息丢入修正队列:"+request,e);
				fixQueue.fix(request);
			}else{
				logger.error("RPC请求发送失败");
			}
			throw e;
		}
	}
	/**
	 * 调用远程服务，无返回值
	 * @param session 允许外接session为空,将为空判断放入方法体主要是为了统一处理会话丢失导致的消息丢失,将消息丢入修正队列
	 * @throws RequestWriteException
	 */
	public void invoke(Session session, RPCRequest request) throws RightSessionNotFoundExcepiton, SessionWriteException {
		logger.debug(">>*"+request);
		session.write(request);
	}

	/**
	 * 异步调用，并指定回调函数 ,无超时限制,不期待有返回值,如果有返回值,则执行回调
	 * @param sessionContext
	 * @param request
	 * @param rqListener
	 * @throws RequestWriteException
	 */
	public void invoke(SessionContext sessionContext, RPCRequest request, RequestListener rqListener) throws RightSessionNotFoundExcepiton, SessionWriteException {
		try {
			Session session = sessionContext.getRightSession(request);
			//如果session为空
			if(session == null){
				logger.error("RPC连接会话不存在,无法发送请求,请求消息丢入修正队列:{}",request);
				fixQueue.fix(request);
				return;
			}

			rqListener.setRequest(request);
			this.addRequestListener(rqListener);

			logger.debug(">>*"+request);

			session.write(request);
		} catch (RightSessionNotFoundExcepiton | SessionWriteException e) {
			logger.error("RPC请求发送失败,请求消息丢入修正队列:" + request, e);
			fixQueue.fix(request);
			throw e;
		}
	}
	
	/**
	 * 调用指定的服务，发出请求
	 * 
	 * @param session
	 * @return
	 * @throws InterruptedException
	 * @throws RequestTimeOutException
	 */
	@SuppressWarnings("static-access")
	public RPCResponse invokeWithReturn(Session session, RPCRequest request)
			throws InterruptedException, RequestTimeOutException, HaveNoExpectedRPCResponseException {
//		Session session = sessionContext.getRightSession();
		//如果session为空
		if(session == null){
			logger.error("RPC连接会话不存在,无法发送请求,请求消息丢入修正队列:{}",request);
			fixQueue.fix(request);
			return null;
		}

		requestMap.put(request.getSessionid(), request);
		logger.debug(">>"+request);

		try {
			session.write(request);
		} catch (SessionWriteException e) {
			logger.error("RPC请求发送失败,请求消息丢入修正队列:"+request,e);
			fixQueue.fix(request);
			return null;
		}
		//写入成功,等待返回值
		long startWait = System.currentTimeMillis();
		long timeout = Long.parseLong(SystemConfig.getProperty("global.rpc.request.timeout","10000"));	//10s超时
		synchronized (request){
			request.wait(timeout);
			if(System.currentTimeMillis() - startWait >= timeout){
				throw new RequestTimeOutException(request);
			}
		}
		//返回值取到
		if(logger.isDebugEnabled()){
			if(!request.getName().equals(ServiceConstants.CH_MN_HEARTBEAT_ECHO) || SystemConfig.getProperty("area.agent.log.show.heartbeat","true").equals("true")) {
				logger.debug("请求醒了:{},已经睡了{}ms", request, (System.currentTimeMillis() - startWait));
			}
		}
		RPCResponse response = responseMap.get(request.getSessionid());

		if(response == null || !request.getSessionid().equals(request.getSessionid())){
			throw new HaveNoExpectedRPCResponseException(request,response);
		}

		responseMap.remove(request.getSessionid());
		requestMap.remove(request.getSessionid());
		return response;
	}


}