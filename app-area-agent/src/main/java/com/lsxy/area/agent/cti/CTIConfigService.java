package com.lsxy.area.agent.cti;

import com.lsxy.framework.cache.manager.RedisCacheService;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by tandy on 16/12/21.
 */
@Component
public class CTIConfigService {

    public static final String KEY_CTI_CLUSTER = "hesong:ipsc:cluster:nodeid";
    private RedisCacheService cacheService;


    /**
     * 查询redis获取服务器列表
     * @return
     */
    public Set<String> ctiServers(){
        HashSet serverList = new HashSet();
        Set<String> ipscNodes = cacheService.smembers(KEY_CTI_CLUSTER);
        ipscNodes.forEach((snode)->{
            Object obj = cacheService.hget(KEY_CTI_CLUSTER + ":"+snode,"ip");
            String sServer = obj.toString();
            serverList.add(sServer.substring(0,sServer.indexOf("-")));
        });
        serverList.forEach((server)->{
            System.out.println(server);
        });
        return serverList;
    }
}
