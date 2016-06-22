package com.lsxy.framework.core.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Map;

/**
 * http client工具方法
 * 
 * @author tandy
 * 迁移到 httpclient 4.5.2  2016/06/22
 * 
 */
public class HttpClientUtil {
	private static Log logger = LogFactory.getLog(HttpClientUtil.class);

	/**
	 * 调用http get
	 * 
	 * @param url
	 * @return
	 */
	public static String httpGet(String url) {
		return "";
	}



	/**
	 * <p>
	 * 执行一个HTTP POST请求，返回请求响应的HTML
	 * </p>
	 * 
	 * @param url
	 *            请求的URL地址
	 * @param params
	 *            请求的查询参数,可以为null
	 * @return 返回请求响应的HTML
	 */
	public static String httpPost(String url,Map<String, String> params) {
		return "";
	}

	/**
	 * 发送POST请求，直接将参数作为body
	 */

	public static String httpPostContent(String url, String body){
		return null;
	}
	

}
