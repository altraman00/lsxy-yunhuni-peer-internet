package com.lsxy.area.server;

import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.RPCResponse;
import com.lsxy.framework.rpc.api.ServiceConstants;
import com.lsxy.framework.rpc.api.handler.HandlerManager;
import com.lsxy.framework.rpc.api.server.AbstractServiceHandler;
import com.lsxy.framework.rpc.api.server.ServerSessionContext;
import com.lsxy.framework.rpc.api.server.Session;
import com.lsxy.yunhuni.api.config.model.Area;
import com.lsxy.yunhuni.api.config.service.AreaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 * Created by tandy on 16/8/8.
 */
@Component
public class AreaServerServiceHandler extends AbstractServiceHandler {


    private static final Logger logger = LoggerFactory.getLogger(AreaServerServiceHandler.class);

    @Autowired(required = false)
    private StasticsCounter sc;

    @Autowired
    private AreaService areaService;

    @Autowired
    private ServerSessionContext sessionContext;

    @Autowired
    private HandlerManager handlerManager;

    @Override
    public RPCResponse handleService(RPCRequest request, Session session) {

        if(sc != null) sc.getReceivedAreaNodeRequestCount().incrementAndGet();

        RPCResponse response = handlerManager.fire(request,session);

        return response;
    }

    /**
     * 判断指定的区域和节点编号的连接是否有效
     * @param areaid 区域标识
     * @param nodeid  节点编号
     * @return
     */
    @Override
    public boolean isConnectAvalid(String areaid, String nodeid) {
        boolean result = false;
        Area area = areaService.findById(areaid);
        if(area!=null){
            if(!area.getEnabled()){
                logger.error("区域[{}]未启用,连接失败",areaid);
            }else{
                result = true;
            }
        }else{
            logger.error("非法连接,拒绝 : {}-{}  ",areaid,nodeid);
        }
        return result;
    }

//    /**
//     * 处理区域连接注册指令
//     *
//     * @param request   解析出来的请求对象  params={arid=?&nid=?}
//     * @param session  RPC SESSION
//     * @return
//     */
//    @Override
//    public RPCResponse process_CH_MN_CONNECT(String areaid,String nodeid,RPCRequest request) {
//        InetSocketAddress add = session.getRemoteAddress();
//        InetAddress addrs = add.getAddress();
//        String ip = addrs.getHostAddress();
////        String ip = session.getRemoteAddress().getAddress().getHostAddress();
//        String blankipList = SystemConfig.getProperty("area.server.blank.iplist","");
//        String blankipEnabled = SystemConfig.getProperty("area.server.blank.iplist.enabled","false");
//        RPCResponse response = RPCResponse.buildResponse(request);
//        response.setMessage(RPCResponse.STATE_EXCEPTION);
//
//        //如果启用了白名单机制,并且非法连接
//        if("true".equals(blankipEnabled) && !(blankipList.indexOf(ip)>=0)){
//            logger.error("白名单机制启用,连接ip["+ip+"]不在白名单中["+blankipList+"],拒绝连接");
//            response.setBody("非法连接,被拒绝");
//            return response;
//        }
//
//        if(StringUtil.isNotEmpty(areaid)){
//
//            //判断是否重复连接
//            if(sessionContext.getSessionByArea(areaid,nodeid) != null){
//                logger.error("节点尝试重复连接["+areaid+"]["+nodeid+"],被拒绝!");
//                response.setBody("节点尝试重复连接["+areaid+"]["+nodeid+"],被拒绝!");
//                return response;
//            }
//
//            Area area = areaService.findById(areaid);
//            if(area!=null){
//                if(area.isEnabled()){
//                    logger.error("区域[{}]未启用,连接失败",areaid);
//                    response.setMessage(RPCResponse.STATE_EXCEPTION);
//                    response.setBody("区域["+areaid+"]未启用,连接被拒绝!!");
//                }else{
//                    sessionContext.putSession(areaid,nodeid,session);
//                    response.setMessage(RPCResponse.STATE_OK);
//                }
//            }else{
//                response.setBody("非法区域标识:"+areaid+",连接被拒绝");
//            }
//        }else{
//            response.setBody("缺少区域标识参数,连接被拒绝");
//        }
//        return response;
//    }


//    private RPCResponse process_MN_CH_TEST_ECHO(RPCRequest request, Session session) {
//        RPCResponse response = RPCResponse.buildResponse(request);
//        response.setTimestamp(request.getTimestamp());
//        return response;
//    }

