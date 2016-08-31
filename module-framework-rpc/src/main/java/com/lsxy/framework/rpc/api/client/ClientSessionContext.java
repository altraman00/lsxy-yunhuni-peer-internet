package com.lsxy.framework.rpc.api.client;

import com.lsxy.framework.rpc.api.session.Session;
import com.lsxy.framework.rpc.api.session.SessionContext;
import org.apache.commons.collections.map.ListOrderedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 * Created by tandy on 16/7/30.
 */
public class ClientSessionContext extends SessionContext{

    private static final Logger logger = LoggerFactory.getLogger(ClientSessionContext.class);

    //clientId-serverUrl 作为session
    private ListOrderedMap sessions = new ListOrderedMap();

    /**
     * 放入session对象到环境中
     * @param session
     */
    public void putSession(Session session){
        sessions.put(session.getId(),session);
        sessions.put(session.getServerUrl(),session);
    }

    /**
     * 获取多少个会话个数
     * @return
     */
    public int size(){
        return sessions.size();
    }


    /**
     * 会话交互过程中,根据sessionid获取会话对象
     * @param sessionid
     * @return
     */
    public Session getSession(String sessionid) {
        return (Session) sessions.get(sessionid);
    }

    @Override
    public Session getSession(int idx) {
        return (Session) this.sessions.getValue(idx);
    }
//
//    /**
//     * 根据serverUrl获取会话对象
//     * 提供给后台监控线程,不要重复连接其中一台服务器
//     * 但是断开后从重复尝试连接
//     * @param serverUrl
//     * @return
//     */
//    public Session getSessionByServerUrl(String serverUrl){
//        return (Session) sessions.get(serverUrl);
//    }

    /**
     * 根据会话标识清理会话数据
     * @param sessionid
     */
    public void remove(String sessionid) {
        Session session = (Session) sessions.get(sessionid);
        if(session != null) {
            sessions.remove(sessionid);
            sessions.remove(session.getServerUrl());
        }
    }

    @Override
    public Collection<Session> sessions() {
        return sessions.values();
    }


}
