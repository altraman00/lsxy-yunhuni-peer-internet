package com.lsxy.framework.rpc.api;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;

/**
 * 相应对象
 * @author tantyuo
 */
public class RPCResponse extends RPCMessage{
	
	public static final String STATE_OK = "0";
	public static final String STATE_EXCEPTION = "1";
	
	private String message;		//相应内容 RP

	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}



	
	/**
	 * 是否响应正常
	 * @return
	 */
	public boolean isOk(){
		return message.equals(STATE_OK);
	}
	
	/**
	 * 根据请求对象创建对应的响应对象
	 * @param request
	 * @return
	 */
	public static RPCResponse buildResponse(RPCRequest request){
		RPCResponse response = new RPCResponse();
		response.setSessionid(request.getSessionid());
		response.setTimestamp(request.getTimestamp());
		return response;
	}

	@Override
	public String toString() {
		String sBody = this.getBodyAsString();
		return "P["+this.getSessionid()+"]["+this.getTimestamp()+"]["+this.tryTimes+"]["+this.lastTryTimestamp+"]>>RP:" +this.message + ">>BODY:"+sBody;
	}
	
}
