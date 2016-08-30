package com.lsxy.area.server.handler;

import com.lsxy.area.api.BusinessState;
import com.lsxy.area.api.BusinessStateService;
import com.lsxy.area.server.StasticsCounter;
import com.lsxy.area.server.test.TestIncomingZB;
import com.lsxy.framework.rpc.api.*;
import com.lsxy.framework.rpc.api.handler.RpcRequestHandler;
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
public class Handler_CH_MN_CTI_EVENT extends RpcRequestHandler{

    private static final Logger logger = LoggerFactory.getLogger(Handler_CH_MN_CTI_EVENT.class);

    @Autowired(required = false)
    private StasticsCounter sc;

    @Autowired
    private RPCCaller rpcCaller;

    @Autowired(required = false)
    private TestIncomingZB tzb;

    @Autowired
    private SessionContext sessionContext;

    @Autowired
    private BusinessStateService businessStateService;

    @Override
    public String getEventName() {
        return ServiceConstants.CH_MN_CTI_EVENT;
    }

    @Override
    public RPCResponse handle(RPCRequest request, Session session) {
        /*临时逻辑,如果收到是 incoming 事件  则概率性的挂断或接通
        sys.call.on_dial_completed
        sys.call.on_incoming
        sys.call.on_released
        */
        String method = (String) request.getParameter("method");
        if(logger.isDebugEnabled()){
            logger.debug("收到CTI事件:{}-----{}" , method,request.getParamMap());
        }

        if(method.equals("sys.call.on_incoming")){
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
        }else if(method.equals(ServiceConstants.MN_CH_SYS_CONF_ON_START)){
            String conf_id = (String)request.getParamMap().get("user_data");
            BusinessState state = businessStateService.get(conf_id);
            logger.info("confi_id={},state={}",conf_id,state);
            logger.info("会议创建成功事件");
        }else if(method.equals("sys.conf.on_released")){//会议结束事件
            String conf_id = (String)request.getParamMap().get("user_data");
            BusinessState state = businessStateService.get(conf_id);
            logger.info("confi_id={},state={}",conf_id,state);
            logger.info("会议结束事件");
        }else{
//            if(logger.isDebugEnabled()){
//                logger.debug("不是 INCOMING.........");
//            }
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
