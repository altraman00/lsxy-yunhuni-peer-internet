package com.lsxy.area.agent.handler.conf;

import com.lsxy.app.area.cti.commander.Client;
import com.lsxy.app.area.cti.commander.RpcError;
import com.lsxy.app.area.cti.commander.RpcResultListener;
import com.lsxy.area.agent.cti.CTIClientContext;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.RPCResponse;
import com.lsxy.framework.rpc.api.ServiceConstants;
import com.lsxy.framework.rpc.api.handler.RpcRequestHandler;
import com.lsxy.framework.rpc.api.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * Created by liuws on 2016/8/27.
 */
@Component
public class Handler_MN_CH_SYS_CONF_PLAY_STOP extends RpcRequestHandler{

    private static final Logger logger = LoggerFactory.getLogger(Handler_MN_CH_SYS_CONF_PLAY_STOP.class);

    @Autowired
    private CTIClientContext cticlientContext;

    @Override
    public String getEventName() {
        return ServiceConstants.MN_CH_SYS_CONF_PLAY_STOP;
    }

    @Override
    public RPCResponse handle(RPCRequest request, Session session) {
        if(logger.isDebugEnabled()){
            logger.debug("开始处理{}事件,{}",getEventName(),request);
        }
        RPCResponse response = RPCResponse.buildResponse(request);

        Client cticlient = cticlientContext.getAvalibleClient();
        if(cticlient == null) {
            response.setMessage(RPCResponse.STATE_EXCEPTION);
            return response;
        }

        Map<String, Object> params = request.getParamMap();
        String conf_id = (String)params.get("user_data");
        String res_id = (String)params.get("res_id");

        try {
            cticlient.operateResource(0, 0, res_id,"sys.conf.play_stop", params, new RpcResultListener(){

                @Override
                protected void onResult(Object o) {
                    if(logger.isDebugEnabled()){
                        logger.debug("调用sys.conf.play_stop成功conf_id={},result={}",conf_id,o);
                    }
                }

                @Override
                protected void onError(RpcError rpcError) {
                    logger.error("调用sys.conf.play_stop失败conf_id={},result={}",conf_id,rpcError);
                }

                @Override
                protected void onTimeout() {
                    logger.error("调用sys.conf.play_stop超时conf_id={}",conf_id);
                }
            });
            response.setMessage(RPCResponse.STATE_OK);
        } catch (IOException e) {
            e.printStackTrace();
            response.setMessage(RPCResponse.STATE_EXCEPTION);
        }
        return response;

    }
}
