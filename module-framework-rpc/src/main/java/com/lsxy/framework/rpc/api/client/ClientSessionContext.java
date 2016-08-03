package com.lsxy.framework.rpc.api.client;

import com.lsxy.framework.rpc.api.SessionContext;
import com.lsxy.framework.rpc.api.server.Session;
import com.lsxy.framework.rpc.mina.client.MinaClientSession;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tandy on 16/7/30.
 */
@Component
public class ClientSessionContext implements SessionContext{

    //clientId-serverUrl 作为session
    private Map<String,Session> sessions = new HashMap<>();

    /**
     * 放入session对象到环境中
     * @param session
     */
    public void putSession(Session session){
        sessions.put(session.getId(),session);
        sessions.put(session.getServerUrl(),session);
    }


    /**
     * 会话交互过程中,根据sessionid获取会话对象
     * @param sessionid
     * @return
     */
    public Session getSession(String sessionid) {
        return sessions.get(sessionid);
    }

    /**
     * 根据serverUrl获取会话对象
     * 提供给后台监控线程,不要重复连接其中一台服务器
     * 但是断开后从重复尝试连接
     * @param serverUrl
     * @return
     */
    public Session getSessionByServerUrl(String serverUrl){
        return sessions.get(serverUrl);
    }

    /**
     * 根据会话标识清理会话数据
     * @param sessionid
     */
    public void remove(String sessionid) {
        MinaClientSession session = (MinaClientSession) sessions.get(sessionid);
        sessions.remove(sessionid);
        sessions.remove(session.getServerUrl());
    }

    @Override
    public Collection<Session> sessions() {
        return sessions.values();
    }
}
