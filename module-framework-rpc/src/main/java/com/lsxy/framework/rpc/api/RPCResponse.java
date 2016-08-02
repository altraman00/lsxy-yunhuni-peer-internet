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
	 * 获取字符串形式的body
	 * @return
	 */
	public String getBodyAsString(){
		String s = null;
		if(this.getBody()!=null){
			try {
				s = new String(this.getBody(),"UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return s;
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
		return response;
	}

	@Override
	public String toString() {
		String sBody = null;
		if(this.getBody() == null)
			sBody = "";
		else
			try {
				sBody = new String(this.getBody(),"utf-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return "P["+this.getSessionid()+"]>>RP:" +this.message + ">>BODY:"+sBody;
	}
	
}
