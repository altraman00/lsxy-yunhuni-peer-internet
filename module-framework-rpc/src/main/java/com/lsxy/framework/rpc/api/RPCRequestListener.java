package com.lsxy.framework.rpc.api;

/**
 * Created by tandy on 16/7/30.
 */
public interface RPCRequestListener {
    /**
     * 注册监听器
     * @param listener
     */
    public void addRequestListener(RequestListener listener);

    /**
     * 移除监听器
     * @param listener
     */
    public void removeRequestListener(RequestListener listener);
}
