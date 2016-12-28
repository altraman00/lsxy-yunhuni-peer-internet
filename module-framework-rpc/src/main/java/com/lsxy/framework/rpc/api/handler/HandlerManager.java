package com.lsxy.framework.rpc.api.handler;

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
public class HandlerManager {

    private static final Logger logger = LoggerFactory.getLogger(HandlerManager.class);

    @Autowired
    private ApplicationContext applicationContext;

    private Map<String,RpcRequestHandler> handlers = new HashMap<>();

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
        Reflections reflections = new Reflections("com.lsxy");
        Set<Class<? extends RpcRequestHandler>> handlerClasss = reflections.getSubTypesOf(RpcRequestHandler.class);
        for (Class<? extends RpcRequestHandler> handlerClass : handlerClasss) {
            RpcRequestHandler handler = applicationContext.getBean(handlerClass);
            handlers.put(handler.getEventName(),handler);
            if(logger.isDebugEnabled()){
                logger.debug("注册消息处理器:{},{}",handler.getEventName(),handler);
            }

        }
    }

    public RPCResponse fire(RPCRequest request, Session session){
        RpcRequestHandler handler = handlers.get(request.getName());
        if(handler == null){
            return null;
        }
        if(logger.isDebugEnabled()){
            logger.debug("开始处理{}事件,{}",handler.getEventName(),request);
        }
        try{
            return handler.handle(request,session);
        }catch (InvalidParamException e){
            logger.error("非法调用参数:"+request,e);
        }catch (Throwable t){
            logger.error("处理失败:"+request,t);
        }
        return null;
    }
}
