package com.lsxy.framework.rpc.api.server;

import com.lsxy.framework.rpc.api.SessionContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * IOSession环境变量
 * @author tandy
 *
 */
@Component
public class ServerSessionContext implements SessionContext{
	public static final Log logger = LogFactory.getLog(ServerSessionContext.class);
	//<sessionid,session>
	private Map<String,Session> sessionMap = new HashMap<>();

	@Override
	public void putSession(Session session){
		sessionMap.put(session.getId(),session);
	}

	public void remove(String sessionid){

		sessionMap.remove(sessionid);
		
	}

	@Override
	public Collection<Session> sessions() {
		return sessionMap.values();
	}

	@Override
	public Session getSession(String sessionid) {
		return sessionMap.get(sessionid);
	}

	@Override
	public Session getSessionByServerUrl(String serverUrl) {
		return null;
	}

}
