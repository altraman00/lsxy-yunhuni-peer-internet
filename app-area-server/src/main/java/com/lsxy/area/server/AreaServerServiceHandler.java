package com.lsxy.area.server;

import com.lsxy.framework.rpc.api.RPCCaller;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.RPCResponse;
import com.lsxy.framework.rpc.api.ServiceConstants;
import com.lsxy.framework.rpc.api.client.AbstractClientServiceHandler;
import com.lsxy.framework.rpc.api.server.AbstractServiceHandler;
import com.lsxy.framework.rpc.api.server.ServerSessionContext;
import com.lsxy.framework.rpc.api.server.Session;
import com.lsxy.framework.rpc.exceptions.RequestWriteException;
import org.apache.commons.lang3.RandomUtils;
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

    @Autowired
    private RPCCaller rpcCaller;

    @Autowired
    private ServerSessionContext sessionContext;

    @Override
    public RPCResponse handleService(RPCRequest request, Session session) {
        if(logger.isDebugEnabled()){
                logger.debug("处理响应");
        }

        if(request.getName().equals(ServiceConstants.CH_MN_CTI_EVENT)){
            return process_CH_MN_CTI_EVENT(request,session);
        }

        RPCResponse response = RPCResponse.buildResponse(request);
        response.setMessage(RPCResponse.STATE_OK);
        return response;
    }

    /**
     * 处理CH_MN_CTI_EVENT CTI事件监听
     * 有CTI事件触发时,需要将事件告诉用户,让用户告知接下来如何处理
     * @param request
     * @param session
     * @return
     */
    private RPCResponse process_CH_MN_CTI_EVENT(RPCRequest request, Session session) {
        if(logger.isDebugEnabled()){
            logger.debug("收到CTI事件:{}" , request);
        }

        /*临时逻辑,如果收到是 incoming 事件  则概率性的挂断或接通
        sys.call.on_dial_completed
        sys.call.on_incoming
        sys.call.on_released
        */
        String method = (String) request.getParameter("method");
        if(logger.isDebugEnabled()){
            logger.debug("is incoming?  [{}] vs [sys.call.on_incoming]",method);
        }
        if(method.equals("sys.call.on_incoming")){
            if(logger.isDebugEnabled()){
                logger.debug("收到了INCOMING.........");
            }
            RPCRequest sendRequest = randomRequest(request);
            try {
                if(logger.isDebugEnabled()){
                    logger.debug("准备发送指令到CTI:{}",sendRequest);
                }
                rpcCaller.invoke(session,sendRequest);
            } catch (RequestWriteException e) {
                logger.error("发送区域的指令出现异常,指令发送失败:{}",sendRequest);
                e.printStackTrace();
            }
        }else{
            if(logger.isDebugEnabled()){
                logger.debug("不是 INCOMING.........");
            }
        }
        return null;
    }

    /**
     * 随机创建拒绝或挂断请求
     * @param request
     * @return
     */
    private RPCRequest randomRequest(RPCRequest request) {
        int radom = RandomUtils.nextInt(0,100);
        RPCRequest requestX = null;
        String param = null;
        if(radom % 2 == 0){
            param = "method=sys.call.drop&res_id="+request.getParameter("res_id")+"&cause=603";
            if(logger.isDebugEnabled()){
                logger.debug("这个是挂断指令:{}",param);
            }
        }else{
            param = "method=sys.call.answer&res_id="+request.getParameter("res_id")+"&max_answer_seconds=5&user_data=1234";
            if(logger.isDebugEnabled()){
                logger.debug("这个是接通指令:{}",param);
            }
        }
        requestX = RPCRequest.newRequest(ServiceConstants.MN_CH_CTI_API,param);

        return requestX;
    }

}
