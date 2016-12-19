package com.lsxy.framework.rpc.api.client;

import com.lsxy.framework.config.SystemConfig;
import com.lsxy.framework.rpc.api.RPCCaller;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.RPCResponse;
import com.lsxy.framework.rpc.api.ServiceConstants;
import com.lsxy.framework.rpc.api.session.Session;
import com.lsxy.framework.rpc.exceptions.ClientBindException;
import com.lsxy.framework.rpc.exceptions.ClientConnecException;
import com.lsxy.framework.rpc.exceptions.HaveNoExpectedRPCResponseException;
import com.lsxy.framework.rpc.exceptions.RequestTimeOutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * Created by tandy on 16/8/1.
 */
public abstract class AbstractClient implements Client{

    private static final Logger logger = LoggerFactory.getLogger(AbstractClient.class);
    
    //服务器连接url 多个
    private String serverUrls[];

    private String areaid;

    private String nodeid;

    @Autowired
    private RPCCaller rpcCaller;

    @Autowired
    private ClientSessionContext sessionContext;

    @Autowired(required = false)
    private ClientBindCallback bindCallback;

    private ExecutorService executorService;

    @Override
    public void bind() {
        executorService = Executors.newFixedThreadPool(serverUrls.length, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {

                Thread thread = new Thread(r);
                String name = "AgentDeamonThread-"+thread.getId();
                thread.setName(name);
                if(logger.isDebugEnabled()){
                    logger.debug("创建代理心跳线程：{}",name);
                }
                return thread;
            }
        });
        for (String serverUrl:serverUrls) {
            ServerDeamonTask task = new ServerDeamonTask(serverUrl,this.areaid);
            //刚刚开始就执行一次绑定
            try {
                Session session = this.doBind(serverUrl);

                //连接成功后,将session丢到环境里面去
                if(session != null) {
                    if(logger.isDebugEnabled()){
                        logger.debug("bind success and got session id is :{}" , session.getId() );
                    }

                    sessionContext.putSession(session);
                }

            } catch (ClientBindException e) {
                logger.error("客户端连接失败:{}   {}",serverUrl,e.getMessage());
            }
            executorService.submit(task);
        }
    }


    protected abstract Session doBind(String serverUrl) throws ClientBindException;

    /**
     * 设置服务器连接url
     * 支持格式
     * cluster://ip:port|ip:port|ip:port
     * @param serverUrl
     */
    @Override
    public void setServerUrl(String serverUrl) {
        Assert.notNull(serverUrl);
        Assert.isTrue(serverUrl.startsWith("cluster://"),"serverUrl配置不正确 ["+serverUrl+"]  vs  [cluster://192.168.1.1:3000|192.168.1.2:3000|192.168.1.2|2222]");
        String urlstring = serverUrl.substring(10);
        serverUrls = urlstring.split("\\|");
    }

    @Override
    public void setClientId(String areaid,String nodeid) {
        this.areaid = areaid;
        this.nodeid = nodeid;
    }


    public static void main(String[] args) {
        int size = 20;
        int j = 0;
        int count = 0;
        while(true && j < 2){
            int idx = count ++ % size;
            System.out.println(idx);
            if(idx == (size-1)){
                j ++;
            }
        }

    }

//    public String[] getServerUrls() {
//        return serverUrls;
//    }
//
//    public void setServerUrls(String[] serverUrls) {
//        this.serverUrls = serverUrls;
//    }
//
//    public String getClientId() {
//        return areaid;
//    }

    public RPCCaller getRpcCaller() {
        return rpcCaller;
    }

//    public void setRpcCaller(RPCCaller rpcCaller) {
//        this.rpcCaller = rpcCaller;
//    }


    /**
     * 客户端连接监控线程
     * 每一个连接会有一个线程进行监控,如果断开,会尝试重复连接,直到连接上为止
     */
    class ServerDeamonTask implements Runnable{
        private final Logger logger = LoggerFactory.getLogger(ServerDeamonTask.class);
        private String serverUrl;
        private String clientId;
        ServerDeamonTask(String serverUrl,String clientId){
            this.serverUrl = serverUrl;
            this.clientId = clientId;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    try {
                        TimeUnit.SECONDS.sleep(5);

                        Session session = sessionContext.getSession(serverUrl);
                        if (session != null && session.isValid()) {

                            RPCRequest echoRequest = RPCRequest.newRequest(ServiceConstants.CH_MN_HEARTBEAT_ECHO, "");
                            RPCResponse echoResponse = rpcCaller.invokeWithReturn(session, echoRequest);
                            if (echoResponse.isOk()) {
                                if (logger.isDebugEnabled() && SystemConfig.getProperty("area.agent.log.show.heartbeat", "true").equals("true")) {
                                    logger.debug("连接着呢:{}", this.serverUrl);
                                }
                            }
                            continue;
                        }

                        sessionContext.remove(serverUrl);
                        session = doBind(this.serverUrl);
                        if (session != null) {
                            sessionContext.putSession(session);
                            logger.info("连接区域管理服务{}:{}】成功,", session.getRemoteAddress().getAddress().getHostAddress(), session.getRemoteAddress().getPort());
                        }
                    } catch (RequestTimeOutException e) {
                        logger.error("心跳请求超时:" + serverUrl, e);
                    } catch (InterruptedException e) {
                        logger.error("出现异常", e);
                    } catch (ClientBindException e) {
                        logger.error("客户端连接异常:" + serverUrl, e);
                    } catch (HaveNoExpectedRPCResponseException e) {
                        logger.error("非期待响应对象:" + serverUrl, e);
                    }
                }
            }catch(Exception ex){
                logger.error("后台心跳线程竟然退出了:"+serverUrl,ex);
            }
        }
    }


    /**
     * 通过指定的session向服务器发送连接指令进行区域节点的注册
     * @param session
     * @throws ClientConnecException
     */
    protected void doConnect(Session session) throws ClientConnecException {
        try {
            if(session==null || !session.isValid()){
                throw new ClientConnecException("客户端连接通道无效,无法发送区域节点注册指令:"+session.getId());
            }
            String param = "aid=" + areaid+"&nid="+nodeid+"&sessionid="+session.getId();
            RPCRequest request = RPCRequest.newRequest(ServiceConstants.CH_MN_CONNECT,param);
            RPCResponse response = getRpcCaller().invokeWithReturn(session, request);
            if(response == null){
                throw new ClientConnecException("区域节点注册时出现异常:"+session.getId());
            }
            if (response.isOk()) {
                logger.info("连接区域管理服务{}:{}】成功,",session.getRemoteAddress().getAddress().getHostAddress(),session.getRemoteAddress().getPort());

                if (bindCallback != null) {
                    bindCallback.doCallback(session);
                }
            } else {
                logger.error("连接异常不成功:{}",response.getBodyAsString());
                session.close(true);
            }
        }catch (Exception ex){
            throw new ClientConnecException(ex);
        }
    }
}
