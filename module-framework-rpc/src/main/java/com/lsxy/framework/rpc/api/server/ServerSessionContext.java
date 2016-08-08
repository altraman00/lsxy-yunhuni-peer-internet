package com.lsxy.framework.rpc.api.server;

import com.lsxy.framework.rpc.api.SessionContext;
import org.apache.commons.collections.map.ListOrderedMap;
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
	private ListOrderedMap sessionMap = new ListOrderedMap();


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
		return (Session) sessionMap.get(sessionid);
	}

	/**
	 * 根据索引值获取Session
	 * @param idx
	 * @return
     */
	public Session getSession(int idx){
		return (Session) this.sessionMap.getValue(idx);
	}

	/**
	 * 获取对的区域代理连接会话
	 * 会根据线路成本,运营商,代理繁忙情况获取到合适的区域代理进行处理
	 * @return
     */
	public Session getRightSession(){
		Session session = null;
		try {
			session = (Session) this.sessionMap.getValue(0);
		}catch(Exception ex){
			logger.error("没有找到有效的与区域会话");
		}
		return session;

	}

}
