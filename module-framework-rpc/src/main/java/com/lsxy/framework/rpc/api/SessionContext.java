package com.lsxy.framework.rpc.api;

import com.lsxy.framework.rpc.api.server.Session;
import com.lsxy.framework.rpc.exceptions.RightSessionNotFoundExcepiton;
import com.lsxy.framework.rpc.mina.client.MinaClientSession;

import java.util.Collection;
import java.util.Set;

/**
 * Created by tandy on 16/8/3.
 */
public abstract class SessionContext {

    /**
     * 放入session对象到环境中
     * @param session
     */
    public abstract void putSession(Session session);

    /**
     * 会话交互过程中,根据sessionid获取会话对象
     * @param sessionid
     * @return
     */
    public abstract Session getSession(String sessionid);

    /**
     * 根据会话标识清理会话数据
     * @param sessionid
     */
    public abstract void remove(String sessionid) ;

    /**
     * 获取所有session
     * @return
     */
    public abstract Collection<Session> sessions();


    /**
     * 根据具体情况获取合适的,正确的RPC SESSION对象
     * 去掉 RightSessionNotFoundExcepiton  ,不再抛出该异常
     * 如果没找到就返回空
     * 为了能够让rpccall自己判断是否session为空,统一处理消息重发问题
     * @return
     */
    public abstract Session getRightSession() throws RightSessionNotFoundExcepiton;



}
