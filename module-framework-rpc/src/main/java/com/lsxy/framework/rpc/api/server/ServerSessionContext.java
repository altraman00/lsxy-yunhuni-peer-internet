package com.lsxy.framework.rpc.api.server;

import com.lsxy.framework.rpc.api.session.Session;
import com.lsxy.framework.rpc.api.session.SessionContext;
import org.apache.commons.collections.map.ListOrderedMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Collection;

/**
 * IOSession环境变量
 * @author tandy
 *
 */
public class ServerSessionContext extends SessionContext{
	public static final Log logger = LogFactory.getLog(ServerSessionContext.class);
	//<sessionid,session>
	private ListOrderedMap sessionMap = new ListOrderedMap();
	//<areaid-nodeid,session>
	private ListOrderedMap areaSessionMap = new ListOrderedMap();

	//<sessionid,areaid-nodeid>
	private ListOrderedMap sessionIdAreaMap = new ListOrderedMap();


	/**
	 * 注册区域代理连接会话
	 * @param areaid
	 * @param nodeid
	 * @param session
     */
	public void putSession(String areaid,String nodeid,Session session){
		String sid = areaid+"-"+nodeid;
		areaSessionMap.put(sid,session);
		putSession(session);
		sessionIdAreaMap.put(session.getId(),sid);
	}


	public void remove(String sessionid){
		sessionMap.remove(sessionid);
		String sid = (String) sessionIdAreaMap.get(sessionid);
		areaSessionMap.remove(sid);
		sessionIdAreaMap.remove(sessionid);
	}



	@Override
	public Collection<Session> sessions() {
		return sessionMap.values();
	}

	@Override
	public void putSession(Session session) {
		sessionMap.put(session.getId(),session);
	}

	/**
	 * 根据区域标识和节点标识查询对应的会话,确定一个区域的一个节点只能允许连接一次
	 * @param areaid
	 * @param nodeid
     * @return
     */
	public Session getSessionByArea(String areaid,String nodeid) {
		String sid = areaid + "-" + nodeid;
		return (Session) areaSessionMap.get(sid);
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
	@Override
	public Session getSession(int idx){
		return (Session) this.sessionMap.getValue(idx);
	}

//	/**
//	 * 获取对的区域代理连接会话
//	 * 会根据线路成本,运营商,代理繁忙情况获取到合适的区域代理进行处理
//	 * @return
//     */
//	@Override
//	public Session getRightSession() throws RightSessionNotFoundExcepiton {
//		Session session;
//		try {
//			session = (Session) this.sessionMap.getValue(0);
//		}catch(Exception ex){
//			throw new RightSessionNotFoundExcepiton("没有找到有效的区域会话");
//		}
//		return session;
//	}

	@Override
	public int size() {
		return this.sessionMap.size();
	}

	/**
	 * 获取指定区域的所有会话
	 * @param areaid
	 * @return
     */
	public ListOrderedMap getSessionsByArea(String areaid) {
		ListOrderedMap result = new ListOrderedMap();
		for(Object key:this.areaSessionMap.keySet()){
			String strKey = (String) key;
			if(strKey.substring(0,strKey.indexOf("-")).equals(areaid)){
				result.put(strKey,areaSessionMap.get(key));
			}
		}
		return result;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		for (Object key:areaSessionMap.keySet()) {
			Session session = (Session) areaSessionMap.get(key);
			sb.append("{\"nodeId\":\""+key + "\",\"host\":\"" + session.getRemoteAddress().getHostName() + "\"},");
		}
		if(sb.length() > 1){
			sb.deleteCharAt(sb.length()-1);
		}
		sb.append("]");
		return sb.toString();
	}

}
