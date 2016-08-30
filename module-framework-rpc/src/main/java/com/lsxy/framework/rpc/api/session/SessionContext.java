package com.lsxy.framework.rpc.api.session;

import com.lsxy.framework.rpc.exceptions.RightSessionNotFoundExcepiton;
import org.apache.commons.collections.map.ListOrderedMap;

import java.util.Collection;

/**
 * Created by tandy on 16/8/3.
 */
public abstract class SessionContext {

    //选择合适的session的策略
    private SelectSessionPolicy selectSessionPolicy = new DefaultSelectSessionPolicy(this);

    /**
     * 放入session对象到环境中
     * @param session
     */
    public abstract void putSession(Session session);

    /**
     * 会话交互过程中,根据sessionid获取会话对象
     * @param sessionid
     * @return
     */
    public abstract Session getSession(String sessionid);
    /**
     * 会话交互过程中,根据sessionid获取会话对象
     * @param idx
     * @return
     */
    public abstract Session getSession(int idx);
    /**
     * 根据会话标识清理会话数据
     * @param sessionid
     */
    public abstract void remove(String sessionid) ;

    /**
     * 获取所有session
     * @return
     */
    public abstract Collection<Session> sessions();


    /**
     * 根据具体情况获取合适的,正确的RPC SESSION对象
     * 去掉 RightSessionNotFoundExcepiton  ,不再抛出该异常
     * 如果没找到就返回空
     * 为了能够让rpccall自己判断是否session为空,统一处理消息重发问题
     * @return
     */
    public Session getRightSession() throws RightSessionNotFoundExcepiton {
        if(this.selectSessionPolicy != null)
            return this.selectSessionPolicy.select();
        else{
            return null;
        }
    }
    /**
     * 设置会话选择策略
     * @param selectSessionPolicy
     */
    public void setSelectSessionPolicy(SelectSessionPolicy selectSessionPolicy){
        this.selectSessionPolicy = selectSessionPolicy;
    }

    public abstract int size();
}
