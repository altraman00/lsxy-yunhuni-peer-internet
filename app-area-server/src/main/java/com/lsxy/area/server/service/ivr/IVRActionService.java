package com.lsxy.area.server.service.ivr;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsxy.area.api.BusinessState;
import com.lsxy.area.api.BusinessStateService;
import com.lsxy.area.server.AreaAndTelNumSelector;
import com.lsxy.area.server.batch.CallCenterBatchInserter;
import com.lsxy.area.server.batch.CallSessionBatchInserter;
import com.lsxy.area.server.batch.VoiceIvrBatchInserter;
import com.lsxy.area.server.service.callcenter.CallCenterUtil;
import com.lsxy.area.server.service.callcenter.ConversationService;
import com.lsxy.area.server.service.ivr.handler.ActionHandler;
import com.lsxy.area.server.service.ivr.handler.EnqueueHandler;
import com.lsxy.area.server.service.ivr.handler.HangupActionHandler;
import com.lsxy.area.server.util.HttpClientHelper;
import com.lsxy.area.server.util.NotifyCallbackUtil;
import com.lsxy.call.center.api.model.CallCenter;
import com.lsxy.call.center.api.service.CallCenterService;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.core.utils.*;
import com.lsxy.framework.rpc.api.RPCCaller;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.ServiceConstants;
import com.lsxy.framework.rpc.api.session.SessionContext;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.service.AppService;
import com.lsxy.yunhuni.api.config.service.LineGatewayService;
import com.lsxy.yunhuni.api.session.model.CallSession;
import com.lsxy.yunhuni.api.session.model.VoiceIvr;
import com.lsxy.yunhuni.api.session.service.CallSessionService;
import com.lsxy.yunhuni.api.session.service.VoiceIvrService;
import com.lsxy.yunhuni.api.statistics.model.CallCenterStatistics;
import com.lsxy.yunhuni.api.statistics.service.CallCenterStatisticsService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
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
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.*;

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

    public static final int MAX_DURATION_SEC = 60 * 60 * 6;

    /**等待应答标记**/
    public static final String IVR_ANSWER_WAITTING_FIELD = "IVR_ANSWER_WAITTING";

    /**IVR呼入执行ivr动作前，会自动应答，所以保存ivr动作xml一次，应答后自动执行动作**/
    public static final String IVR_ANSWER_AFTER_XML_FIELD = "IVR_ANSWER_AFTER_XML";

    /**IVR当前执行的action**/
    public static final String IVR_ACTION_FIELD ="IVR_ACTION";

    /**IVR下一步的url**/
    public static final String IVR_NEXT_FIELD = "IVR_NEXT";

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

    @Autowired
    private ConversationService conversationService;

    @Reference(lazy = true,check = false,timeout = 3000)
    private CallCenterService callCenterService;

    @Autowired
    private CallCenterStatisticsService callCenterStatisticsService;

    @Autowired
    private CallSessionBatchInserter callSessionBatchInserter;

    @Autowired
    private CallCenterBatchInserter callCenterBatchInserter;

    @Autowired
    private VoiceIvrBatchInserter voiceIvrBatchInserter;

    private CloseableHttpAsyncClient client = null;

    private ExecutorService worker = null;

    private Map<String,ActionHandler> handlers = new HashMap<>();

    private Schema schema = null;

    @PostConstruct
    public void init(){
        initClient();
        initHandler();
        initIVRSchema();
        initWorker();
    }

    private void initWorker(){
        //初始化100个线程，最多1000个线程，最多积压1024个任务
        worker = new ThreadPoolExecutor(100, 1000, 60L, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(1024),
                new BasicThreadFactory.Builder().namingPattern("ivr-action-%s").build());
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
        client = HttpAsyncClientBuilder.create()
                .setDefaultRequestConfig(
                        RequestConfig.custom()
                                .setConnectionRequestTimeout(5000)
                                .setSocketTimeout(5000)
                                .setConnectTimeout(5000).build())
                //总共最多1000并发
                .setMaxConnTotal(3000)
                //每个host最多300并发
                .setMaxConnPerRoute(1000)
                //禁用cookies
                .disableCookieManagement()
                .setSSLContext(HttpClientHelper.sslContext)
                .setSSLHostnameVerifier(HttpClientHelper.hostnameVerifier)//设置https无证书访问
                .build();
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
     * 调用接口询问ivr第一步干嘛
     * @param url
     * @return
     */
    public String getFirstIvr(final String call_id,final String url,String from){
        String res = null;
        boolean success = false;
        int re_times = 0;
        do{
            long start = System.currentTimeMillis();
            try{
                HttpPost post = new HttpPost(url);
                Map<String,Object> data = new MapBuilder<String,Object>()
                        .putIfNotEmpty("action","ivr_start")
                        .putIfNotEmpty("call_id",call_id)
                        .putIfNotEmpty("from",from)
                        .build();
                post.setHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON);
                StringEntity se = new StringEntity(JSONUtil2.objectToJson(data));
                post.setEntity(se);
                post.setHeader("accept",ACCEPT_TYPE_TEXT_PLAIN);
                Future<HttpResponse> future = client.execute(post,null);
                HttpResponse response = future.get();
                if(logger.isDebugEnabled()){
                    logger.info("url={},status={}"
                            ,url,response.getStatusLine().getStatusCode());
                }
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    res = receiveResponse(response);
                    success = true;
                }
                if(logger.isDebugEnabled()){
                    logger.info("url={},耗时:{}ms",url,(System.currentTimeMillis() - start));
                }
            }catch (Throwable t){
                logger.error("调用{}失败,耗时={},error={}",url,(System.currentTimeMillis() - start),t);
            }
            re_times++;
        }while (!success && re_times<=RETRY_TIMES);
        return res;
    }

    private static String inputUrl(String url,String key,String value) throws UnsupportedEncodingException {
        URI uri = URI.create(url);
        String scheme = uri.getScheme();
        String host = uri.getHost();
        String port = "";
        Integer p = uri.getPort();
        String path = uri.getPath();
        String query = uri.getQuery();
        String fragment = uri.getFragment();
        if(StringUtil.isEmpty(query)){
            query = key + "=" + URLEncoder.encode(value,"UTF-8");
        }else{
            query = query + "&" + key + "=" + URLEncoder.encode(value,"UTF-8");
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
    private String getNextRequest(String call_id,String url,String prevAction,Map<String,Object> prevResults){
        String res = null;
        boolean success = false;
        int re_times = 0;
        do{
            long start = System.currentTimeMillis();
            try{
                String target = inputUrl(url,"call_id",call_id);
                if(prevAction != null){
                    target = inputUrl(target,"type",prevAction);
                }
                if(prevResults != null && prevResults.size()>0){
                    Set<Map.Entry<String,Object>> entries = prevResults.entrySet();
                    for (Map.Entry<String,Object> entry : entries){
                        String key = entry.getKey();
                        String value = (String)entry.getValue();
                        if(value!=null){
                            target = inputUrl(target,key,value);
                        }
                    }
                }
                HttpGet get = new HttpGet(target);
                get.setHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON);
                get.setHeader("accept",ACCEPT_TYPE_TEXT_PLAIN);
                Future<HttpResponse> future = client.execute(get,null);
                HttpResponse response = future.get();
                if(logger.isDebugEnabled()){
                    logger.info("url={},status={}"
                            ,url,response.getStatusLine().getStatusCode());
                }
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    res = receiveResponse(response);
                    success = true;
                }
                if(logger.isDebugEnabled()){
                    logger.info("url={},耗时:{}ms",url,(System.currentTimeMillis() - start));
                }
            }catch (Throwable t){
                logger.error("调用{}失败,耗时={},error={}",url,(System.currentTimeMillis() - start),t);
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
    public boolean doActionIfAccept(App app, Tenant tenant,String res_id,
                                    String from, String to,String lineId,boolean iscc){
        String call_id = UUIDGenerator.uuid();
        saveIvrSessionCall(call_id,app,tenant,res_id,from,to,lineId,iscc);
        doAction(call_id,null);
        return true;
    }

    private void saveIvrSessionCall(String call_id, App app, Tenant tenant, String res_id, String from, String to, String lineId, boolean iscc){
        CallSession callSession = new CallSession();
        callSession.setId(UUIDGenerator.uuid());
        try{
            callSession.setStatus(CallSession.STATUS_CALLING);
            callSession.setFromNum(to);
            callSession.setToNum(from);
            callSession.setAppId(app.getId());
            callSession.setTenantId(app.getTenant().getId());
            callSession.setRelevanceId(call_id);
            callSession.setResId(res_id);
            callSession.setType(iscc ? CallSession.TYPE_CALL_CENTER:CallSession.TYPE_VOICE_IVR);
            callSessionBatchInserter.put(callSession);
            if(iscc){
                CallCenter callCenter = new CallCenter();
                callCenter.setId(call_id);
                callCenter.setTenantId(tenant.getId());
                callCenter.setAppId(app.getId());
                callCenter.setFromNum(from);
                callCenter.setToNum(to);
                callCenter.setStartTime(new Date());
                callCenter.setType(""+CallCenter.CALL_IN);
                callCenter.setCost(BigDecimal.ZERO);
                callCenterBatchInserter.put(callCenter);
                try{
                    callCenterStatisticsService.incrIntoRedis(new CallCenterStatistics.Builder(tenant.getId(),app.getId(),
                            new Date()).setCallIn(1L).build());
                }catch (Throwable t){
                    logger.error("incrIntoRedis失败",t);
                }
            }else{
                VoiceIvr voiceIvr = new VoiceIvr();
                voiceIvr.setId(call_id);
                voiceIvr.setAppId(app.getId());
                voiceIvr.setTenantId(app.getTenant().getId());
                voiceIvr.setFromNum(from);
                voiceIvr.setToNum(to);
                voiceIvr.setStartTime(new Date());
                voiceIvr.setIvrType(VoiceIvr.IVR_TYPE_INCOMING);
                voiceIvrBatchInserter.put(voiceIvr);
            }
        }catch (Throwable t){
            logger.error("保存callsession失败",t);
        }
        String areaId = areaAndTelNumSelector.getAreaId(app);
        //保存业务数据，后续事件要用到
        BusinessState state = new BusinessState.Builder()
                .setTenantId(tenant.getId())
                .setAppId(app.getId())
                .setId(call_id)
                .setResId(res_id)
                .setType(BusinessState.TYPE_IVR_INCOMING)
                .setCallBackUrl(app.getUrl())
                .setAreaId(areaId)
                .setLineGatewayId(lineId)
                .setBusinessData(new MapBuilder<String,String>()
                        //incoming是第一个会话所以是自己引用自己
                        .put(BusinessState.REF_RES_ID,res_id)
                        //TYPE_IVR_INCOMING 才需要等待应答标记
                        .put(IVR_ANSWER_WAITTING_FIELD,"1")
                        //incoming事件from 和 to是相反的
                        .putIfNotEmpty("from",to)
                        .putIfNotEmpty("to",from)
                        .putIfWhere(CallCenterUtil.CALLCENTER_FIELD,iscc,call_id)
                        .putIfWhere(CallCenterUtil.ISCC_FIELD,iscc,CallCenterUtil.ISCC_TRUE)
                        .putIfNotEmpty(BusinessState.SESSIONID,callSession.getId())
                        .build())
                .build();
        businessStateService.save(state);
    }

    public void answer(String res_id,String call_id,String areaId){
        Map<String, Object> params = new MapBuilder<String,Object>()
                .put("res_id",res_id)
                .put("max_answer_seconds",MAX_DURATION_SEC)
                .put("user_data",call_id)
                .put("areaId",areaId)
                .build();
        RPCRequest rpcrequest = RPCRequest.newRequest(ServiceConstants.MN_CH_SYS_CALL_ANSWER,params);
        try {
            rpcCaller.invoke(sessionContext, rpcrequest,true);
        } catch (Throwable e) {
            logger.error("调用失败",e);
        }
    }

    public void doAction(String call_id,Map<String,Object> prevResults){
        worker.execute(() -> this.doWork(call_id,prevResults));
    }

    private boolean doWork(String call_id,Map<String,Object> prevResults){
        BusinessState state = businessStateService.get(call_id);
        if(state == null){
            logger.info("callId={}没有找到state",call_id);
            return false;
        }

        if(state.getClosed() != null && state.getClosed()){
            logger.info("[{}][{}]callId={}IVR呼叫已关闭",state.getTenantId(),state.getAppId(),call_id);
            return false;
        }

        Map<String,String> businessDate = state.getBusinessData();

        //呼入,如果有IVR_ANSWER_AFTER_XML_FIELD直接执行，不需要调用next获取xml
        if(state.getType().equals(BusinessState.TYPE_IVR_INCOMING)){
            String ivr_action_xml = businessDate.get(IVR_ANSWER_AFTER_XML_FIELD);
            if(StringUtil.isNotEmpty(ivr_action_xml)){
                businessStateService.deleteInnerField(call_id,IVR_ANSWER_AFTER_XML_FIELD);
                return handleXML(call_id,null,ivr_action_xml,state);
            }
        }
        String nextUrl = businessDate.get(IVR_NEXT_FIELD);
        // is "" 代表没有next，null代表第一次
        if(nextUrl!=null && StringUtils.isBlank(nextUrl)){
            logger.info("[{}][{}]没有后续ivr动作了，call_id={}",state.getTenantId(),state.getAppId(),call_id);
            hangup(state.getResId(),call_id,state.getAreaId());
            return  false;
        }
        String resXML = null;
        if(nextUrl == null){//第一次
            nextUrl = state.getCallBackUrl();
            resXML = getFirstIvr(call_id,nextUrl,state.getBusinessData().get("to"));
        }else{
            resXML = getNextRequest(call_id,nextUrl,businessDate.get(IVR_ACTION_FIELD),prevResults);
        }
        if(StringUtils.isBlank(resXML)){
            return false;
        }
        return handleXML(call_id,nextUrl,resXML,state);
    }

    private boolean handleXML(String call_id,String curUrl,String resXML,BusinessState state){
        ActionHandler  h = null;
        Element root = null;
        Element actionEle = null;
        try {
            Document doc = DocumentHelper.parseText(resXML);
            validateXMLSchema(doc);
            root = doc.getRootElement();
            actionEle = getActionEle(root);
            h = handlers.get(actionEle.getName().toLowerCase());
            if(h == null){
                logger.info("[{}][{}]callId={}没有找到对应的ivr动作处理类",state.getTenantId(),state.getAppId(),call_id);
                return false;
            }
            //呼叫中心排队
            if(h instanceof EnqueueHandler){
                if(!conversationService.isCC(state)){
                    logger.info("[{}][{}]callId={}没有开通呼叫中心服务",state.getTenantId(),state.getAppId(),call_id);
                    return false;
                }
            }
            //不是挂断动作且未应答，需要自动应答
            if(! (h instanceof HangupActionHandler) && state.getBusinessData().get(IVR_ANSWER_WAITTING_FIELD) !=null){
                businessStateService.updateInnerField(call_id,IVR_ANSWER_AFTER_XML_FIELD,resXML);
                if(logger.isDebugEnabled()){
                    logger.info("调用应答isCallcenter={}，callid={}",conversationService.isCC(state),call_id);
                }
                answer(state.getResId(),call_id,state.getAreaId());
                return true;
            }
            businessStateService.updateInnerField(call_id,IVR_ACTION_FIELD,h.getAction());
            if(logger.isDebugEnabled()){
                logger.debug("[{}][{}]开始处理ivr动作，callId={},ivr={}",state.getTenantId(),state.getAppId(),call_id,h.getAction());
            }
            String nextUrl = getNextUrl(root);
            if(StringUtil.isNotEmpty(nextUrl)){
                //如果是相对路径需要根据相对路径返回绝对路径
                nextUrl = UrlUtils.resolve(curUrl,getNextUrl(root));
            }
            return h.handle(call_id,state,actionEle,nextUrl);
        } catch(DocumentException e){
            logger.error(String.format("[%s][%s]callid=%s处理ivr动作指令出错",state.getTenantId(),state.getAppId(),call_id),e);
            //发送ivr格式错误通知
            Map<String,Object> notify_data = new MapBuilder<String,Object>()
                    .putIfNotEmpty("event","ivr.format_error")
                    .putIfNotEmpty("id",call_id)
                    .putIfNotEmpty("user_data",state.getUserdata())
                    .build();
            notifyCallbackUtil.postNotify(state.getCallBackUrl(),notify_data,3);
            hangup(state.getResId(),call_id,state.getAreaId());
            return false;
        } catch (Throwable e) {
            logger.error(String.format("[%s][%s]callid=%s处理ivr动作指令出错",state.getTenantId(),state.getAppId(),call_id),e);
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
            if(!businessStateService.closed(call_id)) {
                rpcCaller.invoke(sessionContext, rpcrequest, true);
            }
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

    public boolean validateXMLSchema(Document doc) throws DocumentException {
        try {
            Validator validator = schema.newValidator();
            validator.validate(new DocumentSource(doc));
        } catch (Throwable e) {
            throw new DocumentException(e);
        }
        return true;
    }
}
