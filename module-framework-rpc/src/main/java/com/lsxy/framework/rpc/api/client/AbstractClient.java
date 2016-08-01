package com.lsxy.framework.rpc.api.client;

import com.lsxy.framework.rpc.api.RPCCaller;
import com.lsxy.framework.rpc.api.server.Session;
import com.lsxy.framework.rpc.exceptions.ClientBindException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by tandy on 16/8/1.
 */
public abstract class AbstractClient implements Client{

    //服务器连接url 多个
    private String serverUrls[];

    //客户端标识
    private String clientId;

    @Autowired
    private RPCCaller rpcCaller;

    @Autowired
    private ClientSessionContext sessionContext;

    private ExecutorService executorService;


    @Override
    public void bind() throws ClientBindException {
        executorService = Executors.newFixedThreadPool(serverUrls.length);
        for (String serverUrl:serverUrls) {
            ServerDeamonTask task = new ServerDeamonTask(serverUrl,this.clientId);
            //刚刚开始就执行一次绑定
            this.doBind(serverUrl);
            executorService.submit(task);
        }
    }


    protected abstract Session doBind(String serverUrl) ;

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

        return null;
    }

    public String[] getServerUrls() {
        return serverUrls;
    }

    public void setServerUrls(String[] serverUrls) {
        this.serverUrls = serverUrls;
    }

    public String getClientId() {
        return clientId;
    }

    public RPCCaller getRpcCaller() {
        return rpcCaller;
    }

    public void setRpcCaller(RPCCaller rpcCaller) {
        this.rpcCaller = rpcCaller;
    }


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

                    if(logger.isDebugEnabled()){
                        logger.debug("尝试连接:"+this.serverUrl);
                    }

                    doBind(this.serverUrl);
                }

            }
        }
    }
}
