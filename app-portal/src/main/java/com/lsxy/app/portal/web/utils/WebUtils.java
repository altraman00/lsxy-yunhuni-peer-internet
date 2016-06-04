package com.lsxy.app.portal.web.utils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.lang.reflect.Field;
import java.util.Enumeration;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lsxy.framework.config.SystemConfig;
import com.lsxy.framework.core.utils.BeanUtils;
import com.lsxy.framework.core.utils.StringUtil;


/**
 * @author tantyou
 */
@SuppressWarnings("unchecked")
public class WebUtils {
	private static Log logger = LogFactory.getLog(WebUtils.class);

	private WebUtils() {
	}
	
	/**
	 *  向客户端输出json字符串
	 * @param response
	 * @param json
	 */
	public static void outputJson(HttpServletResponse response,String json){
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json;charset=utf-8");
		try {
			logger.debug("response output :"+json);
			response.getWriter().print(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 重载Spring WebUtils中的函数,作用如函数名所示 加入泛型转换,改变输入参数为request 而不是session
	 *
	 * @param name  session中变量名称
	 * @param clazz session中变量的类型
	 */
	public static <T> T getOrCreateSessionAttribute(HttpServletRequest request, String name, Class<T> clazz) {
		return (T) org.springframework.web.util.WebUtils.getOrCreateSessionAttribute(request.getSession(), name, clazz);
	}
	
	/**
	 * 取得request参数值,根据是否需要转码配置进行相应的转码工作
	 * @param request
	 * @param name
	 * @return
	 */
	public static String getRequestParam(HttpServletRequest request,String name){
		String method = request.getMethod().trim().toLowerCase();
		String ret = request.getParameter(name);
		if(StringUtil.isNotEmpty(ret) && method.equals("get")){ 
			String encode = SystemConfig.getProperty("JTEAP_SYSTEM_REQUEST_ENCODE");
			if(StringUtil.isNotEmpty(encode) && encode.toLowerCase().equals("true")){
				String charset = SystemConfig.getProperty("JTEAP_SYSTEM_REQUEST_ENCODE_CHARSET");
				try {
					if (ret != null && !ret.equals("")) {
						byte[] byteStr = ret.getBytes("ISO-8859-1");
						ret = new String(byteStr,charset);
					}
				} catch (Exception e) {
				}
				
			}
		}
		return ret;
	}
	
	/**
	 * 得到request的queryString
	 * @param request
	 * @return
	 */
	public static String getQueryString(HttpServletRequest request){
		String ret = request.getQueryString();
		if(StringUtil.isNotEmpty(ret)){
			String encode = SystemConfig.getProperty("JTEAP_SYSTEM_REQUEST_ENCODE");
			if(StringUtil.isNotEmpty(encode) && encode.toLowerCase().equals("true")){
				String charset = SystemConfig.getProperty("JTEAP_SYSTEM_REQUEST_ENCODE_CHARSET");
				try {
					if (ret != null && !ret.equals("")) {
						byte[] byteStr = ret.getBytes("ISO-8859-1");
						ret = new String(byteStr,charset);
					}
				} catch (Exception e) {
				}
				
			}
		}
		return ret;
	}
	
	/**
	 * 输出请求参数方法
	 * @param request
	 */
	public static void logRequestParams(HttpServletRequest request){
		if(!logger.isDebugEnabled()){
			return;
		}
		logger.debug("Http请求 "+request.getRequestURL()+" 参数如下：");
		Enumeration<String> params = request.getParameterNames();
		while(params.hasMoreElements()){
			String paramName = params.nextElement();
			String value = request.getParameter(paramName);
			logger.debug(paramName+":"+value);
		}
	}

	/**
	 * 输出请求参数方法
	 * @param request
	 */
	public static void logRequestHeaders(HttpServletRequest request){
		if(!logger.isDebugEnabled()){
			return;
		}
		logger.debug("Http请求 "+request.getRequestURL()+" Header参数如下：");
		Enumeration<String> params = request.getHeaderNames();
		while(params.hasMoreElements()){
			String paramName = params.nextElement();
			String value = request.getHeader(paramName);
			logger.debug(paramName+":"+value);
		}
	}

	
	/**
	 * 根据请求参数，将参数值设置到指定bean对象的对应的属性中
	 * @param request
	 */
	public static void setRequestFormValuesToBean(HttpServletRequest request,
			Object object) {
		Enumeration<String> params = request.getParameterNames();
		while(params.hasMoreElements()){
			String paramName = params.nextElement();
			String value = getRequestParam(request, paramName);
			Field field = null;
			try {
				field = BeanUtils.getDeclaredField(object.getClass(), paramName);
			} catch (NoSuchFieldException e) {
			}
			if(field == null){
				continue;
			}else{
				try {
					BeanUtils.setProperty(object, paramName,value);
				} catch (Exception e) {
					logger.error("为对象"+object+"设置属性"+paramName+"时出现异常，该值为"+value);
					e.printStackTrace();
				}
			}
			
		}
		
	}
	

	/**
	 * 获取调用者ip地址
	 * @param request
	 * @return
	 */
	public static String getRemoteAddress(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
	
	/**
	 * 获取mac地址
	 * @param ip
	 * @return
	 */
	 public String getMACAddress(String ip) {
	        String str = "";
	        String macAddress = "";
	        try {
	            Process p = Runtime.getRuntime().exec("nbtstat -a " + ip);
	            InputStreamReader ir = new InputStreamReader(p.getInputStream());
	            LineNumberReader input = new LineNumberReader(ir);
	            for (int i = 1; i < 100; i++) {
	                str = input.readLine();
	                if (str != null) {
	                    //if (str.indexOf("MAC Address") > 1) {
	                    if (str.indexOf("MAC") > 1) {
	                        macAddress = str.substring(
	                                str.indexOf("=") + 2, str.length());
	                        break;
	                    }
	                }
	            }
	        } catch (IOException e) {
	            e.printStackTrace(System.out);
	        }
	        return macAddress;
	    }

	/**
	 * 获取cookie
	 * @param request
	 * @param name
	 * @return String
	 */
	public static String getCookie(HttpServletRequest request, String name) {
		String value = null;
		try {
			for (Cookie c : request.getCookies()) {
				if (c.getName().equals(name)) {
					value = c.getValue();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}
	 
}
