package com.lsxy.framework.rpc.api.client;

import com.lsxy.framework.rpc.api.RPCCaller;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.RPCResponse;
import com.lsxy.framework.rpc.api.ServiceConstants;
import com.lsxy.framework.rpc.api.server.Session;
import com.lsxy.framework.rpc.exceptions.ClientBindException;
import com.lsxy.framework.rpc.exceptions.ClientConnecException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by tandy on 16/8/1.
 */
public abstract class AbstractClient implements Client{

    private static final Logger logger = LoggerFactory.getLogger(AbstractClient.class);
    
    //服务器连接url 多个
    private String serverUrls[];

    //客户端标识
    private String clientId;

    @Autowired
    private RPCCaller rpcCaller;

    @Autowired
    private ClientSessionContext sessionContext;

    @Autowired(required = false)
    private ClientBindCallback bindCallback;

    //有效会话选择器
    private AtomicInteger sessionSelectCounter = new AtomicInteger(0);

    private ExecutorService executorService;

    @Override
    public void bind() {
        executorService = Executors.newFixedThreadPool(serverUrls.length);
        for (String serverUrl:serverUrls) {
            ServerDeamonTask task = new ServerDeamonTask(serverUrl,this.clientId);
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
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    @Override
    public Session getAvalibleSession() {
        int idx = 0;    //索引  默认为0
        int times = 1;  //遍历次数,一次
        int j = 0;      //当前遍历次数
        Session session = null;
        while(j<times && this.sessionContext.size()>0) {
            int i = this.sessionSelectCounter.addAndGet(1);
            //如果总计数器达到整型最大值,重置0
            if (i >= (Integer.MAX_VALUE - 1)) {
                this.sessionSelectCounter.set(0);
            }
            int size = this.sessionContext.size();
            idx = i % size;
            //遍历到最后一个表示到了一轮
            if(idx == (size -1)){
                j++;
            }
            session = this.sessionContext.getSessionByIndex(idx);
            if(session.isValid()){
                return session;
            }else{
                continue;
            }
        }
        return session;
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
//        return clientId;
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
            while(true){
                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Session session = sessionContext.getSession(serverUrl);
                if(session != null ){
                    //如果有效,继续,否则清理掉
                    if(session.isValid()){
                        continue;
                    }else{
                        sessionContext.remove(serverUrl);
                    }
                }else{
                    logger.info("尝试连接:"+this.serverUrl);
                    try {
                        doBind(this.serverUrl);
                    } catch (ClientBindException e) {
                        logger.error("客户端连接失败:{}    {}", serverUrl,  e.getMessage());
                    }
                }

            }
        }
    }


    protected void doConnect(Session session) throws ClientConnecException {
        try {
            String param = "sessionid=" + session.getId();
            RPCRequest request = RPCRequest.newRequest(ServiceConstants.CH_MN_CONNECT,param);
            RPCResponse response = getRpcCaller().invokeWithReturn(session, request);
            if (response.isOk()) {
                if (bindCallback != null) {
                    bindCallback.doCallback(session);
                }
                //连接成功构建正式的session对象,将serverUrl作为Sessionid
                logger.info("连接节点管理器【{}:{}】成功,",session.getRemoteAddress().getAddress().getHostAddress(),session.getRemoteAddress().getPort());
            } else {
                String msg = new String(response.getBody(), "utf-8");
                logger.info(msg);
                session.close(true);
            }
        }catch (Exception ex){
            throw new ClientConnecException(ex);
        }
    }
}
