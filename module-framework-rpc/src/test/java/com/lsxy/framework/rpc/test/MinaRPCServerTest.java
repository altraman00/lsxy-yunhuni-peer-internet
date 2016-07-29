package com.lsxy.framework.rpc.test;

import com.lsxy.framework.rpc.FrameworkRPCConfig;
import com.lsxy.framework.rpc.api.server.AbstractServiceHandler;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.RPCResponse;
import com.lsxy.framework.rpc.mina.server.MinaRemoteServer;
import org.apache.mina.core.session.IoSession;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * Created by tandy on 16/7/29.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(MinaRPCServerTest.class)
@Import(FrameworkRPCConfig.class)
public class MinaRPCServerTest {

    @Autowired
    private MinaRemoteServer server;

    @Test
    public void test001() throws IOException, GeneralSecurityException {
        Assert.notNull(server);
        server.bind();
        System.out.println("");
    }


}
