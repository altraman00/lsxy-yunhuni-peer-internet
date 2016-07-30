package com.lsxy.framework.rpc.api.server;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * IOSession环境变量
 * @author tandy
 *
 */
public class SessionContext {
	public static final Log logger = LogFactory.getLog(SessionContext.class);
	private static Map<String,Session> sessionMap = new HashMap<String,Session>();//用于存储clientId与session间的映射关系
	private static Map<Long,String> sessionClientIdMap = new HashMap<Long,String>();//用户存储sessionid与clientId间的映射关系
	
	public void putSession(String nodeAgentId,Session session){
		sessionMap.put(nodeAgentId, session);
		sessionClientIdMap.put(session.getId(), nodeAgentId);
//		String base = toBase64Text(session);
//		logger.debug("********** Session id: " + session.getId());
//		logger.debug("********** SessionBase64: " + base);
//		Session baseSession = buildFromBase64(base);
//		logger.debug("********** Base session id: " + baseSession.getId());
	}
	
	public void removeSession(String nodeAgentId){
		logger.debug("remove socket session:"+nodeAgentId);
		Session session = sessionMap.get(nodeAgentId);
		sessionClientIdMap.remove(session.getId());
		sessionMap.remove(nodeAgentId);
		
	}
	public void removeSession(Session session){
		String nodeAgentId = sessionClientIdMap.get(session.getId());
		removeSession(nodeAgentId);
	}

	public Session getSession(String nodeAgentId){
		Session session = sessionMap.get(nodeAgentId);
		return session;
	}
	
	public Map<String,Session> getSessionMap(){
		return sessionMap;
	}
	
	/**
	 * 根据session获取对应的clientId;
	 * @param session
	 * @return
	 */
	public String getClientId(Session session){
		return sessionClientIdMap.get(session.getId());
	}
	
	/**
	 * 将对象序列化成base64
	 * @return
	 */
	public String toBase64Text(Session session){
		String result = null;
		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		try {
			ObjectOutputStream oos = new ObjectOutputStream(bao);
			oos.writeObject(session);
			byte[] bytes = bao.toByteArray();
			result = Base64.encodeBase64String(bytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 对象从base64反向构建
	 * @param base64
	 * @return
	 */
	public static Session buildFromBase64(String base64){
		byte[] bytes = Base64.decodeBase64(base64);
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		Session session = null;
		try {
			ObjectInputStream ois = new ObjectInputStream(bais);
			Object obj = ois.readObject();
			session = (Session) obj;
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return session;
	}
}
