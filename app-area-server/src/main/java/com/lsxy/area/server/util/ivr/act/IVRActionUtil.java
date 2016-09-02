package com.lsxy.area.server.util.ivr.act;

import com.lsxy.area.api.BusinessState;
import com.lsxy.area.api.BusinessStateService;
import com.lsxy.area.server.util.ivr.act.handler.ActionHandler;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.service.AppService;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HTTP;
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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by liuws on 2016/9/1.
 */
@Component
public class IVRActionUtil {
    private static final Logger logger = LoggerFactory.getLogger(IVRActionUtil.class);

    private static final String APPLICATION_JSON = "application/json;charset=utf-8";

    private static final String CONTENT_TYPE_TEXT_JSON = "text/json";

    //询问是否接听的url
    private static final String IFACCEPTURL = "/yunhuni/ivr/accept";

    //请求第一步的ivr指令
    private static final String STEP1 = "/yunhuni/ivr/start";

    private static final int RETRY_TIMES = 3;

    private HttpClient client = null;

    //设置请求和传输超时时间
    private RequestConfig config =
            RequestConfig.custom().setSocketTimeout(30000).setConnectTimeout(30000).build();

    @Autowired
    private BusinessStateService businessStateService;

    @Autowired
    private AppService appService;

    @Autowired
    private ApplicationContext applicationContext;

    private Map<String,ActionHandler> handlers = new HashMap<>();

    @PostConstruct
    public void init(){
        initClient();
    }
    private void initClient(){
        client = HttpClientBuilder.create().build();
    }
    private void initHandler(){
        Reflections reflections = new Reflections("com.lsxy.area.server.util.ivr");
        Set<Class<? extends ActionHandler>> handlerClasss = reflections.getSubTypesOf(ActionHandler.class);
        for (Class<? extends ActionHandler> handlerClass : handlerClasss) {
            ActionHandler handler = applicationContext.getBean(handlerClass);
            handlers.put(handler.getAction(),handler);
            logger.info("注册IVR动作处理器:{},{}",handler.getAction(),handler);
        }
    }
    @PreDestroy
    public void destroy(){
        if(client instanceof CloseableHttpClient){
            try {
                ((CloseableHttpClient)client).close();
            } catch (IOException e) {
                logger.info("关闭httpclient",e);
            }
        }
    }

    private String getRequest(String url){
        String res = null;
        boolean result = false;
        int re_times = 0;
        do{
            try{
                HttpGet get = new HttpGet(url);
                get.setConfig(config);
                HttpResponse response = client.execute(get);
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    res = receiveResponse(response);
                    result = true;
                }
            }catch (Throwable t){
                logger.error("调用{}失败",url);
            }
            re_times++;
        }while (!result && re_times<=RETRY_TIMES);
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
        BufferedReader br = new BufferedReader(new InputStreamReader(entity.getContent()));
        String line;
        StringBuilder sb = new StringBuilder("");
        while((line = br.readLine())!=null){
            sb.append(line);
        }
        String reqBody = sb.toString();
        return URLDecoder.decode(reqBody, HTTP.UTF_8);
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
        String res = getRequest(app.getUrl()+IFACCEPTURL);
        if(!"accept".equals(res.toLowerCase())){
            //TODO 发送拒接指令
            return true;
        }
        //TODO 发送接收指令
        return doAction(call_id);
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

        if(nextUrl == null){//第一次
            String appId = state.getAppId();
            App app = appService.findById(appId);
            nextUrl = app.getUrl() + STEP1;
        }

        String resXML = getRequest(nextUrl.toString());

        ActionHandler  h = null;
        Element ele = null;
        try {
            Document doc = DocumentHelper.parseText(resXML);
            ele = doc.getRootElement();
            h = handlers.get(ele.getName());
            if(h == null){
                logger.info("没有找到对应的ivr动作处理类");
                return false;
            }
        } catch (Throwable e) {
            logger.error("处理ivr动作指令出错",e);
        }
        return h.handle(call_id,ele);
    }
}
