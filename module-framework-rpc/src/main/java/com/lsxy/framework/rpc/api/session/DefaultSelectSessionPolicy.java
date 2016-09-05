package com.lsxy.framework.rpc.api.session;

import com.lsxy.framework.rpc.api.RPCMessage;

import java.util.concurrent.atomic.AtomicInteger;

import static com.lsxy.framework.rpc.api.server.ServerSessionContext.logger;

/**
 * Created by tandy on 16/8/30.
 */
public class DefaultSelectSessionPolicy implements SelectSessionPolicy {

    private static DefaultSelectSessionPolicy instance;
    protected SessionContext sessionContext;

    //有效会话选择器
    private AtomicInteger sessionSelectCounter = new AtomicInteger(0);

    @Override
    public Session select(RPCMessage request) {
        return getSession();
    }

    /**
     * 获取有效的会话
     * 负载均衡算法
     * @return
     * */
    public Session getSession()  {
        int idx = 0;    //索引  默认为0
        int times = 1;  //遍历次数,一次
        int j = 0;      //当前遍历次数
        if(sessionContext.size()<=0) {
            logger.error("没有找到有效的会话");
            return null;
        }

        Session session = null;
        while(j<times && sessionContext.size()>0) {
            int i = this.sessionSelectCounter.addAndGet(1);
            //如果总计数器达到整型最大值,重置0
            if (i >= (Integer.MAX_VALUE - 1)) {
                this.sessionSelectCounter.set(0);
            }
            int size = sessionContext.size();
            idx = i % size;
            //遍历到最后一个表示到了一轮
            if(idx == (size -1)){
                j++;
            }
            session = sessionContext.getSession(idx);
            if(session.isValid()){
                return session;
            }else{
                continue;
            }
        }
        return session;
    }

    public static SelectSessionPolicy getInstance(SessionContext sessionContext) {
        if(instance == null){
            instance = new DefaultSelectSessionPolicy();
            instance.setSessionContext(sessionContext);
        }
        return instance;
    }

    public void setSessionContext(SessionContext sessionContext) {
        this.sessionContext = sessionContext;
    }
}
