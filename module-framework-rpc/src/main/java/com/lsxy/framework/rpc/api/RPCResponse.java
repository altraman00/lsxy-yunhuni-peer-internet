package com.lsxy.framework.rpc.api;

import com.lsxy.framework.core.utils.StringUtil;

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
		String sBody = this.getBody();
		return "P["+this.getSessionid()+"]["+this.getTimestamp()+"]["+this.tryTimes+"]["+this.lastTryTimestamp+"]>>RP:" +this.message + ">>BODY:"+sBody;
	}

	/**
	 * 序列化
	 * RP:SESSIONID TIMESTAMP MESSAGE BODY
	 * @return
	 */
	@Override
	public String serialize() {
		StringBuffer sb = new StringBuffer("RP:");
		sb.append(this.getSessionid());
		sb.append(" ");
		sb.append(this.getTimestamp());
		sb.append(" ");
		sb.append(this.getMessage());
		sb.append(" ");
		sb.append(encode(this.getBody()));
		return sb.toString();
	}


	public static RPCResponse unserialize(String str){
		RPCResponse response = null;
		if(StringUtil.isNotEmpty(str) && str.matches("RP:\\w{32}\\s\\d{13}+\\s\\w+\\s.*")){
			response = new RPCResponse();
			String[] parts = str.split(" ");
			response.setSessionid(parts[0].substring(3));
			response.setTimestamp(Long.valueOf(parts[1]));
			response.setMessage(parts[2]);
			if(parts.length>=4){
				response.setBody(parts[3]);
			}

		}
		return response;
	}



	public static void main(String[] args) {
		String value = "RP:12341234123412341234123412341234 1481705348021 OK {{{{{s";
//		Pattern pt = Pattern.compile("RQ:\\w[32]\\s\\w+\\s.*");
		System.out.println(value.matches("RP:\\w{32}\\s\\d{13}+\\s\\w+\\s.*"));
		String[] parts = value.split(" ");
		String sessionid = parts[0].substring(3);
		String timestamp = parts[1];
		String message = parts[2];
		System.out.println(message);
		System.out.println(timestamp);

		System.out.println(sessionid);

		RPCResponse response = RPCResponse.unserialize(value);
		System.out.println(response.getMessage());
	}
}
