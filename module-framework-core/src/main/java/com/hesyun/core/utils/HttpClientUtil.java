package com.hesyun.core.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hesyun.core.SystemConfig;

/**
 * http client工具方法
 * 
 * @author tandy
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
		String ret = null;
		try {
			logger.debug("http get:" + url);
			HttpClient client = buildHttpClient(url);
			GetMethod getMethod = new GetMethod(url);
			client.executeMethod(getMethod);
			ret = new String(getMethod.getResponseBody(), "UTF-8");
			logger.debug("结果：" + ret);
			getMethod.releaseConnection();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return ret;
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
		logger.debug("http post:" + url);
		StringBuffer response = new StringBuffer();

		HttpClient client = buildHttpClient(url);
		PostMethod method = new PostMethod(url);

		// 设置Http Post数据
		if (params != null) {
			Vector<NameValuePair> nameValuePairs = new Vector<NameValuePair>();
			for (Map.Entry<String, String> keyEntry : params.entrySet()) {
				String key = keyEntry.getKey();
				String value = params.get(keyEntry.getKey());
				nameValuePairs.add(new NameValuePair(key, value));
			}
			NameValuePair[] xx = new NameValuePair[nameValuePairs.size()];
			method.setRequestBody(nameValuePairs.toArray(xx));
		}
		try {
			method.addRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
			client.executeMethod(method);
			if (method.getStatusCode() == HttpStatus.SC_OK) {
				// 读取为 InputStream，在网页内容数据量大时候推荐使用
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(method.getResponseBodyAsStream(),
								"UTF-8"));
				String line;
				while ((line = reader.readLine()) != null) {
					response.append(line);
				}
				reader.close();
			}
		} catch (IOException e) {
			System.out.println("执行HTTP Post请求" + url + "时，发生异常！");
			e.printStackTrace();
		} finally {
			method.releaseConnection();
		}
		logger.debug("结果：" + response);
		return response.toString();
	}

	/**
	 * 发送POST请求，直接将参数作为body
	 */

	public static String httpPostContent(String url, String body)
			throws IOException {
		logger.debug("http post:" + url);
		logger.debug("post body:" + body);
		String responseBody = null;
		HttpClient httpClient = buildHttpClient(url);
		PostMethod postMethod = new PostMethod(url);
		try {
			StringRequestEntity requestEntity = new StringRequestEntity(body,"text/json","UTF-8");
			postMethod.setRequestEntity(requestEntity);
			httpClient.executeMethod(postMethod);
			responseBody = postMethod.getResponseBodyAsString();  
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			postMethod.releaseConnection();
		}
		logger.debug("result:" + responseBody);
		return responseBody;

	}
	

	public static boolean isIP(String ip) {
	    if (ip == null || ip.isEmpty()) return false;
	    ip = ip.trim();
	    if ((ip.length() < 6) & (ip.length() > 15)) return false;

	    try {
	        Pattern pattern = Pattern.compile("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");
	        Matcher matcher = pattern.matcher(ip);
	        return matcher.matches();
	    } catch (PatternSyntaxException ex) {
	        return false;
	    }
	}

	/**
	 * 构建一个http client对象
	 * 
	 * @return
	 */
	public static HttpClient buildHttpClient(final String url) {
		// 设置代理
		// 创建httpClient客户端
		HttpClient client = new HttpClient();
		try {
			URL urlx = new URL(url);
			String host = urlx.getHost();
			logger.debug("http host : " + host);
			if(isIP(host)){
				logger.debug("ip is internal addr,not need proxy");
				return client;
			}
			String excludeUrl = SystemConfig.getProperty(
					"http.proxy.exclude.hosts", "192.168.3.2,aa.hesyun.com");
			if (excludeUrl.indexOf(host) >= 0) {
				logger.debug("the url is exclude url ,not need proxy");
				return client;
			}
		} catch (MalformedURLException e) {
		}
		
		
		if (SystemConfig.getProperty("http.proxy.enable", "false").equals(
				"true")) {
			String hostname = SystemConfig.getProperty("http.proxy.hostname");
			int port = Integer.parseInt(SystemConfig.getProperty(
					"http.proxy.port", "0"));
			logger.debug("使用http代理:" + hostname + ":" + port);
			String proxyUser = SystemConfig.getProperty("http.proxy.username");
			String proxyPassword = SystemConfig
					.getProperty("http.proxy.passwd");
			client.getHostConfiguration().setProxy(hostname, port);
			// 代理验证信息
			client.getParams().setAuthenticationPreemptive(true);
			UsernamePasswordCredentials creds = new UsernamePasswordCredentials(
					proxyUser, proxyPassword);
			client.getState().setProxyCredentials(AuthScope.ANY, creds);
		}
		return client;
	}



	public static InputStream httpGetInputStream(String url) {
		HttpClient client = buildHttpClient(url);
		GetMethod getMethod = new GetMethod(url);
		InputStream is = null;
		try {
			client.executeMethod(getMethod);
			is = getMethod.getResponseBodyAsStream();
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
//			getMethod.releaseConnection();
		}
		return is;
	}
	
	

}
