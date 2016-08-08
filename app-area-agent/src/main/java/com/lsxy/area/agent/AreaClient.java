package com.lsxy.area.agent;

import com.lsxy.framework.rpc.api.client.Client;
import com.lsxy.framework.rpc.exceptions.ClientBindException;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by tandy on 16/7/30.
 * 区域客户端
 */
@Component
public class AreaClient {

    private static final Logger logger = LoggerFactory.getLogger(AreaClient.class);

    @Value("${area.agent.server.url}")
    private String serverUrl;

    @Value("${area.agent.client.id}")
    private String clientId;

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
            logger.debug("客户端启动:  {}  {}" , serverUrl,clientId);
        }
        client.setServerUrl(serverUrl);
        client.setClientId(clientId);
        client.bind();
    }



}
