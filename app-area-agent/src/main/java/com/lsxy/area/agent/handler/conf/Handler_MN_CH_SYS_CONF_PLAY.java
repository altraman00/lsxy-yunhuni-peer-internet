package com.lsxy.area.agent.handler.conf;

import com.lsxy.app.area.cti.RpcError;
import com.lsxy.app.area.cti.RpcResultListener;
import com.lsxy.area.agent.cti.CTIClientContext;
import com.lsxy.area.agent.cti.CTINode;
import com.lsxy.framework.core.utils.JSONUtil2;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.RPCResponse;
import com.lsxy.framework.rpc.api.ServiceConstants;
import com.lsxy.framework.rpc.api.handler.RpcRequestHandler;
import com.lsxy.framework.rpc.api.session.Session;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by liuws on 2016/8/27.
 */
@Component
public class Handler_MN_CH_SYS_CONF_PLAY extends RpcRequestHandler{

    private static final Logger logger = LoggerFactory.getLogger(Handler_MN_CH_SYS_CONF_PLAY.class);

    @Autowired
    private CTIClientContext cticlientContext;

    @Override
    public String getEventName() {
        return ServiceConstants.MN_CH_SYS_CONF_PLAY;
    }

    @Override
    public RPCResponse handle(RPCRequest request, Session session) {


        Map<String, Object> params = request.getParamMap();
        String conf_id = (String)params.get("user_data");
        String res_id = (String)params.get("res_id");

        try {
            String content = (String)params.get("content");
            if(StringUtils.isNotEmpty(content)){
                params.put("content", JSONUtil2.fromJson(content,(new Object[1][]).getClass()));
            }
            CTINode cticlient = cticlientContext.getAvalibleNode(res_id);
            cticlient.operateResource( res_id,"sys.conf.play_start", params, new RpcResultListener(){

                @Override
                protected void onResult(Object o) {
                    if(logger.isDebugEnabled()){
                        logger.debug("调用sys.conf.play_start成功conf_id={},result={}",conf_id,o);
                    }
                }

                @Override
                protected void onError(RpcError rpcError) {
                    logger.error("调用sys.conf.play_start失败conf_id={},result={}",conf_id,rpcError);
                }

                @Override
                protected void onTimeout() {
                    logger.error("调用sys.conf.play_start超时conf_id={}",conf_id);
                }
            });
        } catch (Throwable e) {
            logger.error("调用资源操作失败",e);
        }
        return null;

    }
}
