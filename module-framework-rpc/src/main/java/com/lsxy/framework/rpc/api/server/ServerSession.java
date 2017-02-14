package com.lsxy.framework.rpc.api.server;

import com.lsxy.framework.rpc.api.session.Session;

/**
 * Created by tandy on 17/1/20.
 */
public interface ServerSession extends Session{
    public long getClientStartTimestamp();
    public String getClientVersion();
}
