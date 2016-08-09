package com.lsxy.framework.rpc.api.client;

import com.lsxy.framework.rpc.api.SessionContext;
import com.lsxy.framework.rpc.api.server.Session;
import com.lsxy.framework.rpc.mina.client.MinaClientSession;
import org.apache.commons.collections.map.ListOrderedMap;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by tandy on 16/7/30.
 */
@Component
public class ClientSessionContext implements SessionContext{

    //有效会话选择器
    private AtomicInteger sessionSelectCounter = new AtomicInteger(0);

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
     * 根据索引获取会话,获取指定的第几个会话对象
     * @param idx
     * @return
     */
    public Session getSessionByIndex(int idx){
        return (Session) sessions.getValue(idx);

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

    /**
     * 根据serverUrl获取会话对象
     * 提供给后台监控线程,不要重复连接其中一台服务器
     * 但是断开后从重复尝试连接
     * @param serverUrl
     * @return
     */
    public Session getSessionByServerUrl(String serverUrl){
        return (Session) sessions.get(serverUrl);
    }

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


    /**
     * 获取有效的会话
     * 负载均衡算法
     * @return
     */
    public Session getAvalibleSession() {
        int idx = 0;    //索引  默认为0
        int times = 1;  //遍历次数,一次
        int j = 0;      //当前遍历次数
        Session session = null;
        while(j<times && size()>0) {
            int i = this.sessionSelectCounter.addAndGet(1);
            //如果总计数器达到整型最大值,重置0
            if (i >= (Integer.MAX_VALUE - 1)) {
                this.sessionSelectCounter.set(0);
            }
            int size = size();
            idx = i % size;
            //遍历到最后一个表示到了一轮
            if(idx == (size -1)){
                j++;
            }
            session = getSessionByIndex(idx);
            if(session.isValid()){
                return session;
            }else{
                continue;
            }
        }
        return session;
    }
}
