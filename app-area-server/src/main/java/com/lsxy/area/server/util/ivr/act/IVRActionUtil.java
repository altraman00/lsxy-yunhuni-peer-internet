package com.lsxy.area.server.util.ivr.act;

import com.lsxy.area.api.BusinessState;
import com.lsxy.area.api.BusinessStateService;
import com.lsxy.area.server.util.ivr.act.handler.ActionHandler;
import com.lsxy.framework.core.utils.JSONUtil2;
import com.lsxy.framework.core.utils.MapBuilder;
import com.lsxy.framework.rpc.api.RPCCaller;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.ServiceConstants;
import com.lsxy.framework.rpc.api.session.SessionContext;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.service.AppService;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;

/**
 * Created by liuws on 2016/9/1.
 */
@Component
public class IVRActionUtil {
    private static final Logger logger = LoggerFactory.getLogger(IVRActionUtil.class);

    private static final String APPLICATION_JSON = "application/json;charset=utf-8";

    private static final String CONTENT_TYPE_TEXT_JSON = "text/json";

    private static final int RETRY_TIMES = 3;

    private CloseableHttpAsyncClient client = null;

    //设置请求和传输超时时间
    private RequestConfig config =
            RequestConfig.custom().setSocketTimeout(30000).setConnectTimeout(30000).build();

    @Autowired
    private BusinessStateService businessStateService;

    @Autowired
    private AppService appService;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private RPCCaller rpcCaller;

    @Autowired
    private SessionContext sessionContext;

    private Map<String,ActionHandler> handlers = new HashMap<>();

    @PostConstruct
    public void init(){
        initClient();
        initHandler();
    }
    private void initClient(){
        client = HttpAsyncClients.createDefault();
        client.start();
    }
    private void initHandler(){
        Reflections reflections = new Reflections("com.lsxy.area.server.util.ivr");
        Set<Class<? extends ActionHandler>> handlerClasss = reflections.getSubTypesOf(ActionHandler.class);
        for (Class<? extends ActionHandler> handlerClass : handlerClasss) {
            ActionHandler handler = applicationContext.getBean(handlerClass);
            handlers.put(handler.getAction(),handler);
            logger.info("注册IVR动作处理器:{},{}",handler.getAction().toLowerCase(),handler);
        }
    }
    @PreDestroy
    public void destroy(){
        try {
            client.close();
        } catch (IOException e) {
            logger.info("关闭httpclient",e);
        }
    }

