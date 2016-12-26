package com.lsxy.framework.rpc.api;

import com.lsxy.framework.rpc.api.session.Session;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;


/**
 * 节点代理客户端消息处理类
 * @author Administrator
 *
 */
public abstract class RPCHandler {


	private static final Logger logger = LoggerFactory.getLogger(RPCHandler.class);
	public static final int CORE_POOL_SIZE = 20;
	public static final int MAXIMUM_POOL_SIZE = 200;
	public static final int QUEUE_CAPACITY = 1024;

	/**
	 * 根据环境获取对应的session对象
	 * @param ctxObject
	 */
	public abstract Session getSessionInTheContextObject(Object ctxObject);


	/**
	 * 线程池
	 *
	 * @param
	 * @return
	 */
	public static ExecutorService rpcHandlerExecutorService(String threadNamePattern) {
		return new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, 0L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(QUEUE_CAPACITY),
				new BasicThreadFactory.Builder().namingPattern(threadNamePattern).build());
	}

}
