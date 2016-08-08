package com.lsxy.area.agent.cti;

import com.lsxy.app.area.cti.commander.Client;
import com.lsxy.app.area.cti.commander.RpcEventListener;
import com.lsxy.app.area.cti.commander.RpcRequest;
import com.lsxy.app.area.cti.commander.Unit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Set;

/**
 * Created by tandy on 16/8/5.
 * CTI 客户端启动器  需要配合JNI使用
 */
@Component
@Profile(value={"test","production","development"})
public class CTIClient implements RpcEventListener{


    private static final Logger logger = LoggerFactory.getLogger(CTIClient.class);

    @Value("${area.agent.client.cti.unitid}")
    private byte localUnitID;

    @Autowired
    private CTIClientContext clientContext;

    @Autowired
    private CTIClientConfigFactory ctiClientConfigFactory;


    @PostConstruct
    public void start(){
        if(logger.isDebugEnabled()){
            logger.debug("开始启动CTI客户端,初始化UnitID:{}",localUnitID);
        }

        Unit.initiate(localUnitID);
        try {
            Set<CTIClientConfigFactory.CTIClientConfig>  configs = ctiClientConfigFactory.getConfigs();
            for (CTIClientConfigFactory.CTIClientConfig config:configs ) {
                Client client = Unit.createClient(config.clientId, config.ctiHost, this);
                if(logger.isDebugEnabled()){
                    logger.debug("client id {} create invoke complete, connect to {}" , config.clientId,config.ctiHost);
                }
                clientContext.add(config.clientId,client);
            }

        }catch(Exception ex){
            logger.error("CTI客户端启动失败:{}",ex.getMessage());
            ex.printStackTrace();
        }
    }

    @Override
    public void onEvent(RpcRequest rpcRequest) {
        if(logger.isDebugEnabled()){
            logger.debug("收到事件通知:{}-{}",rpcRequest.getMethod(),rpcRequest.getParams());
        }
    }
}
