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
public class RPCResponse {
	
	public static final String STATE_OK = "0";
	public static final String STATE_EXCEPTION = "1";
	
	private String sessionid;	//session id
	private String message;		//相应内容 RP
	private byte[] body;		//供大数据传输 BC
	
	public String getSessionid() {
		return sessionid;
	}
	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public byte[] getBody() {
		return body;
	}
	/**
	 * 获取字符串形式的body
	 * @return
	 */
	public String getBodyAsString(){
		String s = null;
		if(this.body!=null){
			try {
				s = new String(this.body,"UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return s;
	}
	
	public void setBody(String body){
		if(body != null){
			try {
				this.body = body.getBytes("UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}else{
			this.body = null;
		}
	}
	public void setBody(byte[] body) {
		this.body = body;
	}
	
	public void setBody(Object obj){
		this.setBody(serializeObject(obj));
	}
	/**
	 * 获取body作为一个object对象
	 * @return
	 */
	public Object getBodyAsObject(){
		ByteArrayInputStream bais = new ByteArrayInputStream(this.getBody());
		ObjectInputStream ois = null;
		Object obj = null;
		try {
			ois = new ObjectInputStream(bais);
			obj = ois.readObject();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}finally{
			if(ois != null){
				try {
					ois.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return obj;
	}

	/**
	 * 序列化一个对象成为一个字节数组
	 * @param obj
	 * @return
	 */
	private byte[] serializeObject(Object obj){
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput out = null;
		byte[] result = null;
		try {
		  out = new ObjectOutputStream(bos);   
		  out.writeObject(obj);
		  result = bos.toByteArray();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
		  try {
		    if (out != null) {
		      out.close();
		    }
		  } catch (IOException ex) {
		  }
		  try {
		    bos.close();
		  } catch (IOException ex) {
		  }
		}
		return result;
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
		if(this.body == null)
			sBody = "";
		else
			try {
				sBody = new String(this.body,"utf-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return "P["+this.sessionid+"]>>RP:" +this.message + ">>BODY:"+sBody;
	}
	
}
