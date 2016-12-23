package com.lsxy.area.agent.handler;

import com.lsxy.area.agent.StasticsCounter;
import com.lsxy.area.agent.cti.CTIClientContext;
import com.lsxy.area.agent.cti.CTINode;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.RPCResponse;
import com.lsxy.framework.rpc.api.ServiceConstants;
import com.lsxy.framework.rpc.api.handler.RpcRequestHandler;
import com.lsxy.framework.rpc.api.session.Session;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liuws on 2016/8/27.
 */
@Component
public class Handler_MN_CH_CTI_API extends RpcRequestHandler{

    private static final Logger logger = LoggerFactory.getLogger(Handler_MN_CH_CTI_API.class);

    @Value("${area.agent.client.cti.sip.host}")
    private String ctiHost;

    @Value("${area.agent.client.cti.sip.port}")
    private int ctiPort;

    @Autowired
    private CTIClientContext cticlientContext;

    @Autowired(required = false)
    private StasticsCounter sc;

    @Override
    public String getEventName() {
        return ServiceConstants.MN_CH_CTI_API;
    }

    @Override
    public RPCResponse handle(RPCRequest request, Session session) {
        if(logger.isDebugEnabled()){
            logger.debug("响应CTI API:{}",request);
        }

        String resId = (String) request.getParameter("res_id");
        String method = (String) request.getParameter("method");

        Map<String, Object> params = new HashMap<>();
        if(method.equals("sys.call.answer")){
            /*收到应答指令次数计数*/
            if(sc!=null) sc.getReceivedCTIAnswerCount().incrementAndGet();

            params.put("max_answer_seconds", RandomUtils.nextInt(10,60));
            if(logger.isDebugEnabled()){
                logger.debug("处理应答参数:{}",params);
            }
        }else if(method.equals("sys.call.drop")){
            /*收到挂机指令次数计数*/
            if(sc!=null) sc.getReceivedCTIDropCount().incrementAndGet();

            params.put("cause",603);
            if(logger.isDebugEnabled()){
                logger.debug("处理挂机动作的参数:{}",params);
            }
        }
        try {
            CTINode cticlient = cticlientContext.getAvalibleNode(resId);
            if(logger.isDebugEnabled()){
                logger.debug("开始操作资源:{}{}",method,resId);
            }

            cticlient.operateResource(resId,method,params,null);

            /*发送请求给CTI次数计数*/
            if(sc!=null) sc.getSendCTIRequestCount().incrementAndGet();
        } catch (Exception e) {
            logger.error("操作CTI资源异常{}",request,e);
        }
        return null;
    }
}
