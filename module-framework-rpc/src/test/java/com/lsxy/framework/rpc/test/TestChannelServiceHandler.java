package com.lsxy.framework.rpc.test;

import com.lsxy.framework.rpc.api.server.AbstractServiceHandler;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.RPCResponse;
import org.apache.mina.core.session.IoSession;
import org.springframework.stereotype.Component;

/**
 * Created by tandy on 16/7/29.
 */
@Component
public class TestChannelServiceHandler extends AbstractServiceHandler {
    @Override
    public RPCResponse handleService(RPCRequest request, IoSession session) {
        return null;
    }
}
