package com.lsxy.framework.rpc.api.session;

import com.lsxy.framework.rpc.api.RPCMessage;

/**
 * Created by tandy on 16/8/30.
 */
public interface SelectSessionPolicy {

    public Session select(RPCMessage request);
}
