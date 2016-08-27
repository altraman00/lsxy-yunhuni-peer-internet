package com.lsxy.framework.rpc.api.handler;

/**
 * Created by liuws on 2016/8/27.
 */
public interface Handler<Q,S> {

    public S handle(Q q);

}
