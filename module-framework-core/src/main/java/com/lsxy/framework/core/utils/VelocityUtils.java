package com.lsxy.framework.core.utils;

import java.io.StringWriter;
import java.util.Map;
import java.util.Properties;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

/**
 * velocity方式格式化模板字符串
 * @author tandy
 *
 */
@SuppressWarnings("rawtypes")
public class VelocityUtils {
	static{
		Velocity.init();
	}
	
	public static String evalString(Map contextMap,String templateString){
		StringWriter writer = new StringWriter();
		VelocityContext context = new VelocityContext(contextMap);
		Velocity.evaluate(context, writer, "", templateString);
		return writer.toString();
	}
	
	public VelocityContext buildContext(Map contextMap){
		VelocityContext context = new VelocityContext(contextMap);
		return context;
	}
	
	
	/**
	 * 根据模板名称获取模板内容
	 * @param temName 模板名称
	 * @return map	参数内容  可为 null
	 */
	public static String getVelocityContext(String temName , Map map){
		// 读取vm中初始化策略
		Properties properties = new Properties();
		properties.setProperty("resource.loader", "class");
		properties.setProperty("class.resource.loader.class", 
				"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		VelocityEngine velocityEngine = new VelocityEngine(properties);
		
		
		VelocityContext context = new VelocityContext();
		if(map != null){
			for(Object key : map.keySet()){
				context.put((String)key, map.get(key));
			}		
		}
		
		StringWriter writer = new StringWriter();
		velocityEngine.mergeTemplate(temName, "utf-8", context, writer);
		
		return writer.toString();
	}
}
