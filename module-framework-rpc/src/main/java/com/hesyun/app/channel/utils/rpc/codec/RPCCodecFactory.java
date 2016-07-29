package com.hesyun.app.channel.utils.rpc.codec;

import org.apache.mina.filter.codec.demux.DemuxingProtocolCodecFactory;

import com.hesyun.app.channel.utils.rpc.core.RPCRequest;
import com.hesyun.app.channel.utils.rpc.core.RPCResponse;

/**
 * 
 * 编解码器创建工厂类，用于注册请求、相应对象的编解码工作
 * @author tantyuo
 *
 */
public class RPCCodecFactory extends DemuxingProtocolCodecFactory  {

	public RPCCodecFactory(){
			super.addMessageEncoder(RPCResponse.class,RPCResponseEncoder.class);
			super.addMessageEncoder(RPCRequest.class, RPCRequestEncoder.class);
			super.addMessageDecoder(RPCResponseDecoder.class);
			super.addMessageDecoder(RPCRequestDecoder.class);
	}
}
