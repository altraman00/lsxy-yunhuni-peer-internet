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
import org.apache.commons.lang3.RandomUtils;
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

    @Autowired
    private StasticsCounter sc;

    @Override
    public RPCResponse handleService(RPCRequest request, Session session) {
        if(logger.isDebugEnabled()){
            logger.debug("收到请求:{}",request );
        }

        /*收到区域管理器请求次数计数*/
        sc.getReceivedAreaServerRequestCount().incrementAndGet();

        RPCResponse response = null;

        if(request.getName().equals(ServiceConstants.MN_CH_SYS_CALL)){
            response = this.process_MN_CH_SYS_CALL(request);
        }

        if(request.getName().equals(ServiceConstants.MN_CH_CTI_API)){
            response = this.process_MN_CH_CTI_API(request);
        }
        if(request.getName().equals(ServiceConstants.MN_CH_TEST_STASTICS_RESET)){
            response = this.process_MN_CH_TEST_STASTICS_RESET(request);
        }


        if(logger.isDebugEnabled()){
            logger.debug("返回给区域管理器的对象:{}",response);
        }

        return response;
    }

    /**
     * 测试使用  用于重置计数器
     * @param request
     * @return
     */
    private RPCResponse process_MN_CH_TEST_STASTICS_RESET(RPCRequest request) {
        if(logger.isDebugEnabled()){
            logger.debug("重置计数器");
        }
        sc.reset();
        return null;
    }

    private RPCResponse process_MN_CH_CTI_API(RPCRequest request) {
        if(logger.isDebugEnabled()){
            logger.debug("响应CTI API:{}",request);
        }
        Client cticlient = cticlientContext.getAvalibleClient();
        String resId = (String) request.getParameter("res_id");
        String method = (String) request.getParameter("method");
        Map<String, Object> params = new HashMap<>();
        if(method.equals("sys.call.answer")){
            /*收到应答指令次数计数*/
            sc.getReceivedCTIAnswerCount().incrementAndGet();

            params.put("max_answer_seconds",RandomUtils.nextInt(10,60));
            if(logger.isDebugEnabled()){
                logger.debug("处理应答参数:{}",params);
            }
        }else if(method.equals("sys.call.drop")){
            /*收到挂机指令次数计数*/
            sc.getReceivedCTIDropCount().incrementAndGet();

            params.put("cause",603);
            if(logger.isDebugEnabled()){
                logger.debug("处理挂机动作的参数:{}",params);
            }
        }
        try {
            if(logger.isDebugEnabled()){
                logger.debug("开始操作资源:{}{}",method,resId);
            }

            cticlient.operateResource(0,1,resId,method,params,null);

            /*发送请求给CTI次数计数*/
            sc.getSendCTIRequestCount().incrementAndGet();
        } catch (IOException e) {
            logger.error("操作CTI资源异常{}",request);
            e.printStackTrace();
        }
        return null;
    }

    private RPCResponse process_MN_CH_SYS_CALL(RPCRequest request) {
        if(logger.isDebugEnabled()){
            logger.debug("handler process_MN_CH_SYS_CALL:{}",request);
        }
        RPCResponse response = RPCResponse.buildResponse(request);

        String to = (String) request.getParameter("to");
        String maxAnswerSec = (String) request.getParameter("maxAnswerSec");
        String maxRingSec = (String) request.getParameter("maxRingSec");

        assert  to != null;
        assert maxAnswerSec != null;
        assert maxRingSec!=null;

        Integer iMaxAnswerSec = Integer.parseInt(maxAnswerSec);
        Integer iMaxRingSec = Integer.parseInt(maxRingSec);

        Client cticlient = cticlientContext.getAvalibleClient();
        if(cticlient == null) {
            response.setMessage(RPCResponse.STATE_EXCEPTION);
            return response;
        }
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("from_uri", "");
            params.put("to_uri", request.getParameter("to")+"@192.168.2.100:5062");
            params.put("max_answer_seconds", iMaxAnswerSec);
            params.put("max_ring_seconds", iMaxRingSec);

            if(logger.isDebugEnabled()){
                logger.debug("呼叫API调用参数:{}",params);
            }

            cticlient.createResource(0, 0, "sys.call", params, null);

            /*给CTI发送请求计数*/
            sc.getSendCTIRequestCount().incrementAndGet();

            response.setMessage(RPCResponse.STATE_OK);
        } catch (IOException e) {
            response.setMessage(RPCResponse.STATE_EXCEPTION);
            e.printStackTrace();
        }
        return response;
    }
}
