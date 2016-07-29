package com.hesyun.app.channel.utils.rpc.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.session.IoSession;

/**
 * IOSession环境变量
 * @author tandy
 *
 */
public class IoSessionContext {
	public static final Log logger = LogFactory.getLog(IoSessionContext.class);
	private static Map<String,IoSession> sessionMap = new HashMap<String,IoSession>();//用于存储clientId与session间的映射关系
	private static Map<Long,String> sessionClientIdMap = new HashMap<Long,String>();//用户存储sessionid与clientId间的映射关系
	
	public void putSession(String nodeAgentId,IoSession session){
		sessionMap.put(nodeAgentId, session);
		sessionClientIdMap.put(session.getId(), nodeAgentId);
//		String base = toBase64Text(session);
//		logger.debug("********** Session id: " + session.getId());
//		logger.debug("********** IoSessionBase64: " + base);
//		IoSession baseSession = buildFromBase64(base);
//		logger.debug("********** Base session id: " + baseSession.getId());
	}
	
	public void removeSession(String nodeAgentId){
		logger.debug("remove socket session:"+nodeAgentId);
		IoSession session = sessionMap.get(nodeAgentId);
		sessionClientIdMap.remove(session.getId());
		sessionMap.remove(nodeAgentId);
		
	}
	public void removeSession(IoSession session){
		String nodeAgentId = sessionClientIdMap.get(session.getId());
		removeSession(nodeAgentId);
	}

	public IoSession getSession(String nodeAgentId){
		IoSession session = sessionMap.get(nodeAgentId);
		return session;
	}
	
	public Map<String,IoSession> getSessionMap(){
		return sessionMap;
	}
	
	/**
	 * 根据session获取对应的clientId;
	 * @param session
	 * @return
	 */
	public String getClientId(IoSession session){
		return sessionClientIdMap.get(session.getId());
	}
	
	/**
	 * 将对象序列化成base64
	 * @return
	 */
	public String toBase64Text(IoSession session){
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
	public static IoSession buildFromBase64(String base64){
		byte[] bytes = Base64.decodeBase64(base64);
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		IoSession session = null;
		try {
			ObjectInputStream ois = new ObjectInputStream(bais);
			Object obj = ois.readObject();
			session = (IoSession) obj;
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return session;
	}
}
