package com.lsxy.framework.rpc.netty.demo004;

/**
 * Created by tandy on 16/8/1.
 */
//public class HelloServer {
//    /**
//     * 服务端监听的端口地址
//     */
//    private static final int portNumber = 8888;
//
//    public static void main(String[] args) throws InterruptedException {
//        EventLoopGroup bossGroup = new NioEventLoopGroup();
//        EventLoopGroup workerGroup = new NioEventLoopGroup();
//        try {
//            ServerBootstrap b = new ServerBootstrap();
//            b.group(bossGroup, workerGroup);
//            b.channel(NioServerSocketChannel.class);
//            b.childHandler(new HelloServerInitializer());
//
//            // 服务器绑定端口监听
//            ChannelFuture f = b.bind(portNumber).sync();
//            // 监听服务器关闭监听
//            f.channel().closeFuture().sync();
//
//            // 可以简写为
//            /* b.bind(portNumber).sync().channel().closeFuture().sync(); */
//        } finally {
//            bossGroup.shutdownGracefully();
//            workerGroup.shutdownGracefully();
//        }
//    }
//}
