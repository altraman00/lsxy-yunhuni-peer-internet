package com.lsxy.app.portal.web.utils;

import java.io.StringWriter;
import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

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
}
