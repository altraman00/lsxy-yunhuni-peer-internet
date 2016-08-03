package com.lsxy.framework.rpc.api;

import com.lsxy.framework.rpc.api.server.Session;
import com.lsxy.framework.rpc.mina.client.MinaClientSession;

import java.util.Collection;
import java.util.Set;

/**
 * Created by tandy on 16/8/3.
 */
public interface SessionContext {

    /**
     * 放入session对象到环境中
     * @param session
     */
    public void putSession(Session session);

    /**
     * 会话交互过程中,根据sessionid获取会话对象
     * @param sessionid
     * @return
     */
    public Session getSession(String sessionid);

    /**
     * 根据serverUrl获取会话对象
     * 提供给后台监控线程,不要重复连接其中一台服务器
     * 但是断开后从重复尝试连接
     * @param serverUrl
     * @return
     */
    public Session getSessionByServerUrl(String serverUrl);

    /**
     * 根据会话标识清理会话数据
     * @param sessionid
     */
    public void remove(String sessionid) ;

    /**
     * 获取所有session
     * @return
     */
    public Collection<Session> sessions();

}
