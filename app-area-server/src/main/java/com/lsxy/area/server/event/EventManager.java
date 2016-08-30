package com.lsxy.area.server.event;

import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.RPCResponse;
import com.lsxy.framework.rpc.api.server.Session;
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
            logger.info("注册消息处理器:{},{}",handler.getEventName(),handler);
        }
    }

    public RPCResponse fire(RPCRequest request, Session session){
        String method = (String) request.getParameter("method");
        if(logger.isDebugEnabled()){
            logger.debug("收到CTI事件:{}-----{}" , method,request.getParamMap());
        }
        EventHandler handler = handlers.get(method);
        return handler.handle(request,session);
    }
}
