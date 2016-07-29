package com.lsxy.area.server;

import com.lsxy.framework.rpc.FrameworkRPCConfig;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.RPCResponse;
import com.lsxy.framework.rpc.api.server.AbstractServiceHandler;
import com.lsxy.framework.rpc.api.server.RemoteServer;
import com.lsxy.framework.rpc.exceptions.RemoteServerStartException;
import com.lsxy.framework.web.web.AbstractSpringBootStarter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import static com.lsxy.framework.core.web.SpringContextUtil.getBean;
import static org.springframework.boot.SpringApplication.run;

/**
 * Created by tandy on 16/7/19.
 */
@SpringBootApplication
@ComponentScan
@Import(FrameworkRPCConfig.class)
public class MainClass extends AbstractSpringBootStarter{

    private static final Logger logger = LoggerFactory.getLogger(MainClass.class);
    @Override
    public String systemId() {
        return "area-server";
    }

    @Bean
    public AbstractServiceHandler getServiceHandler(){
        return new AbstractServiceHandler() {
            @Override
            public RPCResponse handleService(RPCRequest request, IoSession session) {
                if(logger.isDebugEnabled()){
                    logger.debug("处理响应");
                }
                RPCResponse response = RPCResponse.buildResponse(request);
                response.setMessage("hahaha");
                return response;
            }
        };
    }

    public static void main(String[] args) throws RemoteServerStartException {
        ApplicationContext applicationContext = SpringApplication.run(MainClass.class);
        RemoteServer remoteServer = applicationContext.getBean(RemoteServer.class);
        remoteServer.startServer();
    }
}
