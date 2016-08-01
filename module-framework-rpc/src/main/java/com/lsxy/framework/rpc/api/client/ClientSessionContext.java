package com.lsxy.framework.rpc.api.client;

import com.lsxy.framework.rpc.api.server.Session;
import com.lsxy.framework.rpc.mina.client.MinaClientSession;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tandy on 16/7/30.
 */
@Component
public class ClientSessionContext {


    //clientId-serverUrl 作为session
    private Map<String,Session> sessions = new HashMap<>();


    public void putSession(MinaClientSession session){
        sessions.put(session.getId(),session);

    }

    public Session getSession(String sessionid) {
        return sessions.get(sessionid);
    }

    public void remove(String sessionid) {
        sessions.remove(sessionid);
    }
}
