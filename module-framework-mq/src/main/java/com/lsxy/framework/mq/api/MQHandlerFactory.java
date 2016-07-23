package com.lsxy.framework.mq.api;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import com.lsxy.framework.config.SystemConfig;
import com.lsxy.framework.core.web.SpringContextUtil;

/**
 * MQ事件Handler查找
 * @author tandy
 *
 */
@Component
@SuppressWarnings({ "rawtypes", "unchecked" })
public class MQHandlerFactory {
	public MQHandlerFactory(){
	}
	private static final Log logger = LogFactory.getLog(MQHandlerFactory.class);
	private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();  
	private final String RESOURCE_PATTERN = "/**/*.class";  
	
	//初始化的handlers  <MQEventClassName,Handler>
	private Map<String,Class<? extends MQMessageHandler<? extends MQEvent>>> handlersMap =
			new HashMap<>();
	
	/** 
	 * 加载所有的handlers类
	 * @return
	 * @throws IOException
	 */
	@PostConstruct
	public void loadMQHandlers() throws IOException{
		logger.debug("MQHandlerFactory.loadMQHandlers"); 
		String pkg = SystemConfig.getProperty("mq.handlers.package","com.lsxy");
		 String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +  
                 ClassUtils.convertClassNameToResourcePath(pkg) + RESOURCE_PATTERN;  
		 Resource[] res = resourcePatternResolver.getResources(pattern);
		 MetadataReaderFactory readerFactory = new CachingMetadataReaderFactory(resourcePatternResolver);  
		 TypeFilter filter = new AnnotationTypeFilter(MQHandler.class);
         for (Resource resource : res) {  
             if (resource.isReadable()) {  
                 MetadataReader reader = readerFactory.getMetadataReader(resource);  
                 if(filter.match(reader, readerFactory)){
                	 String className = reader.getClassMetadata().getClassName();  
                	 try {
						Class<? extends MQMessageHandler<MQEvent>> clazzx = (Class<? extends MQMessageHandler<MQEvent>>) Class.forName(className);
						Object x = clazzx.getGenericSuperclass();
						if(x instanceof ParameterizedType){
							Type[] types =  ((ParameterizedType)x).getActualTypeArguments();
							 for (Type tv : types) {
								if (tv instanceof Class) {
									this.handlersMap.put(((Class) tv).getName(),clazzx);
								}
							} 
						}
					} catch (ClassNotFoundException e) { 
						e.printStackTrace();
					}
                 }
                
             }  
         }  
	}

	/**
	 * 获取指定handler类的类名
	 * @param class1
	 * @return
	 */
	private Class getHandlerClass(Class class1) {
		return this.handlersMap.get(class1.getName());
	}
	
	/**
	 * 获取指定mq事件对象的handler
	 * @param event 触发的事件
	 * @return
	 */
	public MQMessageHandler getHandler(MQEvent event){
		MQMessageHandler result = null;
		Class handlerClass = this.getHandlerClass(event.getClass());
		if(handlerClass != null){
			result = (MQMessageHandler)SpringContextUtil.getApplicationContext().getBean(handlerClass);
		}
		return result;
		
	}
	
}
