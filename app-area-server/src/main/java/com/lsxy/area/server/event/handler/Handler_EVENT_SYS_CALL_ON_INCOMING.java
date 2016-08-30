package com.lsxy.area.server.event.handler;

import com.lsxy.area.server.StasticsCounter;
import com.lsxy.area.server.event.EventHandler;
import com.lsxy.area.server.test.TestIncomingZB;
import com.lsxy.framework.rpc.api.*;
import com.lsxy.framework.rpc.api.event.Constants;
import com.lsxy.framework.rpc.api.server.Session;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by liuws on 2016/8/29.
 */
@Component
public class Handler_EVENT_SYS_CALL_ON_INCOMING extends EventHandler{

    private static final Logger logger = LoggerFactory.getLogger(Handler_EVENT_SYS_CALL_ON_INCOMING.class);

    @Autowired(required = false)
    private StasticsCounter sc;

    @Autowired
    private RPCCaller rpcCaller;

    @Autowired(required = false)
    private TestIncomingZB tzb;

    @Autowired
    private SessionContext sessionContext;

    @Override
    public String getEventName() {
        return Constants.EVENT_SYS_CALL_ON_INCOMING;
    }

    @Override
    public RPCResponse handle(RPCRequest request, Session session) {
        tzb.receivedIncoming(request);
        if(sc != null)  sc.getReceivedAreaNodeInComingEventCount().incrementAndGet();
        if(logger.isDebugEnabled()){
            logger.debug("<<<<<<INCOMING>>>>>>>");
        }
        RPCRequest sendRequest = randomRequest(request);
        try {
            if(logger.isDebugEnabled()){
                logger.debug(">>>>>>>>发送指令到CTI:{}",sendRequest);
            }

            if(sendRequest!=null){
                      /*发送给区域的请求次数计数*/
                if(sc!=null) sc.getSendAreaNodeRequestCount().incrementAndGet();
                rpcCaller.invoke(sessionContext,sendRequest);
            }

        } catch (Exception e) {
            logger.error("发送区域的指令出现异常,指令发送失败:{}",sendRequest);
        }
        return null;
    }

    /**
     * 随机创建拒绝或挂断请求
     * @param request
     * @return
     */
    private RPCRequest randomRequest(RPCRequest request) {
//        if(logger.isDebugEnabled()){
//            logger.debug("请求远程接口参数:{}",request.getParamMap());
//        }
//
//        RestResponse<String> response = RestRequest.buildRequest().post("http://101.200.73.13:3000/incoming",request.getParamMap());
//
//        RPCRequest requestX = null;
//        if(response != null && response.isSuccess()){
//            String param = response.getData();
//            if(sc != null){
//                /*接听 挂机  次数计数*/
//                if(param.indexOf("sys.call.answer")>=0){
//                    if(sc != null)  sc.getSendAreaNodeSysAnswerCount().incrementAndGet();
//                }else if(param.indexOf("sys.call.drop")>=0){
//                    if(sc != null)  sc.getSendAreaNodeSysDropCount().incrementAndGet();
//                }
//            }
//            requestX = RPCRequest.newRequest(ServiceConstants.MN_CH_CTI_API,param);
//        }else {
//            logger.error("请求用户接口发生异常,用户接口没有READY!!!!!!!!");
//        }


//        int radom = RandomUtils.nextInt(0,100);
//        String  param = null;
////        if(radom % 2 == 0){
////            param = "method=sys.call.drop&res_id="+request.getParameter("res_id")+"&cause=603";
////            if(logger.isDebugEnabled()){
////                logger.debug("这个是挂断指令:{}",param);
////            }
////        }else{
////            param = "method=sys.call.answer&res_id="+request.getParameter("res_id")+"&max_answer_seconds=5&user_data=1234";
////            if(logger.isDebugEnabled()){
////                logger.debug("这个是接通指令:{}",param);
////            }
////        }
        int ansersec = RandomUtils.nextInt(2,20);
        String   param = "method=sys.call.answer&res_id="+request.getParameter("res_id")+"&max_answer_seconds="+ansersec+"&user_data=1234";
        /*挂机指令计数*/
        if(sc != null) sc.getSendAreaNodeSysAnswerCount().incrementAndGet();
        RPCRequest requestX = RPCRequest.newRequest(ServiceConstants.MN_CH_CTI_API,param);

        return requestX;
    }
}
