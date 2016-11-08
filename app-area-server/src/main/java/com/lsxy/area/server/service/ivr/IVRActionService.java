package com.lsxy.area.server.service.ivr;

import com.lsxy.area.api.BusinessState;
import com.lsxy.area.api.BusinessStateService;
import com.lsxy.area.api.exceptions.AppOffLineException;
import com.lsxy.area.server.AreaAndTelNumSelector;
import com.lsxy.area.server.service.ivr.handler.ActionHandler;
import com.lsxy.area.server.util.NotifyCallbackUtil;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.core.utils.JSONUtil2;
import com.lsxy.framework.core.utils.MapBuilder;
import com.lsxy.framework.core.utils.StringUtil;
import com.lsxy.framework.core.utils.UUIDGenerator;
import com.lsxy.framework.rpc.api.RPCCaller;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.ServiceConstants;
import com.lsxy.framework.rpc.api.session.SessionContext;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.service.AppService;
import com.lsxy.yunhuni.api.config.model.LineGateway;
import com.lsxy.yunhuni.api.config.service.LineGatewayService;
import com.lsxy.yunhuni.api.session.model.CallSession;
import com.lsxy.yunhuni.api.session.model.VoiceIvr;
import com.lsxy.yunhuni.api.session.service.CallSessionService;
import com.lsxy.yunhuni.api.session.service.VoiceIvrService;
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
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.DocumentSource;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.*;
import java.util.concurrent.Future;

/**
 * Created by liuws on 2016/9/1.
 * 解析IVR 动作指令 调用不同的处理器
 */
@Component
public class IVRActionService {
    private static final Logger logger = LoggerFactory.getLogger(IVRActionService.class);

    private static final String APPLICATION_JSON = "application/json;charset=utf-8";

    private static final String ACCEPT_TYPE_TEXT_PLAIN = "text/plain;charset=utf-8";

    private static final int RETRY_TIMES = 3;

    private CloseableHttpAsyncClient client = null;

    public static final int MAX_DURATION_SEC = 60 * 60 * 6;

    //设置请求和传输超时时间
    private RequestConfig config =
            RequestConfig.custom().setConnectionRequestTimeout(10000).setSocketTimeout(10000).setConnectTimeout(10000).build();

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

    @Autowired
    private LineGatewayService lineGatewayService;

    @Autowired
    private CallSessionService callSessionService;

    @Autowired
    private VoiceIvrService voiceIvrService;

    @Autowired
    private AreaAndTelNumSelector areaAndTelNumSelector;

    @Autowired
    private NotifyCallbackUtil notifyCallbackUtil;

    private Map<String,ActionHandler> handlers = new HashMap<>();

    private Schema schema = null;

