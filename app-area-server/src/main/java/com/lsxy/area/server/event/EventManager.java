package com.lsxy.area.server.event;

import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.RPCResponse;
import com.lsxy.framework.rpc.api.session.Session;
import com.lsxy.framework.rpc.exceptions.InvalidParamException;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by liuws on 2016/8/27.
 */
@Component
public class EventManager {

    private static final Logger logger = LoggerFactory.getLogger(EventManager.class);

    @Autowired
    private ApplicationContext applicationContext;

    private Map<String,EventHandler> handlers = new HashMap<>();

    /**
     * 加载所有的handlers类
     * @return
     */
    @PostConstruct
    public void loadMQHandlers() {
        this.init();
    }

    /**
     * 初始化handler集合，扫描类路径中所有的handler并放入map中方便使用
     */
    public void init() {
        Reflections reflections = new Reflections("com.lsxy.area.server.event");
        Set<Class<? extends EventHandler>> handlerClasss = reflections.getSubTypesOf(EventHandler.class);
        for (Class<? extends EventHandler> handlerClass : handlerClasss) {
            EventHandler handler = applicationContext.getBean(handlerClass);
            handlers.put(handler.getEventName(),handler);
            logger.info("注册CTI事件处理器:{},{}",handler.getEventName(),handler);
        }
    }

    public RPCResponse fire(RPCRequest request, Session session){
        String method = (String) request.getParameter("method");
        EventHandler handler = handlers.get(method);
        if(handler == null){
            return null;
        }
        if(logger.isDebugEnabled()){
            logger.debug("开始处理{}事件,{}",handler.getEventName(),request);
        }
        long start = 0;
        if(logger.isDebugEnabled()){
            start = System.currentTimeMillis();
        }
        try{
            return handler.handle(request,session);
        }catch (InvalidParamException e){
            logger.error("非法调用参数:"+request,e);
        }catch (Throwable t){
            logger.error("处理失败"+request,t);
        }finally {
            if(logger.isDebugEnabled()){
                logger.info("处理callevent={}耗时：{}",handler.getEventName(),(System.currentTimeMillis() - start));
            }
        }
        return null;
    }
}
