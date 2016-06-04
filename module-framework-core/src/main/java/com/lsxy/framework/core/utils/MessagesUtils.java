package com.lsxy.framework.core.utils;

import java.io.File;
import java.net.URL;
import java.text.MessageFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lsxy.framework.core.Constants;

/**
 * 
 * @author WangYun
 *
 */
public class MessagesUtils {
	private static Log logger=LogFactory.getLog(MessagesUtils.class);
	private static PropertiesUtil pp;
	static{
		URL url = MessagesUtils.class.getClassLoader().getResource(Constants.MESSAGE_CONFIG_FILE);
		if(url != null){
			String file = url.getFile();
			file = file.replaceAll("%20", " ");
			pp = new PropertiesUtil(file,Constants.MESSAGE_CONFIG_FILE_ENCODING);
			File filex =new File(file);
			logger.debug("loading message file:"+file);
			MessagesUtils.setProperty("lastmodified", String.valueOf(filex.lastModified()));
		}
	}

	/**
	 * 获取指定的消息值
	 * @param name
	 * @param args
	 * @return
	 */
	public static String getMessageText(String name, Object ... args){
		String value = null;
		if(pp != null){
			value = pp.getProperties(name);
		}
		value = MessageFormat.format(value, args);
		return value;
	}
	
	/**
	 * 设置配置
	 * @param name
	 * @param value
	 */
	public static void setProperty(String name,String value){
		pp.setProperties(name, value);
	}
	
	public static void main(String[] args) {
		String xx = "系统消息：\n有新用户[{0}]通过{1}号客服通道关注了客服微信公众号，请点击<a href=\"{2}\">审核</a>";
		
		String msg = MessageFormat.format(xx, "aaa", "1", "http://www.com.com");
		System.out.println(msg);
	}
	
	
}
