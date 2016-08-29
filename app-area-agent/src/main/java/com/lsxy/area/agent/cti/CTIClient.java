package com.lsxy.area.agent.cti;

import com.lsxy.app.area.cti.commander.Client;
import com.lsxy.app.area.cti.commander.RpcEventListener;
import com.lsxy.app.area.cti.commander.RpcRequest;
import com.lsxy.app.area.cti.commander.Unit;
import com.lsxy.area.agent.StasticsCounter;
import com.lsxy.framework.rpc.api.RPCCaller;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.ServiceConstants;
import com.lsxy.framework.rpc.api.client.ClientSessionContext;
import com.lsxy.framework.rpc.api.server.Session;
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


    @Autowired(required = false)
    private StasticsCounter sc;

    @Value("${area.agent.client.cti.unitid}")
    private byte localUnitID;

    @Autowired
    private CTIClientContext clientContext;

    @Autowired
    private CTIClientConfigFactory ctiClientConfigFactory;

    @Autowired
    private RPCCaller rpcCaller;

    @Autowired
    private ClientSessionContext sessionContext;

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
        /*收到CTI事件计数*/
        sc.getReceivedCTIEventCount().incrementAndGet();

        if(logger.isDebugEnabled()){
            logger.debug("收到事件通知:{}-{}",rpcRequest.getMethod(),rpcRequest.getParams());
        }
        if(sc != null) {
            if (rpcRequest.getMethod().equals("sys.call.on_incoming")) {
                sc.getReceivedCTIIncomingEventCount().incrementAndGet();
            }

            if (rpcRequest.getMethod().equals("sys.call.on_dial_completed")) {
                sc.getReceivedCTIDialCompleteEventCount().incrementAndGet();
            }
            if (rpcRequest.getMethod().equals("sys.call.on_released")) {
                sc.getReceivedCTIReleaseEventCount().incrementAndGet();
            }
            if (null != rpcRequest.getParams().get("error") && rpcRequest.getParams().get("error").equals("dial timeout")) {
                sc.getReceivedCTIDialTimeOutEventCount().incrementAndGet();
            }
            if (null != rpcRequest.getParams().get("error") && rpcRequest.getParams().get("error").equals("dial failed")) {
                sc.getReceivedCTIDialFailedEventCount().incrementAndGet();
            }
        }
        try {
            rpcRequest.getParams().put("method",rpcRequest.getMethod());
            //收到事件,向中心报告所有事件
            RPCRequest areaRPCRequest = RPCRequest.newRequest(ServiceConstants.CH_MN_CTI_EVENT,rpcRequest.getParams());
            Session session = sessionContext.getRightSession();

            assert rpcCaller!=null;
            /*发送区域管理器请求次数计数*/
            if(sc!=null) sc.getSendAreaServerRequestCount().incrementAndGet();

            rpcCaller.invoke(sessionContext,areaRPCRequest);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("CTI事件通知区域管理器时发生异常,事件被丢失:{}-{}",rpcRequest.getMethod(), rpcRequest.getParams());
        }
    }
}
