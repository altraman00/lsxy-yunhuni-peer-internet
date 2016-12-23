package com.lsxy.framework.rpc.netty.codec;

/**
 * Created by tandy on 16/8/2.
 * RPC消息编码器
 */
//public class RPCMessageEncoder extends MessageToByteEncoder<RPCMessage>{
//    private static final Logger logger = LoggerFactory.getLogger(RPCMessageEncoder.class);
//    @Override
//    protected void encode(ChannelHandlerContext ctx, RPCMessage msg, ByteBuf out) throws Exception {
//        if(msg instanceof RPCRequest){
//            //request
//            RPCRequest request = (RPCRequest) msg;
//            out.writeBytes(request.getSessionid().getBytes(CharsetUtil.UTF_8));
//            out.writeLong(request.getTimestamp());
//            out.writeBytes(("RQ:" + request.getName() + "\r\n").getBytes(CharsetUtil.UTF_8));
//            out.writeBytes(("PM:" + request.getParam() + "\r\n").getBytes(CharsetUtil.UTF_8));
//            writeBody(msg, out);
//        }else{
//            //response
//            RPCResponse response = (RPCResponse) msg;
//            out.writeBytes(response.getSessionid().getBytes(CharsetUtil.UTF_8));
//            out.writeLong(response.getTimestamp());
//            out.writeBytes(("RP:"+response.getMessage()+"\r\n").getBytes(CharsetUtil.UTF_8));
//            writeBody(msg, out);
//
//        }
//    }
//
//    /**
//     * 编码body部分
//     * @param msg
//     * @param out
//     */
//    private void writeBody(RPCMessage msg, ByteBuf out) {
//        byte[] bc = msg.getBody();
//        int bl = (bc == null?0:bc.length);
//        out.writeInt(bl);
//        if(bc != null){
//            if(logger.isDebugEnabled()){
//
//            }
//            out.writeBytes(bc);
//        }
//    }
//}
