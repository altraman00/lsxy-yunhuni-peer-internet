package com.hesyun.app.channel.utils.rpc.client;

import org.apache.mina.core.session.IoSession;



/**
 * 渠道管理器连接成功后的回调
 * 执行CH_MN_CONNECT命令成功后的回调
 * @author tandy
 *
 */
public interface ChannelClientBindCallback {
	public void doCallback(IoSession session) throws Exception;
}
