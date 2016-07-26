package com.lsxy.framework.mq.api;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import com.lsxy.framework.config.SystemConfig;

import static com.lsxy.framework.core.web.SpringContextUtil.getBean;

/**
 * MQ事件Handler查找
 * @author tandy
 *
 */
@Component
@SuppressWarnings({ "rawtypes", "unchecked" })
public class MQHandlerFactory {

    @Autowired
    private ApplicationContext applicationContext;

	public MQHandlerFactory(){
	}
	private static final Log logger = LogFactory.getLog(MQHandlerFactory.class);
	private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();  
	private final String RESOURCE_PATTERN = "/**/*.class";

	private String handleBasePackages =  SystemConfig.getProperty("global.mq.handle.basepackage","com.lsxy");

	
	//初始化的handlers  <MQEventClassName,Handler>
	private Map<String,Set<Class<? extends MQMessageHandler>>> handlersMap =
			new HashMap<>();
	
	/** 
	 * 加载所有的handlers类
	 * @return
	 * @throws IOException
	 */
	@PostConstruct
	public void loadMQHandlers() throws IOException{
		this.init();
	}


	/**
	 * 初始化handler集合，扫描类路径中所有的handler并放入map中方便使用
	 */
	public void init(){
		logger.debug("scan package "+ handleBasePackages +" for global event handlers");
		Reflections reflections = new Reflections(handleBasePackages);
		Set<Class<? extends MQMessageHandler>> handlers = reflections.getSubTypesOf(MQMessageHandler.class);
		logger.debug("found "+handlers.size()+" handlers");
		for (Class<? extends MQMessageHandler> handlerClass : handlers) {
			String eventClassName = getEventClassName(handlerClass);
			logger.debug("[GEH]"+handlerClass+" for " + eventClassName);
			//一开始初始化的时候，handler映射表中对应类名的处理handler设置为空，只需要知道有这个处理类就可以了
			Set<Class<? extends MQMessageHandler>> handlesSet = this.handlersMap.get(eventClassName);
			if(handlesSet == null){
				handlesSet = new HashSet<>();
			}
			handlesSet.add(handlerClass);
			this.handlersMap.put(eventClassName,handlesSet);
		}
	}
	/**
	 * 根据handler class 获取handler类的实现接口所使用的泛型类名，用于确定该handler是处理什么类型事件的handler
	 * @param handlerClass
	 * @return
	 */
	private String getEventClassName(
			Class<? extends MQMessageHandler> handlerClass) {
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
	 * 获取指定handler类的类名
	 * @param class1
	 * @return
	 */
	private Set<Class<? extends MQMessageHandler>> getHandlerClass(Class class1) {
		return this.handlersMap.get(class1.getName());
	}
	
	/**
	 * 获取指定mq事件对象的handler
	 * @param event 触发的事件
	 * @return
	 */
	public Set<Class<? extends MQMessageHandler>> getHandler(MQEvent event){
		MQMessageHandler result = null;
		Set<Class<? extends MQMessageHandler>> handlerClass = this.getHandlerClass(event.getClass());
		return handlerClass;
	}
	
}
