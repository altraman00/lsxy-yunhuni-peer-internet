package com.lsxy.framework.rpc.api.client;

import com.lsxy.framework.rpc.api.session.Session;



/**
 * 渠道管理器连接成功后的回调
 * 执行CH_MN_CONNECT命令成功后的回调
 * @author tandy
 *
 */
public interface ClientBindCallback {
	public void doCallback(Session session) throws Exception;
}
