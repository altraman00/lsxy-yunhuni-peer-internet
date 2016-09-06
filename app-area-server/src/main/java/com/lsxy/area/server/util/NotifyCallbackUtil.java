package com.lsxy.area.server.util;

import com.lsxy.framework.core.utils.JSONUtil2;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 通过这个类向开发者发送事件通知
 * Created by liuws on 2016/8/30.
 */
@Component
public class NotifyCallbackUtil {

    private static final Logger logger = LoggerFactory.getLogger(NotifyCallbackUtil.class);

    private static final String APPLICATION_JSON = "application/json;charset=utf-8";

    private static final String CONTENT_TYPE_TEXT_JSON = "text/json";

    private static final String EVENT_NOTIFY_URL = "/yunhuni/event/notify";

    private CloseableHttpAsyncClient client = null;


    //设置请求和传输超时时间
    private RequestConfig config =
            RequestConfig.custom().setSocketTimeout(30000).setConnectTimeout(30000).build();

    @PostConstruct
    public void init(){
        client = HttpAsyncClients.createDefault();
        client.start();
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
     *
     * @param url 请求地址
     * @param data 请求数据
     * 30秒超时，不重试
     * @return
     */
    public Response postNotify(String url, Object data){
        return postNotify(url,data,null,0);
    }

    /**
     *
     * @param url 请求地址
     * @param data 请求数据
     * 30秒超时
     * @param retry 重试次数
     * @return
     */
    public Response postNotify(String url, Object data,int retry){
        return postNotify(url,data,null,retry);
    }

    /**
     *
     * @param url app设置的回调地址
     * @param data 请求数据
     * @param timeout 超时时间
     * @param retry 重试次数
     * @return
     */
    public Response postNotify(final String url, final Object data,final Integer timeout,final int retry){
        Response res = new Response();
        try{
            HttpPost post = new HttpPost(url + EVENT_NOTIFY_URL);
            RequestConfig c = this.config;
            if(timeout != null){
                c = RequestConfig.custom().setSocketTimeout(timeout*1000)
                        .setConnectTimeout(timeout*1000).build();
            }
            post.setConfig(c);
            post.addHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON);
            StringEntity se = new StringEntity(JSONUtil2.objectToJson(data));
            se.setContentType(CONTENT_TYPE_TEXT_JSON);
            se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON));
            post.setEntity(se);
            client.execute(post,new FutureCallback<HttpResponse>(){

                @Override
                public void completed(HttpResponse response) {
                    boolean success = false;
                    if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                        success = true;
                    }
                    if(!success){
                        logger.error("发送事件通知失败http status={}",response.getStatusLine().getStatusCode());
                        if(retry >0){
                            logger.info("开始重试");
                            postNotify(url,data,retry-1);
                        }
                    }
                }

                @Override
                public void failed(Exception e) {
                    logger.error("发送事件通知失败",e);
                    if(retry >0){
                        logger.info("开始重试");
                        postNotify(url,data,retry-1);
                    }

                }

                @Override
                public void cancelled() {
                    logger.error("发送事件通知被取消");
                    if(retry >0){
                        logger.info("开始重试");
                        postNotify(url,data,retry-1);
                    }
                }
            });
        }catch (Throwable t){
            logger.error("调用{}失败",url);
            t.printStackTrace();
        }
        return res;
    }

    private String receiveResponse(InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        StringBuilder sb = new StringBuilder();
        while((line = br.readLine())!=null){
            sb.append(line);
        }
        String reqBody = sb.toString();
        return URLDecoder.decode(reqBody, HTTP.UTF_8);
    }

    public class Response{
        private boolean result;
        private String data;

        public boolean isResult() {
            return result;
        }

        public void setResult(boolean result) {
            this.result = result;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }

    public static void main(String[] args) {
        NotifyCallbackUtil a = new NotifyCallbackUtil();
        a.init();
        long s1=System.currentTimeMillis();
        Map<String,Object> data = new HashMap();
        data.put("userName","123456");
        data.put("password","123456");
        data.put("code","nc7y");
        Response res = a.postNotify("http://google.com/",data,3);
        System.out.println((System.currentTimeMillis()-s1));
    }
}