    /**
     * 处理CH_MN_CTI_EVENT CTI事件监听
     * 有CTI事件触发时,需要将事件告诉用户,让用户告知接下来如何处理
     * @param request
     * @param session
     * @return
     */
//    private RPCResponse process_CH_MN_CTI_EVENT(RPCRequest request, Session session) {
//
//
//
//        /*临时逻辑,如果收到是 incoming 事件  则概率性的挂断或接通
//        sys.call.on_dial_completed
//        sys.call.on_incoming
//        sys.call.on_released
//        */
//        String method = (String) request.getParameter("method");
//        if(logger.isDebugEnabled()){
//            logger.debug("收到CTI事件:{}-----{}" , method,request.getParamMap());
//        }
//
//        if(method.equals("sys.call.on_incoming")){
//            tzb.receivedIncoming(request);
//            if(sc != null)  sc.getReceivedAreaNodeInComingEventCount().incrementAndGet();
//            if(logger.isDebugEnabled()){
//                logger.debug("<<<<<<INCOMING>>>>>>>");
//            }
//            RPCRequest sendRequest = randomRequest(request);
//            try {
//                if(logger.isDebugEnabled()){
//                    logger.debug(">>>>>>>>发送指令到CTI:{}",sendRequest);
//                }
//
//                if(sendRequest!=null){
//                      /*发送给区域的请求次数计数*/
//                    if(sc!=null) sc.getSendAreaNodeRequestCount().incrementAndGet();
//                    rpcCaller.invoke(sessionContext,sendRequest);
//                }
//
//            } catch (Exception e) {
//                logger.error("发送区域的指令出现异常,指令发送失败:{}",sendRequest);
//            }
//        }else{
////            if(logger.isDebugEnabled()){
////                logger.debug("不是 INCOMING.........");
////            }
//        }
//        return null;
//    }

    /**
     * 随机创建拒绝或挂断请求
     * @param request
     * @return
     */
//    private RPCRequest randomRequest(RPCRequest request) {
////        if(logger.isDebugEnabled()){
////            logger.debug("请求远程接口参数:{}",request.getParamMap());
////        }
////
////        RestResponse<String> response = RestRequest.buildRequest().post("http://101.200.73.13:3000/incoming",request.getParamMap());
////
////        RPCRequest requestX = null;
////        if(response != null && response.isSuccess()){
////            String param = response.getData();
////            if(sc != null){
////                /*接听 挂机  次数计数*/
////                if(param.indexOf("sys.call.answer")>=0){
////                    if(sc != null)  sc.getSendAreaNodeSysAnswerCount().incrementAndGet();
////                }else if(param.indexOf("sys.call.drop")>=0){
////                    if(sc != null)  sc.getSendAreaNodeSysDropCount().incrementAndGet();
////                }
////            }
////            requestX = RPCRequest.newRequest(ServiceConstants.MN_CH_CTI_API,param);
////        }else {
////            logger.error("请求用户接口发生异常,用户接口没有READY!!!!!!!!");
////        }
//
//
////        int radom = RandomUtils.nextInt(0,100);
////        String  param = null;
//////        if(radom % 2 == 0){
//////            param = "method=sys.call.drop&res_id="+request.getParameter("res_id")+"&cause=603";
//////            if(logger.isDebugEnabled()){
//////                logger.debug("这个是挂断指令:{}",param);
//////            }
//////        }else{
//////            param = "method=sys.call.answer&res_id="+request.getParameter("res_id")+"&max_answer_seconds=5&user_data=1234";
//////            if(logger.isDebugEnabled()){
//////                logger.debug("这个是接通指令:{}",param);
//////            }
//////        }
//        int ansersec = RandomUtils.nextInt(2,20);
//       String   param = "method=sys.call.answer&res_id="+request.getParameter("res_id")+"&max_answer_seconds="+ansersec+"&user_data=1234";
//        /*挂机指令计数*/
//        if(sc != null) sc.getSendAreaNodeSysAnswerCount().incrementAndGet();
//        RPCRequest requestX = RPCRequest.newRequest(ServiceConstants.MN_CH_CTI_API,param);
//
//        return requestX;
//    }

//    public static void main(String[] args) {
//        String url = "http://101.200.73.13:3000/incoming";
//        RestRequest request = RestRequest.buildRequest();
//        RestResponse<String> response = request.get(url,String.class);
//        System.out.println(response.getData());
//
//    }
}
