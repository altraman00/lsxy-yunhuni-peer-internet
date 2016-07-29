package com.lsxy.framework.rpc.api;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;


/**
 * RPC 请求对象
 * @author Administrator
 *
 */
public class RPCRequest {
	private String sessionid;	//session id
	private String name;		//RQ
	private String param;		//PM
	private byte[] body;		//BC
	
	private Map<String,String> paramMap;		//参数解析后放入map中以方便调用
	
	
	public String getSessionid() {
		return sessionid;
	}
	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}
	public byte[] getBody() {
		return body;
	}
	public void setBody(byte[] body) {
		this.body = body;
	}
	public String getParam() {
		return param;
	}
	public void setParam(String param) {
		this.param = param;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
				e.printStackTrace();
			}
		return "R["+this.sessionid+"]["+this.name+"]>>PM:" + this.param+">>SESSIONID:"+this.sessionid + ">>BODY:"+sBody;
	}
	
	/**
	 * 解析
	 * @param param
	 */
	public void _parseParam() {
		paramMap = new HashMap<String,String>();
		StringTokenizer st1 = new StringTokenizer(param, "&");
		while (st1.hasMoreTokens()) {
			StringTokenizer st2 = new StringTokenizer(st1.nextToken(), "=");
			String name = st2.nextToken();
			String value = null;
			if(st2.hasMoreElements()){
				value = st2.nextToken();
			}
			paramMap.put(name, value);
		}
	}
	/**
	 * 根据参数名称得到参数值
	 * @param name
	 * @return
	 */
	public String getParameter(String name){
		if(paramMap==null){
			_parseParam();
		}
		return paramMap.get(name);
	}
	/**
	 * 获取参数,如果值为空，则返回默认值
	 * @param name
	 * @param defaultValue
	 * @return
	 */
	public String getParameter(String name,String defaultValue){
		String ret = getParameter(name);
		if(StringUtils.isEmpty(ret)){
			ret = defaultValue;
		}
		return ret;
	}
	public Map<String, String> getParamMap() {
		if (this.paramMap == null && this.param != null)
		{
			this._parseParam();
		}
		return paramMap;
	}
	
	
	
	

}
