package com.lsxy.framework.rpc.netty.codec;

//
///**
// * Created by tandy on 16/8/2.
// * 调用方主要包括5部分
// * sessionid
// * timestamp    请求对象的时间戳 8位长整型
// * RQ:ServiceName\r\n
// * PM:p1=??&p2=??\r\n
// * 4B
// * byte array
// * SESSIONID:sessionid为32位uuid,主要用来唯一标识一次请求和相应的匹配关系。
// * RQ: Request的简写，指定请求服务的名称，以回车换行结束
// * PM：指定请求参数，采用http url的请求参数的方案，以回车换行结束
// * BL：Body Length的简写，指定相应Body的数组长度，整型4个字节
// * BC：Body Content的简写，相应内容，主要针对复杂数据进行相应，比如xml，文件等。
// *
// *
// * 响应方主要包括4部分
// * Sessionid
// * timestamp  请求对象的时间戳  8位长整型
// * RP:message\r\n
// * 4B              4位整型
// * byte array
// * SESSIONID:sessionid为32位uuid,主要用来唯一标识一次请求和相应的匹配关系。
// * RP: Response的简写，返回值，回车换行结束
// * BL:Body Length的简写，指定相应Body的数组长度，整型4个字节
// * BC:Body Content的简写，相应内容，主要针对复杂数据进行相应，比如xml，文件等。
// *
// */
//public class RPCMessageDecoder extends ByteToMessageDecoder{
//    private static final Logger logger = LoggerFactory.getLogger(RPCMessageDecoder.class);
//    private static final int PART_SESSIONID=0;
//    private static final int PART_HEADER = 1;
//    private static final int PART_BODY=2;
//    private int part=PART_SESSIONID;  //解析部分 HEADER 部分
//    private RPCMessage message = null;
//    private ByteBuf headerByteBuffer;
//    private Integer bodyLength = null;
//
//    ByteBuf sessionPartByteBuffer = null;
//    //开始解析时间,用来记录解析花费
//    private long startDecodeTime = 0;
//
//    //HEADER部分回车换行数量
//    Integer crlfNumber = null;
//    //Header部分解析回车换号的次数
//
//    public static void main(String[] args) {
////        ByteBuf byteBuf= Unpooled.buffer(1);
////        byteBuf.writeLong(System.currentTimeMillis());
////        System.out.println(byteBuf.readableBytes());
//        System.out.println("我是岁啊啊死了都快放假啦是江东父老叫阿死了都快解放啦开始觉得浪费空间阿拉山口的肌肤立刻就啊三闾大夫就卡拉斯科江东父老叫阿死了都快解放啦开始觉得浪费空间阿里斯顿健康福利卡绝世独立飞机卡拉斯科记得放辣椒善良的看法加拉加斯的立法局卡拉斯科地方".getBytes().length);
//    }
//
//    @Override
//    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
//            switch (part){
//                case PART_SESSIONID:{
//                    startDecodeTime = System.currentTimeMillis();
//                    try {
//                        decodeSessionIdPart(in);
//                    }catch (Exception ex){
//                        logger.error("解析失败,关闭连接:{}",ctx.channel());
//                        ctx.close();
//                        throw ex;
//                    }
//                    break;
//                }
//                case PART_HEADER: {
//                    decodeHeaderPart(in);
//                    break;
//                }
//                case PART_BODY: {
//                    decodeBodyPart(in, out);
//                    break;
//                }
//                default :
//                    break;
//        }
//    }
//
//    /**
//     * 解析BODY部分
//     * @param in
//     * @param out
//     */
//    private void decodeBodyPart(ByteBuf in, List<Object> out) {
//        if(logger.isDebugEnabled()){
//            logger.debug("start to decode body part");
//        }
//        //至少有个整型
//        if(bodyLength == null && in.readableBytes() < 4){
//            //NEED MORE DATA
//            return;
//        }
//
//        if(bodyLength == null) {
//            bodyLength = in.readInt();
//        }
//
//        if(logger.isDebugEnabled()){
//            logger.debug("body Length is : {}" , bodyLength);
//        }
//
//        if(in.readableBytes() < bodyLength ){
//            //NEED MORE DATA
//            if(logger.isDebugEnabled()){
//                logger.debug("数据缓存还不够,再缓存一下");
//            }
//            return;
//        }else{
//            byte[] body = new byte[bodyLength];
//            in.readBytes(body);
//            if(logger.isDebugEnabled()){
//                logger.debug("数据缓存够了{}",bodyLength);
//                logger.debug("读到的数据是:{}",body);
//            }
//            message.setBody(body);
//            out.add(message);
//
//            if(logger.isDebugEnabled()){
//                logger.debug("========>解析{}花费:{}ms",message.getSessionid(),(System.currentTimeMillis() - startDecodeTime));
//            }
//
//            logger.info("收到RPC消息[{}] 整体耗时:{} ms ,解析消息耗时:{}ms 消息体：{} ",message.getSessionid(),System.currentTimeMillis() - message.getTimestamp(),System.currentTimeMillis()-startDecodeTime,message);
//
//            message = null;
//            this.bodyLength = null;
//
//            part = PART_SESSIONID;
//
//        }
//    }
//
//    /**
//     * 解析头部部分
//     * 包括RP PM RQ
//     * 如果是请求对象 解析RQ 和 PM 两部分  需要解析两个回车换行
//     * 如果是响应对象,只需要解析RP部分 RP 只需要解析1个回车换行
//     * @param in
//     */
//    private void decodeHeaderPart(ByteBuf in) {
//        if(logger.isDebugEnabled()){
//            logger.debug("start to decode header part");
//        }
//        //如果是请求对象,则需要解析2个\r\n 否者只需要解析一个\r\n
//        if(headerByteBuffer == null)
//            headerByteBuffer = Unpooled.buffer(500,2048);
//        //遍历可读区域 找到指定次数的回车换行
//        if(logger.isDebugEnabled()){
//            logger.debug("需要解析{}个回车换行",crlfNumber);
//        }
//
//        while(in != null && in.readableBytes() > 0 && crlfNumber>0){
//            byte xx = in.readByte();
//            headerByteBuffer.writeByte(xx);
//
//            //判断是否是\r\n结尾进来了 至少已经读进来了2个以上的字节并判断结尾的两个字符是否是回车换行
//            if(headerByteBuffer.readableBytes()>=2){
//                int end01 = headerByteBuffer.getByte(headerByteBuffer.readableBytes() -2) ;
//                int end02 = headerByteBuffer.getByte(headerByteBuffer.readableBytes() -1);
//
//                if(end01 == 13 && end02 == 10){
//                    if(logger.isDebugEnabled()){
//                        logger.debug("found one pair CRLF,crlfNumber reduce one");
//                    }
//                    crlfNumber -- ;
//                }
//
//            };
//        }
//        if(logger.isDebugEnabled()){
//            logger.debug("crlf number is {}" , crlfNumber);
//        }
//        if(crlfNumber == 0){
////            if(logger.isDebugEnabled()){
////                logger.debug("the head buffer is : {}",headerByteBuffer.toString(CharsetUtil.UTF_8));
////            }
//            //指定回车换行次数归零,表示已经找到了满足条件的数据字节可以解析headerbuffer了
//            //去掉尾部的回车换行符号
//            String header = new String(headerByteBuffer.toString(0,headerByteBuffer.readableBytes() - 2, CharsetUtil.UTF_8));
//
//            if(message instanceof RPCRequest){
//                String headPices[]  = header.split("\r\n");
//                RPCRequest request = (RPCRequest) message;
//                request.setName(headPices[0]);
//                //从第3位开始,去掉PM:
//                request.setParam(headPices[1].substring(3));
//            }else{
//                RPCResponse response = (RPCResponse) message;
//                response.setMessage(header);
//            }
//            headerByteBuffer.clear();
//            crlfNumber = null;
//            if(logger.isDebugEnabled()){
//                logger.debug("set crlfNumber null");
//            }
//            part = PART_BODY;
//        }else{
//            //NEED DATA AND RETURN
//            return;
//        }
//    }
//
//    /**
//     * 解析SESSIONID部分
//     * SESSIONID 部分包括 SESSIONID TIMESTAMP + METHOD
//     * METHOD = RQ:  |  RP:  分别代表请求或响应
//     * @param in
//     */
//    private void decodeSessionIdPart(ByteBuf in) {
//        if(logger.isDebugEnabled()){
//            logger.debug("start decode session id part");
//        }
//        //解析SESSIONID部分  需要满足43位的条件,否则不合规则
//        if(in.readableBytes() < 32 + 8 +3){
//            if(logger.isDebugEnabled()){
//                logger.debug("not en 43位字节,需要更多数据  vs {}" ,in.readableBytes());
//            }
//            return;
//        }else{
//            if(sessionPartByteBuffer == null) {
//                sessionPartByteBuffer = Unpooled.buffer(43,43);
//            }
//            in.readBytes(sessionPartByteBuffer,43);
//            String sessionId = sessionPartByteBuffer.readBytes(32).toString(CharsetUtil.UTF_8);
//            long timestamp = sessionPartByteBuffer.readLong();
//
//
//
//            String rpcMethod = sessionPartByteBuffer.readBytes(3).toString(CharsetUtil.UTF_8);
//            sessionPartByteBuffer.clear();
//
//            if(logger.isDebugEnabled()){
//                logger.debug("timestamp is :" + timestamp);
//                logger.debug("decode message 's method is [{}]",rpcMethod);
//            }
//
//
//            //看是request 还是 response
//            if(rpcMethod.equals("RQ:")){
//                message = new RPCRequest();
//                // request 对象的\r\n 有两个
//                this.crlfNumber = 2;
//            }else if(rpcMethod.equals("RP:")){
//                message = new RPCResponse();
//                //response对象的\r\n 只有一个
//                this.crlfNumber = 1;
//            }else{
//                if(logger.isDebugEnabled()){
//                    logger.debug("解析消息体失败,既不是RQ 也不是 RP");
//                }
//                throw new DecoderException("解析消息体失败,既不是RQ 也不是 RP");
//            }
//            message.setSessionid(sessionId);
//            message.setTimestamp(timestamp);
//            part = PART_HEADER;
//        }
//    }
//
//}