    @PostConstruct
    public void init(){
        initClient();
        initHandler();
        initIVRSchema();
    }
    private void initIVRSchema() {
        try{
            InputStream input = IVRActionService.class.getResourceAsStream("/ivraction.xsd");
            if(input == null){
                input = IVRActionService.class.getClassLoader().getResourceAsStream("/ivraction.xsd");
            }
            SchemaFactory factory =
                    SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            this.schema = factory.newSchema(new StreamSource(input));
        }catch (Throwable t){
            throw new RuntimeException("ivraction.xsd读取失败");
        }
    }
    private void initClient(){
        client = HttpAsyncClients.createDefault();
        client.start();
    }
    private void initHandler(){
        Reflections reflections = new Reflections("com.lsxy.area.server.service.ivr");
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
    public boolean getAcceptRequest(final String url,final String call_id, final String from){
        boolean res = false;
        boolean success = false;
        int re_times = 0;
        do{
            try{
                HttpPost post = new HttpPost(url);
                Map<String,Object> data = new HashMap<>();
                data.put("action","ivr_incoming");
                data.put("from",from);
                data.put("call_id",call_id);
                post.setConfig(config);
                post.setHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON);
                StringEntity se = new StringEntity(JSONUtil2.objectToJson(data));
                post.setEntity(se);
                post.setHeader("accept",ACCEPT_TYPE_TEXT_PLAIN);
                Future<HttpResponse> future = client.execute(post,null);
                HttpResponse response = future.get();
                if(logger.isDebugEnabled()){
                    logger.info("http ivr response statue = {}",response.getStatusLine().getStatusCode());
                }
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
    public String getFirstIvr(final String call_id,final String url){
        String res = null;
        boolean success = false;
        int re_times = 0;
        do{
            try{
                HttpPost post = new HttpPost(url);
                Map<String,Object> data = new HashMap<>();
                data.put("action","ivr_start");
                data.put("call_id",call_id);
                post.setConfig(config);
                post.setHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON);
                StringEntity se = new StringEntity(JSONUtil2.objectToJson(data));
                post.setEntity(se);
                post.setHeader("accept",ACCEPT_TYPE_TEXT_PLAIN);
                Future<HttpResponse> future = client.execute(post,null);
                HttpResponse response = future.get();
                if(logger.isDebugEnabled()){
                    logger.info("http ivr response statue = {}",response.getStatusLine().getStatusCode());
                }
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

    private static String inputUrl(String url,String key,String value){
        URI uri = URI.create(url);
        String scheme = uri.getScheme();
        String host = uri.getHost();
        String port = "";
        Integer p = uri.getPort();
        String path = uri.getPath();
        String query = uri.getQuery();
        String fragment = uri.getFragment();
        if(StringUtil.isEmpty(query)){
            query = key + "=" + value;
        }else{
            query = query + "&" + key + "=" + value;
        }
        if(p != -1){
            port = ":"+p;
        }
        if(fragment == null){
            fragment = "";
        }else{
            fragment = "#" + fragment;
        }
        return scheme + "://"+host+port+path+"?"+query+fragment;
    }

    /**
     * 调用next
     * @param url
     * @return
     */
    private String getNextRequest(String call_id,String url){
        String res = null;
        boolean success = false;
        int re_times = 0;
        do{
            try{
                HttpGet get = new HttpGet(inputUrl(url,"call_id",call_id));
                get.setConfig(config);
                get.setHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON);
                get.setHeader("accept",ACCEPT_TYPE_TEXT_PLAIN);
                Future<HttpResponse> future = client.execute(get,null);
                HttpResponse response = future.get();
                if(logger.isDebugEnabled()){
                    logger.info("http ivr response statue = {}",response.getStatusLine().getStatusCode());
                }
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
        String result = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
        if(logger.isDebugEnabled()){
            logger.info("http ivr response = {}",result);
        }
        return result;
    }

    /**
     * 询问是否接受，然后执行action
     * @return
     */
    public boolean doActionIfAccept(App app, Tenant tenant,String res_id, String from, String to){
        String call_id = UUIDGenerator.uuid();
        boolean accept = getAcceptRequest(app.getUrl(),call_id,from);
        if(!accept){
            reject(app,res_id,call_id);
        }else{
            answer(app,res_id,call_id);
            saveIvrSessionCall(call_id,app,tenant,res_id,from,to);
        }
        return true;
    }

    private void saveIvrSessionCall(String call_id,App app, Tenant tenant,String res_id, String from, String to){
        Map<String, String> result;
        try {
            result = areaAndTelNumSelector.getTelnumberAndAreaId(app,to);
        } catch (AppOffLineException e) {
            throw new RuntimeException(e);
        }
        String areaId = result.get("areaId");
        String oneTelnumber = result.get("oneTelnumber");

        LineGateway lineGateway = lineGatewayService.getBestLineGatewayByNumber(oneTelnumber);
        //保存业务数据，后续事件要用到
        BusinessState state = new BusinessState.Builder()
                .setTenantId(tenant.getId())
                .setAppId(app.getId())
                .setId(call_id)
                .setResId(res_id)
                .setType("ivr_incoming")
                .setAreaId(areaId)
                .setLineGatewayId(lineGateway.getId())
                .setBusinessData(new MapBuilder<String,Object>()
                        //incoming事件from 和 to是相反的
                        .put("from",to)
                        .put("to",from)
                        .build())
                .build();
        CallSession callSession = new CallSession();
        callSession.setStatus(CallSession.STATUS_CALLING);
        callSession.setFromNum(to);
        callSession.setToNum(from);
        callSession.setApp(app);
        callSession.setTenant(tenant);
        callSession.setRelevanceId(call_id);
        callSession.setType(CallSession.TYPE_VOICE_IVR);
        callSession.setResId(state.getResId());
        callSession = callSessionService.save(callSession);
        Map<String,Object> businessData = state.getBusinessData();
        if(businessData == null){
            businessData = new HashMap<>();
            state.setBusinessData(businessData);
        }
        businessData.put("sessionid",callSession.getId());
        businessStateService.save(state);
        VoiceIvr voiceIvr = new VoiceIvr();
        voiceIvr.setId(call_id);
        voiceIvr.setFromNum(from);
        voiceIvr.setToNum(to);
        voiceIvr.setStartTime(new Date());
        voiceIvr.setIvrType(VoiceIvr.IVR_TYPE_INCOMING);
        voiceIvrService.save(voiceIvr);
    }

    private void reject(App app,String res_id,String call_id){
        String areaId = areaAndTelNumSelector.getAreaId(app);
        Map<String, Object> params = new MapBuilder<String,Object>()
                .put("res_id",res_id)
                .put("user_data",call_id)
                .put("areaId",areaId)
                .build();
        RPCRequest rpcrequest = RPCRequest.newRequest(ServiceConstants.MN_CH_SYS_CALL_REJECT,params);
        try {
            rpcCaller.invoke(sessionContext, rpcrequest);
        } catch (Throwable e) {
            logger.error("调用失败",e);
        }

    }

    private void answer(App app,String res_id,String call_id){
        String areaId = areaAndTelNumSelector.getAreaId(app);
        Map<String, Object> params = new MapBuilder<String,Object>()
                .put("res_id",res_id)
                .put("max_answer_seconds",MAX_DURATION_SEC)
                .put("user_data",call_id)
                .put("areaId",areaId)
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

        if(state.getClosed()){
            logger.info("IVR呼叫已关闭，call_id={}",call_id);
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
            hangup(state.getResId(),call_id,state.getAreaId());
            return  false;
        }
        String resXML = null;
        if(nextUrl == null){//第一次
            String appId = state.getAppId();
            App app = appService.findById(appId);
            if(app == null){
                logger.error("ivr 找不到对应的app");
                return false;
            }
            resXML = getFirstIvr(call_id,app.getUrl());
        }else{
            resXML = getNextRequest(call_id,nextUrl.toString());
        }
        if(StringUtils.isBlank(resXML)){
            return false;
        }
        ActionHandler  h = null;
        Element root = null;
        Element actionEle = null;
        try {
            Document doc = DocumentHelper.parseText(resXML);
            if(validateXMLSchema(doc)){

            }
            root = doc.getRootElement();
            actionEle = getActionEle(root);
            h = handlers.get(actionEle.getName().toLowerCase());
            if(h == null){
                logger.info("没有找到对应的ivr动作处理类");
                return false;
            }
            return h.handle(call_id,actionEle,getNextUrl(root));
        } catch(DocumentException | IllegalArgumentException e){
            logger.error("处理ivr动作指令出错,appID="+state.getAppId(),e);
            //发送ivr格式错误通知
            String appId = state.getAppId();
            App app = appService.findById(appId);
            if(app == null){
                logger.error("ivr 找不到对应的app");
                return false;
            }
            Map<String,Object> notify_data = new MapBuilder<String,Object>()
                    .putIfNotEmpty("event","ivr.format_error")
                    .putIfNotEmpty("id",call_id)
                    .putIfNotEmpty("user_data",state.getUserdata())
                    .build();
            notifyCallbackUtil.postNotify(app.getUrl(),notify_data,3);
            hangup(state.getResId(),call_id,state.getAreaId());
            return false;
        } catch (Throwable e) {
            logger.error("处理ivr动作指令出错,appID="+state.getAppId(),e);
            return false;
        }
    }

    private void hangup(String res_id,String call_id,String area_id){
        Map<String, Object> params = new MapBuilder<String,Object>()
                .putIfNotEmpty("res_id",res_id)
                .putIfNotEmpty("user_data",call_id)
                .put("areaId",area_id)
                .build();
        RPCRequest rpcrequest = RPCRequest.newRequest(ServiceConstants.MN_CH_SYS_CALL_DROP, params);
        try {
            rpcCaller.invoke(sessionContext, rpcrequest);
        } catch (Throwable e) {
            logger.error("调用失败",e);
        }
    }
    /**
     * 返回对应的ivr根元素
     * @param root
     * @return
     */
    private Element getActionEle(Element root) {
        List elements = root.elements();
        Element actionEle = null;
        for (Object obj : elements) {
            Element ele = (Element)obj;
            boolean hasHandler = handlers.get(ele.getName().toLowerCase()) != null;
            if(hasHandler){
                actionEle = ele;
                break;
            }
        }
        return actionEle;
    }

    /**
     * 获取next节点的值
     * @param root
     * @return
     */
    private String getNextUrl(Element root){
        String next = root.elementTextTrim("next");
        if(StringUtils.isBlank(next)){
            next = "";
        }
        return next;
    }

    public boolean validateXMLSchema(Document doc){
        try {
            Validator validator = schema.newValidator();
            validator.validate(new DocumentSource(doc));
        } catch (Throwable e) {
            throw new IllegalArgumentException(e);
        }
        return true;
    }
}
