package com.lsxy.framework.rpc.mina.codec;

///**
// * 响应对象解码器
// * @author tantyuo
// *
// */
//public class RPCResponseDecoder implements MessageDecoder {
//
//	private int count = 0;
//
//	@Override
//	public MessageDecoderResult decodable(IoSession session, IoBuffer in) {
//		MessageDecoderResult result = null;
//		if(in.remaining()<(32+2)){
//			result = MessageDecoderResult.NEED_DATA;
//		}else{
//			byte[] sessionid = new byte[32];
//			in.get(sessionid);
//			byte[] header = new byte[2];
//			in.get(header);
//			String sHeader = new String(header);
//			if(!sHeader.equals("RP"))
//				return MessageDecoderResult.NOT_OK;
//
//			while(in.remaining()>2){
//				byte b1 = in.get();
//				byte b2 = in.get();
//				//如果有两个回车换行，就判断 BL
//				if(b1==13 && b2==10){
//					count ++;
//					break;
//				}
//				in.position(in.position()-1);
//			}
//
//			if(count<1){
//				result = MessageDecoderResult.NEED_DATA;
//			}else{
//				count = 0;
//				int r = in.remaining();
//				if(r<4){
//					result = MessageDecoderResult.NEED_DATA;
//				}else{
//					int bl = in.getInt();
//					int r2 = in.remaining();
//
//					if(r2<bl){
//						in.capacity(in.capacity()+bl);
//						result = MessageDecoderResult.NEED_DATA;
//					}else{
//						result = MessageDecoderResult.OK;
//					}
//				}
//			}
//		}
//		return result;
//	}
//
//	@Override
//	public MessageDecoderResult decode(IoSession session, IoBuffer in,
//			ProtocolDecoderOutput out) throws Exception {
//		RPCResponse response = new RPCResponse();
//		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		byte[] sessionid = new byte[32];
//		in.get(sessionid);
//		response.setSessionid(new String(sessionid));
//		while(in.remaining()>2){
//			byte b1 = in.get();
//			byte b2 = in.get();
//
//			//如果有两个回车换行，就判断 BL
//			if(b1==13 && b2==10){
//				break;
//			}
//
//			baos.write(b1);
//			in.position(in.position()-1);
//		}
//
//		String header = baos.toString("UTF-8");
//		String headerArray[] = header.split("\r\n");
//		response.setMessage(headerArray[0].substring(3));
//		baos.close();
//		int bl = in.getInt();
//		if(bl>0){
//			byte body[] = new byte[bl];
//			in.get(body);
//			response.setBody(body);
//		}
//		out.write(response);
//		return MessageDecoderResult.OK;
//	}
//
//	@Override
//	public void finishDecode(IoSession session, ProtocolDecoderOutput out)
//			throws Exception {
//		// TODO Auto-generated method stub
//
//	}
//
//	public static void main(String[] args) {
//	}
//
//}
