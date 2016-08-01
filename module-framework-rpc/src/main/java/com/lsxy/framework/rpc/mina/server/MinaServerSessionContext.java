package com.lsxy.framework.rpc.mina.server;

import com.lsxy.framework.rpc.api.server.Session;
import com.lsxy.framework.rpc.mina.MinaCondition;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * IOSession环境变量
 * @author tandy
 *
 */
@Component
@Conditional(MinaCondition.class)
public class MinaServerSessionContext {
	public static final Log logger = LogFactory.getLog(MinaServerSessionContext.class);
	//<clientid,session>
	private static Map<String,Session> sessionMap = new HashMap<>();
//	private static Map<Long,String> sessionClientIdMap = new HashMap<Long,String>();//用户存储sessionid与clientId间的映射关系
	
	public void putSession(Session session){
		sessionMap.put(session.getId(), session);
//		sessionClientIdMap.put(session.getId(), nodeAgentId);
//		String base = toBase64Text(session);
//		logger.debug("********** Session id: " + session.getId());
//		logger.debug("********** SessionBase64: " + base);
//		Session baseSession = buildFromBase64(base);
//		logger.debug("********** Base session id: " + baseSession.getId());
	}
	
	public void removeSession(String iosessionHashCode){
//		logger.debug("remove socket session:"+nodeAgentId);
//		Session session = sessionMap.get(iosessionHashCode);
//		sessionClientIdMap.remove(session.getId());
		sessionMap.remove(iosessionHashCode);
		
	}
//	public void removeSession(Session session){
////		= sessionClientIdMap.get(session.getId());
//		removeSession();
//	}

	public Session getSession(Integer iosessionHashCode){
		Session session = sessionMap.get(iosessionHashCode);
		return session;
	}
	
//	/**
//	 * 将对象序列化成base64
//	 * @return
//	 */
//	public String toBase64Text(Session session){
//		String result = null;
//		ByteArrayOutputStream bao = new ByteArrayOutputStream();
//		try {
//			ObjectOutputStream oos = new ObjectOutputStream(bao);
//			oos.writeObject(session);
//			byte[] bytes = bao.toByteArray();
//			result = Base64.encodeBase64String(bytes);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return result;
//	}
	
//	/**
//	 * 对象从base64反向构建
//	 * @param base64
//	 * @return
//	 */
//	public static Session buildFromBase64(String base64){
//		byte[] bytes = Base64.decodeBase64(base64);
//		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
//		Session session = null;
//		try {
//			ObjectInputStream ois = new ObjectInputStream(bais);
//			Object obj = ois.readObject();
//			session = (Session) obj;
//		} catch (IOException | ClassNotFoundException e) {
//			e.printStackTrace();
//		}
//		return session;
//	}
}
