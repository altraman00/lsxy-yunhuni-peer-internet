package com.lsxy.framework.mq.api;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.reflections.Reflections;

import com.lsxy.framework.config.SystemConfig;
import com.lsxy.framework.core.utils.StringUtil;
import com.lsxy.framework.core.web.SpringContextUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * 全局事务handler工厂类
 * @author tandy
 *
 */
@SuppressWarnings({"rawtypes","unchecked"})
@Component
public class GlobalEventHandlerFactory implements InitializingBean{
	
	private static final Log logger = LogFactory.getLog(GlobalEventHandlerFactory.class);
	
	private Map<String,String> handlers = new HashMap<String,String>();
	
	private final String BASE_PACKAGE = "com.lsxy";
	
	/**
	 * 查询对应事件对象是否存在对应的处理handler并返回
	 * @param eventName
	 * @return
	 * @throws ClassNotFoundException
	 */
	public GlobalEventHandler<? extends MQEvent> getHandler(String eventName){
		String handlerClassName = this.handlers.get(eventName);
		if(StringUtil.isNotEmpty(handlerClassName)){
			Class handlerClass;
			try {
				handlerClass = Class.forName(handlerClassName);
				Object handler = SpringContextUtil.getBean(handlerClass);
				return (GlobalEventHandler<? extends MQEvent>) handler;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	

	/**
	 * 根据handler class 获取handler类的实现接口所使用的泛型类名，用于确定该handler是处理什么类型事件的handler
	 * @param handlerClass
	 * @return
	 */
	private String getEventClassName(
			Class<? extends GlobalEventHandler> handlerClass) {
		String result = null;
		Type[] x = handlerClass.getGenericInterfaces();
		for (Type type : x) {
			if(type instanceof ParameterizedType){
				Class<?> classx = (Class<?>) ((ParameterizedType)type).getActualTypeArguments()[0];
				result = classx.getName();
			}
		}
		return result;
	}

	/**
	 * 初始化handler集合，扫描类路径中所有的handler并放入map中方便使用
	 * @throws Exception
     */
	@Override
	public void afterPropertiesSet() throws Exception {
		String basePackage = SystemConfig.getProperty("mq.kafka.handlers.basepackage",BASE_PACKAGE);
		logger.debug("scan package "+basePackage+" for global event handlers");
		Reflections reflections = new Reflections(basePackage);
		Set<Class<? extends GlobalEventHandler>> handlers = reflections.getSubTypesOf(GlobalEventHandler.class);
		logger.debug("found "+handlers.size()+" handlers");
		for (Class<? extends GlobalEventHandler> handlerClass : handlers) {
			String eventClassName = getEventClassName(handlerClass);
			logger.debug("[GEH]"+handlerClass+" for " + eventClassName);
			//一开始初始化的时候，handler映射表中对应类名的处理handler设置为空，只需要知道有这个处理类就可以了
			this.handlers.put(eventClassName, handlerClass.getName());
		}
	}
}
