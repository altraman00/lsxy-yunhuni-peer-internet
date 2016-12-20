package com.lsxy.app.opensips;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsxy.call.center.api.service.AppExtensionService;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by tandy on 16/12/1.
 */
@Component
public class OpenSipEventHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    @Reference(timeout=3000,check = false,lazy = true)
    private AppExtensionService appExtensionService;

    private static final Logger logger = LoggerFactory.getLogger(OpenSipEventHandler.class);
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) throws Exception {
        // 读取收到的数据
        ByteBuf buf = (ByteBuf) packet.copy().content();
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body = new String(req, CharsetUtil.UTF_8);
        if(logger.isDebugEnabled()){
            logger.debug("【NOTE】>>>>>> 收到客户端的数据："+body);
        }
        if(StringUtils.isNotBlank(body)){
            if(body.contains("E_UL_AOR_INSERT")){
                //登录
                String[] split = body.split("::");
                if(split.length > 0){
                    String user = split[1].trim();
                    if(logger.isDebugEnabled()){
                        logger.debug("登录的用户："+user);
                    }
                    appExtensionService.login(user);
                }
            }else if(body.contains("E_UL_AOR_DELETE")){
                //注销
                String[] split = body.split("::");
                if(split.length > 0){
                    String user = split[1].trim();
                    if(logger.isDebugEnabled()){
                        logger.debug("注销的用户："+user);
                    }
                    appExtensionService.logout(user);
                }
            }
        }

        // 回复一条信息给客户端
        ctx.writeAndFlush(new DatagramPacket(
                Unpooled.copiedBuffer("Hello，我是Server，我的时间戳是"+System.currentTimeMillis()
                        , CharsetUtil.UTF_8)
                , packet.sender())).sync();
    }

}
