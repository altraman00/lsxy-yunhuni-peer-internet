package com.lsxy.framework.rpc.api.server;

import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * Created by tandy on 16/7/30.
 * 端口号提供处理器
 */

//@Component
//public class ServerPortProvider {
//    private static final Logger logger = LoggerFactory.getLogger(ServerPortProvider.class);
//    //随机方式
//    public static final String MODE_RANDOM="random";
//    //固定方式
//    public static final String MODE_FIX="fix";
//
//
//    @Value("${global.rpc.server.port.provider}")
//    private String providerMode;
//
//    //随机端口号范围  9000-9999
//    @Value("${global.rpc.server.port.range}")
//    private String portRangeConfig;
//
//    private Integer randomRangeMin;
//    private Integer randomRangeMax;
//
//    @Value("${global.rpc.server.port}")
//    private Integer fixPort;
//
//    public ServerPortProvider(){
//
//    }
//
//    /**
//     * 获取端口号
//     * 根据配置决定是固定提供端口号还是随机端口号
//     * 如果随机端口号,解析端口号的范围配置,在指定范围内生成端口号
//     * @return
//     */
//    public int getServerPort(){
//        if(MODE_FIX.equals(providerMode)){
//            Assert.notNull(fixPort,"固定端口号参数未设置  global.rpc.server.port => 9999");
//            return fixPort;
//        }else{
//            if(randomRangeMin == null || randomRangeMax == null){
//                parsePortRange();
//            }
//            return nextPortInRange();
//        }
//    }
//
//
//    /**
//     * 随机生成指定范围内的端口号
//     * @return
//     */
//    private int nextPortInRange() {
//        return RandomUtils.nextInt(this.randomRangeMin,this.randomRangeMax);
//    }
//
//    private void parsePortRange() {
//        Assert.notNull(portRangeConfig,"随机端口号范围配置项未正确配置  global.rpc.server.port.range ==> 9000-9999");
//        String[] pr = portRangeConfig.split("-");
//        try{
//            this.randomRangeMin = Integer.parseInt(pr[0]);
//            this.randomRangeMax = Integer.parseInt(pr[1]);
//        }catch (Exception e){
//            e.printStackTrace();
//            logger.error("随机端口号范围配置项未正确配置  global.rpc.server.port.range ==> 9000-9999",e);
//        }
//    }
//}
