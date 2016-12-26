package com.lsxy.area.agent.cti;

import com.lsxy.framework.cache.manager.RedisCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by tandy on 16/8/5.
 * CTI CLIENT 配置
 * 需要定义多少个客户端实例
 * 每个实例如何配置等等配置信息
 */
@Component
public class CTIClientConfigFactory {


    private int clientId= 0;

    @Autowired
    private RedisCacheService cacheService;

    class CTIClientConfig {
        String ctiHost;
        byte clientId;

        public CTIClientConfig(byte clientId, String ctiHost) {
            this.ctiHost = ctiHost;
            this.clientId = clientId;
        }
    }

//    /**
//     * 初始化配置
//     * 到REDIS获取配置信息,初始化配置对象集合
//     */
//    @PostConstruct
//    public void initConfig(){
//        Set<String> servers = ctiConfigService.ctiServers();
//
//        servers.forEach((serverIp)->{
//            newConfig(serverIp);
//        });
//    }

//    /**
//     * 发现新的配置
//     * @param serverIp
//     * @return
//     */
//    public CTIClientConfig newConfig(String serverIp){
//        CTIClientConfig config = new CTIClientConfig((byte)((clientId)), serverIp);
//        //客户端clientId采用偶数ID  基数为CTI Monitor
//        clientId = clientId + 2;
//        this.configs.registerCommander(config);
//        return config;
//    }



}
