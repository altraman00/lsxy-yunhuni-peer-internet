package com.lsxy.area.server;

import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.RPCResponse;
import com.lsxy.framework.rpc.api.handler.HandlerManager;
import com.lsxy.framework.rpc.api.server.AbstractServiceHandler;
import com.lsxy.framework.rpc.api.server.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by tandy on 16/8/8.
 */
@Component
public class AreaServerServiceHandler extends AbstractServiceHandler {


    private static final Logger logger = LoggerFactory.getLogger(AreaServerServiceHandler.class);

    @Autowired(required = false)
    private StasticsCounter sc;

    @Autowired
    private HandlerManager handlerManager;

    @Override
    public RPCResponse handleService(RPCRequest request, Session session) {

        if(sc != null) sc.getReceivedAreaNodeRequestCount().incrementAndGet();

        RPCResponse response = handlerManager.fire(request,session);

        return response;
    }

//    public static void main(String[] args) {
//        String url = "http://101.200.73.13:3000/incoming";
//        RestRequest request = RestRequest.buildRequest();
//        RestResponse<String> response = request.get(url,String.class);
//        System.out.println(response.getData());
//
//    }
}
