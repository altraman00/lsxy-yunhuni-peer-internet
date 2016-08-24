package com.lsxy.framework.rpc.mina.codec;

import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.RPCResponse;
import org.apache.mina.filter.codec.demux.DemuxingProtocolCodecFactory;

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
