package com.lsxy.framework.rpc.api;

import com.lsxy.framework.core.utils.UUIDGenerator;

import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;


/**
 * RPC 请求对象
 * @author Administrator
 *
 */
public class RPCRequest extends  RPCMessage{

	private static final String EQ = "=";
	private static final String AND = "&";
	private static final String EQ_ENCODE = URLEncoder.encode("=");
	private static final String AND_ENCODE = URLEncoder.encode("&");

	private String name;		//RQ
	private String param;		//PM

	
	private Map<String,Object> paramMap;		//参数解析后放入map中以方便调用
	
	
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
		String sBody = this.getBodyAsString();
		return "R["+this.getSessionid()+"]["+ this.getTimestamp()+"]["+this.name+"]["+this.tryTimes+"]["+this.lastTryTimestamp+"]>>PM:" + this.param+">>SESSIONID:"+this.getSessionid() + ">>BODY:"+sBody;
	}
	
	/**
	 * 解析
	 */
	public void _parseParam() {
		paramMap = new HashMap<>();
		StringTokenizer st1 = new StringTokenizer(param, AND);
		while (st1.hasMoreTokens()) {
			StringTokenizer st2 = new StringTokenizer(st1.nextToken(), EQ);
			String name = st2.nextToken();
			String value = null;
			if(st2.hasMoreElements()){
				value = decode(st2.nextToken());
			}
			paramMap.put(name, value);
		}
	}
	/**
	 * 根据参数名称得到参数值
	 * @param name
	 * @return
	 */
	public Object getParameter(String name){
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
	public Object getParameter(String name,Object defaultValue){
		Object ret = getParameter(name);
		if(ret == null){
			ret = defaultValue;
		}
		return ret;
	}
	public Map<String,Object > getParamMap() {
		if (this.paramMap == null && this.param != null)
		{
			this._parseParam();
		}
		return paramMap;
	}


	/**
	 * 创建新的请求对象,必须指定请求命令名称和请求参数
	 *
	 * @param name
	 * 		@see ServiceConstants 请求名称
	 * @param params
	 * 		参数使用url参数方案  param01=001&param002=002
     * @return
	 * 			返回请求对象
     */
	public static RPCRequest newRequest(String name,String params) {
		RPCRequest request = new RPCRequest();
		request.setSessionid(UUIDGenerator.uuid());
		request.setTimestamp(new Date().getTime());
		request.setName(name);
		request.setParam(params);
		return request;
	}

	/**
	 * 创建新的请求对象,必须指定请求命令名称和请求参数
	 * 请求参数以map形式传入,会最终转化为url模式
	 * @param name
	 * 		@see ServiceConstants 请求名称
	 * @param params
	 * 		参数使用url参数方案  map参数
	 * @return
	 * 			返回请求对象
	 */

	public static RPCRequest newRequest(String name,Map<String,Object> params) {
		StringBuffer sb = new StringBuffer();
		for (String key:params.keySet() ) {
			Object value = params.get(key);
			if(value == null){
				continue;
			}
			String v = encode(value.toString());
			sb.append(key + EQ + v + AND);
		}
		if(sb.length() > 0){
			sb.subSequence(0,sb.length()-1);
		}
		return newRequest(name,sb.toString());
	}

	/**
	 * 将=转化为%3D
	 * &转为%26
	 * @return
     */
	public static String encode(String value){
		return value.replaceAll(EQ,EQ_ENCODE).replaceAll(AND,AND_ENCODE);
	}

	public static String decode(String value){
		if(value == null){
			return null;
		}
		return value.replaceAll(EQ_ENCODE,EQ).replaceAll(AND_ENCODE,AND);
	}
}
