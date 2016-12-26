package com.lsxy.framework.rpc.netty.demo004;

/**
 * Created by tandy on 16/8/1.
 */
//public class HelloClient {
//    private static final Logger logger = LoggerFactory.getLogger(HelloClient.class);
//    public static void main(String[] args) throws InterruptedException, IOException {
//        EventLoopGroup group = new NioEventLoopGroup();
//        try {
//            Bootstrap b = new Bootstrap();
//            b.group(group)
//                    .channel(NioSocketChannel.class)
//                    .handler(new ChannelInitializer<Channel>() {
//                        @Override
//                        protected void initChannel(Channel ch) throws Exception {
//                            ChannelPipeline pipeline = ch.pipeline();
//
////                            pipeline.addLast("frameDecoder", new DelimiterBasedFrameDecoder(Integer.MAX_VALUE, Delimiters.lineDelimiter()));
//                            pipeline.addLast("decoder", new RPCMessageDecoder());
//                            pipeline.addLast("encoder", new RPCMessageEncoder());
//                            pipeline.addLast("handler", new HelloClientHandler());
//                        }
//                    });
//
//            // 连接服务端
//            Channel ch = b.connect("localhost", 8888).sync().channel();
//
//            long beginTime = System.currentTimeMillis();
////            sendResponse(ch);
//            sendRequest(ch);
//            System.out.println("发送结束,总花费"+(System.currentTimeMillis() - beginTime)+"ms");
//
//            TimeUnit.SECONDS.sleep(5);
//        } finally {
//            // The connection is closed automatically on shutdown.
//            group.shutdownGracefully();
//        }
//    }
//
//    private static void sendRequest(Channel ch) throws InterruptedException {
//        int i = 0;
//        while(i ++ < 1) {
//            RPCRequest request = new RPCRequest();
//            request.setSessionid(UUIDGenerator.uuid());
//            request.setName(ServiceConstants.CH_MN_CONNECT);
//            request.setTimestamp(new Date().getTime());
//            request.setParam("clientId=client001");
//            request.setBody("我是岁啊啊死了都快放假啦是江东父老叫阿死了都快解放啦开始觉得浪费空间阿拉山口的肌肤立刻就啊三闾大夫就卡拉斯科江东父老叫阿死了都快解放啦开始觉得浪费空间阿里斯顿健康福利卡绝世独立飞机卡拉斯科记得放辣椒善良的看法加拉加斯的立法局卡拉斯科地方");
////            request.setBody("1");
//            if (logger.isDebugEnabled()) {
//                logger.debug("SESSIONID={}",request.getSessionid());
//                logger.debug("我的body 是:{}", request.getBody());
//                logger.debug("长度是:{}", request.getBody().length());
//                logger.debug("我的时间戳是:" + request.getTimestamp());
//            }
//
//            long readyToSendTime = System.currentTimeMillis();
//            ch.writeAndFlush(request).sync();
//
//            System.out.println("写出去了 花了 "+(System.currentTimeMillis() - readyToSendTime)+" ms:");
//
////                TimeUnit.MILLISECONDS.sleep(1);
//        }
//    }
//
//    private static void sendResponse(Channel ch) throws InterruptedException {
//        RPCRequest request = new RPCRequest();
//        request.setSessionid(UUIDGenerator.uuid());
//        request.setName(ServiceConstants.CH_MN_CONNECT);
//        request.setTimestamp(new Date().getTime());
//        request.setParam("clientId=client001");
//        request.setBody("我是岁啊啊死了都快放假啦是江东父老叫阿死了都快解放啦开始觉得浪费空间阿拉山口的肌肤立刻就啊三闾大夫就卡拉斯科江东父老叫阿死了都快解放啦开始觉得浪费空间阿里斯顿健康福利卡绝世独立飞机卡拉斯科记得放辣椒善良的看法加拉加斯的立法局卡拉斯科地方");
//
//        int i = 0;
//        while(i ++ < 100000) {
//
//            RPCResponse response = RPCResponse.buildResponse(request);
//            response.setMessage("你要是了");
//            response.setBody("我是岁啊啊死了都快放假啦是江东父老叫阿死了都快解放啦开始觉得浪费空间阿拉山口的肌肤立刻就啊三闾大夫就卡拉斯科江东父老叫阿死了都快解放啦开始觉得浪费空间阿里斯顿健康福利卡绝世独立飞机卡拉斯科记得放辣椒善良的看法加拉加斯的立法局卡拉斯科地方");
//            if (logger.isDebugEnabled()) {
//                logger.debug("response sessionid is {}",response.getSessionid());
//                logger.debug("response body is {}", response.getBody());
//                logger.debug("body length is {}", response.getBody().length());
//                logger.debug("response timestamp is {}", response.getTimestamp());
//            }
//
//            long readyToSendTime = System.currentTimeMillis();
//            ch.writeAndFlush(response);
//
//            System.out.println("写出去了 花了 "+(System.currentTimeMillis() - readyToSendTime)+" ms:");
//
////                TimeUnit.MILLISECONDS.sleep(100);
//        }
//    }
//}
