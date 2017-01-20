package com.lsxy.area.agent;

import com.lsxy.framework.rpc.api.client.Client;
import com.lsxy.framework.rpc.exceptions.ClientBindException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;

/**
 * Created by tandy on 16/7/30.
 * 区域客户端
 */
@Component
public class AreaClient {

    private static final Logger logger = LoggerFactory.getLogger(AreaClient.class);

    @Value("${area.agent.server.url}")
    private String serverUrl;

    //通过环境变量配置该参数,不支持".",只支持"_"
    @Value("${area_agent_areaid:area001}")
    private String areaid;

    //由于需要通过设置环境变量设置该值,需要适用下划线,不支持"."
    @Value("${area_agent_nodeid:1}")
    private String nodeid;

    @Autowired
    private Client client;

    private long startDateTime;

    private String agentVersion;

    public AreaClient(){
        startDateTime = new Date().getTime();
        agentVersion = this.getClass().getPackage().getImplementationVersion();
    }


    @PostConstruct
    public void start() throws ClientBindException {
        logger.info("客户端启动:  {}  {}-{} 版本号：{}" , serverUrl, areaid,nodeid,agentVersion);

        client.setServerUrl(serverUrl);
        client.setClientId(areaid,nodeid);
        client.bind();
    }
}
