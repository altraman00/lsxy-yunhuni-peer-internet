package com.lsxy.framework.rpc.mina.codec;

/**
 * 请求对象编码器
 * RQ:请求服务名称
 * PM:请求参数
 * BL:body length
 * BC:body array
 * @author tantyuo
 *
 */
//public class RPCRequestEncoder implements MessageEncoder<RPCRequest> {
//
//	@Override
//	public void encode(IoSession session, RPCRequest request,
//			ProtocolEncoderOutput out) throws Exception {
//
//		String rq = "RQ:"+request.getName()+"\r\n";
//		String pm = "PM:"+request.getParam()+"\r\n";
//
//		byte[] bc = request.getBody();
//		int bl = (bc == null?0:bc.length);
//		IoBuffer buffer = IoBuffer.allocate(1024);
//		buffer.setAutoExpand(true);
//		buffer.put(request.getSessionid().getBytes());
//		buffer.put(rq.getBytes("UTF-8"));
//		buffer.put(pm.getBytes("UTF-8"));
//		buffer.putInt(bl);
//		if(bc!=null)
//			buffer.put(bc);
//		buffer.flip();
//		out.write(buffer);
//		out.flush();
//	}
//}
