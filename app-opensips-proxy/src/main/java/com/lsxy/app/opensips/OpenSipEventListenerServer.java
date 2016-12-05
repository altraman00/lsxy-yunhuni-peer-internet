package com.lsxy.app.opensips;

import com.lsxy.framework.config.SystemConfig;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by tandy on 16/12/1.
 */
@Component
public class OpenSipEventListenerServer {
    private static final Logger logger = LoggerFactory.getLogger(OpenSipEventListenerServer.class);

    @Autowired
    OpenSipEventHandler openSipEventHandler;

    @PostConstruct
    public void startListener() throws InterruptedException {
        if(logger.isDebugEnabled()){
            logger.debug("启动事件监听...");
        }

        new Thread(() -> {
            Bootstrap b = new Bootstrap();
            EventLoopGroup group = new NioEventLoopGroup();
            b.group(group)
                    .channel(NioDatagramChannel.class)
                    .handler(openSipEventHandler);

            // 服务端监听在9999端口
            try {
                String port = SystemConfig.getProperty("app.cc.opensips.event.listener.port","9009");
                b.bind(Integer.parseInt(port)).sync().channel().closeFuture().await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();


    }
}
