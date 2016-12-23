package com.lsxy.framework.rpc.mina.codec;

///**
// * 请求对象解码器，循环查询两个\r\n之后，读取BL和BC之后，方能确定是一个Request对象
// * @author tantyuo
// *
// */
//public class RPCRequestDecoder implements MessageDecoder {
//	private int count = 0;//记录回车换行的计数器，每次确定一个请求对象之后，该计数器清零
//
////	private Log logger = LogFactory.getLog(RPCRequestDecoder.class);
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
//			if(!sHeader.equals("RQ"))
//				return MessageDecoderResult.NOT_OK;
//
//			while(in.remaining()>2){
//				byte b1 = in.get();
//				byte b2 = in.get();
////				logger.info("字符："+(char)b1+":"+b1);
////				logger.info("字符："+(char)b2+":"+b2);
//				//如果有两个回车换行，就判断 BL
//				if(b1==13 && b2==10){
//					count ++;
//					if(count==2){
//						break;
//					}
//				}
//
//				in.position(in.position()-1);
//			}
////			logger.info("计算count:"+count);
//
//			if(count<2){
////				logger.info("缺少数据1");
//				result = MessageDecoderResult.NEED_DATA;
//			}else{
//				int r = in.remaining();
//				if(r<4){
////					logger.info("缺少数据2");
//					result = MessageDecoderResult.NEED_DATA;
//				}else{
////					logger.info("开始获取BODY");
//					int bl = in.getInt();
////					logger.info("BODY LENGTH:"+bl);
//					int r2 = in.remaining();
//					if(r2<bl){
//						in.capacity(in.capacity()+bl);
//						result = MessageDecoderResult.NEED_DATA;
//					}else{
//						result = MessageDecoderResult.OK;
//					}
//				}
//			}
//			count = 0;
//		}
//		return result;
//	}
//
//	@Override
//	public MessageDecoderResult decode(IoSession session, IoBuffer in,
//			ProtocolDecoderOutput out) throws Exception {
//		RPCRequest request = new RPCRequest();
//		byte[] sessionid = new byte[32];
//		in.get(sessionid);
//		request.setSessionid(new String(sessionid));
//
//		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		int j = 0;
//		while(in.remaining()>2 && j<2){
//			byte b1 = in.get();
//			byte b2 = in.get();
//			baos.write(b1);
//			//如果有两个回车换行，就判断 BL
//			if(b1==13 && b2==10){
//				j++;
//			}
//			if(j<2)
//				in.position(in.position()-1);
//		}
//		String header = baos.toString("UTF-8").trim();
//		String headerArray[] = header.split("\r\n");
//		request.setName(headerArray[0].substring(3));
//		request.setParam(headerArray[1].substring(3));
//		baos.close();
//		int bl = in.getInt();
//		if(bl>0){
//			byte body[] = new byte[bl];
//			in.get(body);
//			request.setBody(body);
//		}
//		out.write(request);
//		return MessageDecoderResult.OK;
//	}
//
//	@Override
//	public void finishDecode(IoSession session, ProtocolDecoderOutput out)
//			throws Exception {
//		// TODO Auto-generated method stub
//
//	}
//}
