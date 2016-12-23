package com.lsxy.area.agent.cti;

import com.lsxy.app.area.cti.*;
import com.lsxy.area.agent.StasticsCounter;
import com.lsxy.framework.rpc.api.RPCCaller;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.ServiceConstants;
import com.lsxy.framework.rpc.api.session.SessionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by tandy on 16/8/5.
 * CTI 客户端启动器  需要配合JNI使用
 */
@Component
@Profile(value={"test","production", "development","localdev"})
public class CTIClient implements RpcEventListener,MonitorEventListener,Runnable,UnitCallbacks{

    private static final Logger logger = LoggerFactory.getLogger(CTIClient.class);

    @Autowired(required = false)
    private StasticsCounter sc;

    private int clientId;

    //本地unitid 由于需要通过环境变量设置值,所以不适用"." 而适用_
    @Value("${area_agent_client_cti_unitid:20}")
    private byte localUnitID;

    @Autowired
    private CTIClientContext clientContext;

    @Autowired
    private CTIClientConfigFactory ctiClientConfigFactory;

    @Autowired
    private RPCCaller rpcCaller;

    @Autowired
    private SessionContext sessionContext;

    @PostConstruct
    public void start() {
        clientContext.init();

        if (logger.isDebugEnabled()) {
            logger.debug("开始启动CTI客户端,初始化UnitID:{}", localUnitID);
        }

        Unit.initiate(localUnitID,this);
        try {
            Set<String> servers = clientContext.getCTIServers();
            for(String serverIp:servers) {
                createNewCTICommander(serverIp);
            }

        } catch (Exception ex) {
            logger.error("CTI客户端启动失败",ex);
        }

        //启动后台探测线程
        runDaemonThread();
    }

    /**
     * 创建新的CTI Commander连接对象
     * @param serverIp
     * @throws InterruptedException
     */
    private void createNewCTICommander(String serverIp) throws InterruptedException {
        Commander commander = Unit.createCommander((byte)clientId, serverIp, this, this);
        if (logger.isDebugEnabled()) {
            logger.debug("client id {} create invoke complete, connect to {}", clientId, serverIp);
        }
        clientContext.registerCommander(serverIp, commander);
        clientId = clientId + 2;
    }

    /**
     * 启动监控线程
     */
    private void runDaemonThread() {
        //启动监听线程
        Thread deamonThread = new Thread(this);
        deamonThread.setDaemon(true);
        deamonThread.setName("CTIClientMonitorThread");
        deamonThread.start();
    }

    @Override
    public void onEvent(BusAddress busAddress, RpcRequest rpcRequest) {
        /*收到CTI事件计数*/

        if (logger.isDebugEnabled()) {
            logger.debug("收到事件通知:{}-{}", rpcRequest.getMethod(), rpcRequest.getParams());
        }
        if (sc != null) {
            sc.getReceivedCTIEventCount().incrementAndGet();
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
            assert rpcCaller!=null;
            /*发送区域管理器请求次数计数*/
            if (sc != null) sc.getSendAreaServerRequestCount().incrementAndGet();

            rpcCaller.invoke(sessionContext,areaRPCRequest);
        } catch (Exception e) {
            logger.error("CTI事件通知区域管理器时发生异常,事件被丢失:{}-{}",rpcRequest.getMethod(), rpcRequest.getParams());
            logger.error("CTI事件通知区域管理器时发生异常",e);
        }
    }

    @Override
    public void onServerLoadChanged(BusAddress busAddress, ServerInfo serverInfo) {
        if(logger.isDebugEnabled()){
            logger.debug("收到负载数据："+serverInfo);
        }

        String unitId = busAddress.getUnitId() + "";
        String pid = busAddress.getClientId() + "";
        clientContext.updateNodeLoadData(unitId,pid,serverInfo.getLoads());
    }

    @Override
    public void run() {
        while(true){
            try {
                TimeUnit.SECONDS.sleep(10);
                if(logger.isDebugEnabled()){
                    logger.debug("10秒一次尝试从Redis缓存加载CTI配置");
                }
                //重新加载一次redis配置
                clientContext.loadConfig();
                Set<String> servers = clientContext.getCTIServers();
                for (String serverIp : servers) {
                    if(clientContext.isNewServerFound(serverIp)){
                        if(logger.isDebugEnabled()){
                            logger.debug("有新的CTI服务连接被发现：{}"+serverIp);
                        }
                        createNewCTICommander(serverIp);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void connectSucceed(Client client) {
        if(logger.isDebugEnabled()){
            logger.debug("CTI 服务连接成功：" + client.getIp() +":" + client.getPort());
        }
    }

    @Override
    public void connectFailed(Client client, int i) {
        logger.error("CTI 服务连接失败：" + client.getIp() + ":" + client.getPort() +"=>"+i);
    }

    @Override
    public void connectLost(Client client) {
        logger.error("CTI 服务连接丢失："+client.getIp());
        String ip = client.getIp();
        byte unitid = client.getUnitId();
        clientContext.ctiClientConnectionLost(ip,unitid);
    }

    /**
     * 全局（整个 CTI BUS 上的连接状态变化事件）
     *
     * @param unitId     产生连接状态变化的BUS节点的Unit ID
     * @param clientId   产生连接状态变化的BUS节点的Client ID。是node中心节点连接时，client id 值为 -1
     * @param clientType 产生连接状态变化的BUS节点的Client Type<br>
     *                   类型定义：
     *                   <ul>
     *                   <li>0: NULL</li>
     *                   <li>1: BUS 服务</li>
     *                   <li>2: IPSC（CTI服务进程）服务</li>
     *                   <li>3: IPSC 监控服务</li>
     *                   </ul>
     * @param status     产生连接状态变化的BUS节点的连接状态 <br>
     *                   状态值：
     *                   <ul>
     *                   <li>0: 断开连接</li>
     *                   <li>1: 新建连接</li>
     *                   <li>2: 已有的连接</li>
     *                   </ul>
     * @param addInfo    产生连接状态变化的BUS节点的附加信息
     */
    @Override
    public void globalConnectStateChanged(byte unitId, byte clientId, byte clientType, byte status, String addInfo) {
        //AA只关心类型为2的IPSC服务
        if(clientId == 2){
            clientContext.connectStateChanged(unitId,clientId,status);
        }

    }
}
