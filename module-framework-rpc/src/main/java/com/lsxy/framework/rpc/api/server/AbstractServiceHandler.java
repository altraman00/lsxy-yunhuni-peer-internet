package com.lsxy.framework.rpc.api.server;

import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.RPCResponse;

import com.lsxy.framework.rpc.mina.server.MinaRemoteServer;
import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * 处理请求操作
 * @author tandy
 *
 */
public abstract  class AbstractServiceHandler {

	/**
	 *处理请求
	 * @param request
	 * @param session
	 * @return
	 * @throws Exception
	 */
	public abstract RPCResponse handleService(RPCRequest request, Session session);

	/**
	 * 连接是否有效
	 * @param areaid
	 * @param nodeid
     * @return
     */
	public abstract boolean isConnectAvalid(String areaid, String nodeid);


//
//    /**
//     * 处理连接命令 客户端连接到服务端,第一件事,发送CH_MN_CONNECT命令到服务器进行注册
//     * 如果出现注册失败,就直接结束掉,如果注册成功,则返回成功的响应对象
//     * @param session  RPC SESSION
//     * @param request   解析出来的请求对象
//     * @return
//     *      如果注册成功并且在IP白名单中,则允许连接  否则拒绝连接
//     */
//	public abstract RPCResponse doConnect(String areaid,String nodeid,RPCRequest request,Session session) ;
}
