package com.lsxy.area.agent.cti;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by tandy on 16/8/5.
 * CTI CLIENT 配置
 * 需要定义多少个客户端实例
 * 每个实例如何配置等等配置信息
 */
@Component
@Profile(value={"production","development"})
public class CTIClientConfigFactory {

    private Set<CTIClientConfig> configs = new HashSet<>();

    class CTIClientConfig {
        String ctiHost;
        byte clientId;

        public CTIClientConfig(byte clientId, String ctiHost) {
            this.ctiHost = ctiHost;
            this.clientId = clientId;
        }
    }

    /**
     * 初始化配置
     * 到REDIS获取配置信息,初始化配置对象集合
     */
    @PostConstruct
    public void initConfig(){
        //TODO 去REDIS获取配置信息来初始化多个客户端配置
        CTIClientConfig config = new CTIClientConfig((byte)0,"192.168.2.100");
        this.configs.add(config);
    }

    public Set<CTIClientConfig> getConfigs() {
        return configs;
    }
}
