package com.lsxy.app.opensips;

import com.lsxy.framework.config.SystemConfig;
import com.lsxy.framework.core.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

/**
 * Created by tandy on 16/12/2.
 */
@Component
@EnableScheduling
public class OpenSipEventSubscriberTask {
    private static final Logger logger = LoggerFactory.getLogger(OpenSipEventSubscriberTask.class);
    private DatagramSocket socket;
    private InetSocketAddress targetHost;

    //分机登录事件
    private static final String EVENT_TOPIC_EXT_LOGIN=":event_subscribe:\n" +
            "E_UL_AOR_INSERT\n" +
            "udp:%s:%s\n" +
            "3600";

    //分机注销事件
    private static final String EVENT_TOPIC_EXT_LOGOUT=":event_subscribe:\n" +
            "E_UL_AOR_DELETE\n" +
            "udp:%s:%s\n" +
            "3600";

    public OpenSipEventSubscriberTask(){
        try {
            logger.info("初始化OpensipsEventSubscriberTask");
            String opensipsHost = SystemConfig.getProperty("app.cc.opensips.event.subscribe.host","127.0.0.1");
            int opensipsPort = Integer.parseInt(SystemConfig.getProperty("app.cc.opensips.event.subscribe.port","3333"));
            targetHost = new InetSocketAddress(opensipsHost,opensipsPort);
            socket = new DatagramSocket();
            socket.setSoTimeout(2000);
        } catch (SocketException e) {
            logger.error("初始化OpenSipEventSubscribeTask出现异常",e);
        }
    }

    /**
     *opensips 每30分钟向opensips发送一次注册请求
     * 启动时注册一次
     */
    @Scheduled(fixedDelay=20*60*1000)
    public void doSubscribed(){
        if(socket != null){
            String ip = StringUtil.getHostIp();
            String port = SystemConfig.getProperty("app.cc.opensips.event.listener.port","9009");

            final String data1 = String.format(EVENT_TOPIC_EXT_LOGIN,ip,port);
            final String data2 = String.format(EVENT_TOPIC_EXT_LOGOUT,ip,port);

            try {
                if(logger.isDebugEnabled()){
                    logger.debug("doSubscribed send data :{}",targetHost);
                    logger.debug("doSubscribed send data :{}",data1);
                    logger.debug("doSubscribed send data :{}",data2);
                }

                byte[] bytes1 = data1.getBytes("UTF-8");
                byte[] bytes2 = data2.getBytes("UTF-8");

                socket.send(new DatagramPacket(bytes1, 0, bytes1.length, targetHost));
                socket.send(new DatagramPacket(bytes2, 0, bytes2.length, targetHost));
                byte[] buffer = new byte[1024];

                DatagramPacket dp = new DatagramPacket(buffer,1024);
                if(logger.isDebugEnabled()){
                    logger.debug("receiving return message");
                }
                socket.receive(dp);
                String info = new String(dp.getData(), 0, dp.getLength());
                if(logger.isDebugEnabled()){
                    logger.debug("received message :"+info);
                }

            } catch (IOException e) {
                logger.error("订阅消息发送失败",e);
            }
        }
    }

}
