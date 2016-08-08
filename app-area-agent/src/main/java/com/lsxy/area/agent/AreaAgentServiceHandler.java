package com.lsxy.area.agent;

import com.lsxy.app.area.cti.commander.Client;
import com.lsxy.app.area.cti.commander.RpcError;
import com.lsxy.app.area.cti.commander.RpcResultListener;
import com.lsxy.area.agent.cti.CTIClient;
import com.lsxy.area.agent.cti.CTIClientContext;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.RPCResponse;
import com.lsxy.framework.rpc.api.ServiceConstants;
import com.lsxy.framework.rpc.api.client.AbstractClientServiceHandler;
import com.lsxy.framework.rpc.api.server.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tandy on 16/8/5.
 * 区域代理业务事件处理器
 */
@Component
public class AreaAgentServiceHandler extends AbstractClientServiceHandler {
    private static final Logger logger = LoggerFactory.getLogger(AreaAgentServiceHandler.class);

    @Autowired(required = false)
    private CTIClient cticlient;

    @Autowired
    private CTIClientContext cticlientContext;

    @Override
    public RPCResponse handleService(RPCRequest request, Session session) {
        if(logger.isDebugEnabled()){
            logger.debug("收到请求:{}",request );
        }

        RPCResponse response = null;

        if(request.getName().equals(ServiceConstants.MN_CH_SYS_CALL)){
            response = this.process_MN_CH_SYS_CALL(request);
        }

        return response;
    }

    private RPCResponse process_MN_CH_SYS_CALL(RPCRequest request) {
        if(logger.isDebugEnabled()){
            logger.debug("handler process_MN_CH_SYS_CALL:{}",request);
        }

        RPCResponse response = RPCResponse.buildResponse(request);
        Client cticlient = cticlientContext.getAvalibleClient();
        if(cticlient == null) {
            response.setMessage(RPCResponse.STATE_EXCEPTION);
            return response;
        }
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("from_uri", "");
            params.put("to_uri", "192.168.2.100:5062");
            params.put("max_answer_seconds", (int) (50 * Math.random()));
            params.put("max_ring_seconds", (int) (10 * Math.random()));


            cticlient.createResource(0, 0, "sys.call", params, new RpcResultListener() {
                @Override
                protected void onResult(Object result) {
                    logger.debug("呼出 返回值：(result={})", result);
//                    String callId = (String) result;
//                    Map<String, Object> params = new HashMap<>();
//                    try {
//                        cticlient.operateResource((byte) 0, 0, callId, "sys.call.drop", params, new RpcResultListener() {
//                            @Override
//                            protected void onResult(Object result) {
//                                logger.debug("挂机 返回值：(result={})", result);
//                            }
//
//                            @Override
//                            protected void onError(RpcError error) {
//                                logger.debug("挂机 错误:{}", error);
//                            }
//
//                            @Override
//                            protected void onTimeout() {
//                                logger.debug("挂机 超时");
//                            }
//                        });
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                }

                @Override
                protected void onError(RpcError rpcError) {

                }

                @Override
                protected void onTimeout() {

                }
            });
            response.setMessage(RPCResponse.STATE_OK);
        } catch (IOException e) {
            response.setMessage(RPCResponse.STATE_EXCEPTION);
            e.printStackTrace();
        }
        return response;
    }
}