    /**
     * 调用接口询问是否接受该呼叫
     * @param url
     * @param from
     * @return
     */
    public boolean getAcceptRequest(final String url, final String from){
        boolean res = false;
        boolean success = false;
        int re_times = 0;
        do{
            try{
                HttpPost post = new HttpPost(url);
                Map<String,Object> data = new HashMap<>();
                data.put("action","ivr_incoming");
                data.put("from",from);
                post.setConfig(config);
                post.addHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON);
                StringEntity se = new StringEntity(JSONUtil2.objectToJson(data));
                se.setContentType(CONTENT_TYPE_TEXT_JSON);
                se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON));
                post.setEntity(se);
                Future<HttpResponse> future = client.execute(post,null);
                HttpResponse response = future.get();
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    String result = receiveResponse(response);
                    if(result!=null && result.equalsIgnoreCase("accept")){
                        res=true;
                    }
                    success = true;
                }
            }catch (Throwable t){
                logger.error("调用{}失败",url);
            }
            re_times++;
        }while (!success && re_times<=RETRY_TIMES);
        return res;
    }

    /**
     * 调用接口询问ivr第一步干嘛
     * @param url
     * @return
     */
    public String getFirstIvr(final String url){
        String res = null;
        boolean success = false;
        int re_times = 0;
        do{
            try{
                HttpPost post = new HttpPost(url);
                Map<String,Object> data = new HashMap<>();
                data.put("action","ivr_start");
                post.setConfig(config);
                post.addHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON);
                StringEntity se = new StringEntity(JSONUtil2.objectToJson(data));
                se.setContentType(CONTENT_TYPE_TEXT_JSON);
                se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON));
                post.setEntity(se);
                Future<HttpResponse> future = client.execute(post,null);
                HttpResponse response = future.get();
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    res = receiveResponse(response);
                    success = true;
                }
            }catch (Throwable t){
                logger.error("调用{}失败",url);
            }
            re_times++;
        }while (!success && re_times<=RETRY_TIMES);
        return res;
    }

    /**
     * 调用next
     * @param url
     * @return
     */
    private String getNextRequest(String url){
        String res = null;
        boolean success = false;
        int re_times = 0;
        do{
            try{
                HttpGet get = new HttpGet(url);
                get.setConfig(config);
                Future<HttpResponse> future = client.execute(get,null);
                HttpResponse response = future.get();
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    res = receiveResponse(response);
                    success = true;
                }
            }catch (Throwable t){
                logger.error("调用{}失败",url);
            }
            re_times++;
        }while (!success && re_times<=RETRY_TIMES);
        return res;
    }

    private String receiveResponse(HttpResponse response) throws IOException {
        if(response == null){
            return null;
        }
        final HttpEntity entity = response.getEntity();
        if(entity == null){
            return null;
        }
        if(entity.getContent() == null){
            return null;
        }
        return EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
    }

    /**
     * 询问是否接受，然后执行action
     * @param call_id
     * @return
     */
    public boolean doActionIfAccept(String call_id){
        BusinessState state = businessStateService.get(call_id);
        String appId = state.getAppId();
        App app = appService.findById(appId);
        Map<String,Object> businessData = state.getBusinessData();
        String from = null;
        if(businessData!=null){
            from = (String)businessData.get("from");
        }
        boolean accept = getAcceptRequest(app.getUrl(),from);
        if(!accept){
            reject(state.getAppId(),state.getResId(),call_id);
            return true;
        }
        answer(state.getAppId(),state.getResId(),call_id);
        return true;
    }

    private void reject(String appId,String res_id,String call_id){
        Map<String, Object> params = new MapBuilder<String,Object>()
                .put("res_id",res_id)
                .put("user_data",call_id)
                .put("appid",appId)
                .build();
        RPCRequest rpcrequest = RPCRequest.newRequest(ServiceConstants.MN_CH_SYS_CALL_REJECT,params);
        try {
            rpcCaller.invoke(sessionContext, rpcrequest);
        } catch (Throwable e) {
            logger.error("调用失败",e);
        }

    }

    private void answer(String appId,String res_id,String call_id){
        Map<String, Object> params = new MapBuilder<String,Object>()
                .put("res_id",res_id)
                //TODO 这个时间如何定
                .put("max_answer_seconds",3600*24)
                .put("user_data",call_id)
                .put("appid",appId)
                .build();
        RPCRequest rpcrequest = RPCRequest.newRequest(ServiceConstants.MN_CH_SYS_CALL_ANSWER,params);
        try {
            rpcCaller.invoke(sessionContext, rpcrequest);
        } catch (Throwable e) {
            logger.error("调用失败",e);
        }
    }
    public boolean doAction(String call_id){
        BusinessState state = businessStateService.get(call_id);
        if(state == null){
            logger.info("没有找到call_id={}的state",call_id);
            return false;
        }
        Map<String,Object> businessDate = state.getBusinessData();
        if(businessDate == null){
            businessDate = new HashMap<>();
        }
        Object nextUrl = businessDate.get("next");

        // is "" 代表没有next，null代表第一次
        if(nextUrl!=null && StringUtils.isBlank(nextUrl.toString())){
            logger.info("没有后续ivr动作了，call_id={}",call_id);
            return  true;
        }
        String resXML = null;
        if(nextUrl == null){//第一次
            String appId = state.getAppId();
            App app = appService.findById(appId);
            resXML = getFirstIvr(app.getUrl());
        }else{
            resXML = getNextRequest(nextUrl.toString());
        }
        if(StringUtils.isBlank(resXML)){
            return false;
        }
        ActionHandler  h = null;
        Element ele = null;
        try {
            Document doc = DocumentHelper.parseText(resXML);
            ele = doc.getRootElement();
            h = handlers.get(ele.getName().toLowerCase());
        } catch (Throwable e) {
            logger.error("处理ivr动作指令出错",e);
            return false;
        }
        if(h == null){
            logger.info("没有找到对应的ivr动作处理类");
            return false;
        }
        return h.handle(call_id,ele);
    }
}
