package com.lsxy.framework.rpc.mina.codec;

import com.lsxy.framework.rpc.api.RPCResponse;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;

/**
 * 响应对象的编码器
 * @author tantyuo
 *
 */
public class RPCResponseEncoder implements MessageEncoder<RPCResponse> {

	@Override
	public void encode(IoSession session, RPCResponse response,
			ProtocolEncoderOutput out) throws Exception {
		String rq = "RP:"+response.getMessage()+"\r\n";
		
		byte[] bc = response.getBody();
		int bl = (bc == null?0:bc.length);
		IoBuffer buffer = IoBuffer.allocate(1024);
		buffer.setAutoExpand(true);
		buffer.put(response.getSessionid().getBytes());
		buffer.put(rq.getBytes("UTF-8"));
		buffer.putInt(bl);
		if(bc!=null)
			buffer.put(bc);
		buffer.flip();
		out.write(buffer);
		out.flush();
	}

}
