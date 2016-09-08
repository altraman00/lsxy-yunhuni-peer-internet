package com.lsxy.area.agent;

import com.lsxy.framework.rpc.api.client.Client;
import com.lsxy.framework.rpc.exceptions.ClientBindException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by tandy on 16/7/30.
 * 区域客户端
 */
@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class AreaClient {

    private static final Logger logger = LoggerFactory.getLogger(AreaClient.class);

    @Value("${area.agent.server.url}")
    private String serverUrl;

    @Value("${area.agent.areaid}")
    private String areaid;

    //由于需要通过设置环境变量设置该值,需要适用下划线,不支持"."
    @Value("${area_agent_nodeid:1}")
    private String nodeid;

    @Autowired
    private Client client;

//    @Bean
//    public AbstractClientServiceHandler getServiceHandler(){
//        return new AbstractClientServiceHandler() {
//            @Override
//            public RPCResponse handleService(RPCRequest request, Session session) {
//                if(logger.isDebugEnabled()){
//                    logger.debug("处理响应");
//                }
//                RPCResponse response = RPCResponse.buildResponse(request);
//                response.setMessage("hahauhuha");
//                return response;
//            }
//        };
//    }

    @PostConstruct
    public void start() throws ClientBindException {
        if(logger.isDebugEnabled()){
            logger.debug("客户端启动:  {}  {}-{}" , serverUrl, areaid,nodeid);
        }
        client.setServerUrl(serverUrl);
        client.setClientId(areaid,nodeid);
        client.bind();
    }
}
