package com.lsxy.framework.rpc.api;

import com.lsxy.framework.rpc.exceptions.RequestTimeOutException;
import com.lsxy.framework.rpc.exceptions.RequestWriteException;
import com.lsxy.framework.config.SystemConfig;
import com.lsxy.framework.core.utils.UUIDGenerator;
import com.lsxy.framework.rpc.mina.AbstractMinaHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.session.IoSession;
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
	
	
	private Map<String,RPCRequest> requestMap = new HashMap<String,RPCRequest>();
	private Map<String,RPCResponse> responseMap = new HashMap<String, RPCResponse>();

	private Log logger = LogFactory.getLog(RPCCaller.class);

	public void putResponse(RPCResponse response){
		logger.debug("putResponse:收到响应【"+response.getSessionid()+"】");
		responseMap.put(response.getSessionid(), response);
		logger.debug("responseMap size:"+responseMap.size());
		logger.debug("this is :"+this);
	}

	/**
	 * 
	 * @author win7desk
	 * 
	 */
	class CallThread extends Thread {
		private RPCCaller cl;
		private RPCRequest request;
		public CallThread(RPCCaller cl,RPCRequest request){
			this.cl = cl;
			this.request = request;
		}

		@Override
		public void run() {
			long s = System.currentTimeMillis();
			long timeout = Long.parseLong(SystemConfig.getProperty("channel.server.request.timeout","60"));	//10s超时
			long currentTime = 0;
			while (!cl.isHaveResponse(request.getSessionid())) {
				try {
					sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				long e = System.currentTimeMillis();
				currentTime = ((e - s)/1000);
				if(currentTime>timeout){
					logger.debug("REQUEST TIME OUT["+request.getSessionid()+"]");
					break;
				}
					
			}
			synchronized (this) {
				this.notify();
			}
		}
	}

//	/**
//	 * 调用远程服务，无返回值
//	 * @param session
//	 * @param serviceName
//	 * @param param
//	 * @throws RequestWriteException 
//	 */
//	public void invoke(IoSession session,String serviceName) throws RequestWriteException{
//		invoke(session,serviceName,null);
//	}
//	
//	/**
//	 * 调用远程服务，无返回值
//	 * @param session
//	 * @param serviceName
//	 * @param param
//	 * @throws RequestWriteException 
//	 */
//	public void invoke(IoSession session,String serviceName,String param) throws RequestWriteException{
//		RPCRequest request = new RPCRequest();
//		request.setName(serviceName);
//		request.setParam(param);
//		invoke(session,request);
//	}

	/**
	 * 调用远程服务，无返回值
	 * @param session
	 * @throws RequestWriteException
	 */
	public void invoke(IoSession session,RPCRequest request) throws RequestWriteException{
		String sessionid = UUIDGenerator.uuid();
		request.setSessionid(sessionid);
		logger.debug(">>*"+request);
		session.write(request);
	}

	/**
	 * 异步调用，并指定回调函数
	 * @param session
	 * @param request
	 * @param rqListener
	 * @throws RequestWriteException
	 */
	public void invoke(IoSession session,RPCRequest request,RequestListener rqListener) throws RequestWriteException{
		AbstractMinaHandler clientHandle = (AbstractMinaHandler) session.getHandler();
		String sessionid =UUIDGenerator.uuid();
		request.setSessionid(sessionid);
		
		rqListener.setRequest(request);
		clientHandle.addRequestListener(rqListener);
		
		logger.debug(">>*"+request);
		session.write(request);
//		wf.awaitUninterruptibly();
//		if(!wf.isWritten()){
//			throw new RequestWriteException();
//		}
	}
	
//	/**
//	 * 调用指定的服务，发出请求
//	 * 
//	 * @param session
//	 * @param serviceName
//	 * @param param
//	 * @return
//	 * @throws InterruptedException
//	 * @throws RequestTimeOutException 
//	 */
//	public RPCResponse invokeWithReturn(IoSession session, String serviceName,
//			String param) throws InterruptedException, RequestTimeOutException {
//		return invokeWithReturn(session,serviceName,param,null);
//	}
//	/**
//	 * 调用指定的服务，发出请求
//	 * 
//	 * @param session
//	 * @param serviceName
//	 * @param param
//	 * @return
//	 * @throws InterruptedException
//	 * @throws RequestTimeOutException 
//	 */
//	public RPCResponse invokeWithReturn(IoSession session, String serviceName,
//			String param,byte[] body) throws InterruptedException, RequestTimeOutException {
//		RPCRequest request = new RPCRequest();
//		RPCResponse response;
//		request.setName(serviceName);
//		request.setParam(param);
//		request.setBody(body);
//		response = invokeWithReturn(session,request);
//		return response;
//	}
	/**
	 * 指定的session的response是否存在
	 * @param sessionid
	 * @return
	 */
	public boolean isHaveResponse(String sessionid) {
		RPCResponse response = responseMap.get(sessionid);
		return response != null;
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
	public RPCResponse invokeWithReturn(IoSession session, RPCRequest request)
			throws InterruptedException, RequestTimeOutException {
		RPCResponse response = null;
		String sessionid = UUIDGenerator.uuid();
		request.setSessionid(sessionid);
		requestMap.put(request.getSessionid(), request);
		logger.debug(">>"+request);
		session.write(request);
//		CallThread ct = new CallThread(this,request);
//		ct.start();
//		synchronized (ct) {
//			ct.wait();
//			response = responseMap.get(request.getSessionid());
//			logger.debug("<<"+response);
//			if(response == null){
//				throw new RequestTimeOutException();
//			}
//		}
		long currentTime = 0;
		long s = System.currentTimeMillis();
		long timeout = Long.parseLong(SystemConfig.getProperty("channel.server.request.timeout","60"));	//60s超时
		while (true) {
			logger.debug("监听结果【"+sessionid+"】"+responseMap.keySet().size());
			logger.debug("responseMap size:"+responseMap.size());
			Object tmp = responseMap.get(sessionid);
			if (tmp != null) { 
				response = (RPCResponse) tmp;
				logger.debug("收到结果【"+sessionid+"】");
				break;
			}
			try {
				Thread.currentThread().sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			long e = System.currentTimeMillis();
			currentTime = ((e - s)/1000);
			if(currentTime>timeout){
				logger.debug("REQUEST TIME OUT["+request.getSessionid()+"]");
				break;
			}
		}
		responseMap.remove(request.getSessionid());
		requestMap.remove(request.getSessionid());
		return response;
	}
}