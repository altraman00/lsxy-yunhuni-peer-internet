package com.lsxy.area.server.util;

import com.lsxy.framework.core.utils.JSONUtil2;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.protocol.HTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * 通过这个类向开发者发送事件通知
 * Created by liuws on 2016/8/30.
 */
@Component
public class NotifyCallbackUtil {

    private static final Logger logger = LoggerFactory.getLogger(NotifyCallbackUtil.class);

    private static final String APPLICATION_JSON = "application/json;charset=utf-8";

    private static final String ACCEPT_TYPE_TEXT_PLAIN = "text/plain;charset=utf-8";

    private CloseableHttpAsyncClient client = null;

    @PostConstruct
    public void init(){
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
                .build();
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
    public void postNotify(String url, Map<String,Object> data){
        postNotify(url,data,null,0);
    }

    /**
     *
     * @param url 请求地址
     * @param data 请求数据
     * 30秒超时
     * @param retry 重试次数
     * @return
     */
    public void postNotify(String url, Map<String,Object> data,int retry){
        postNotify(url,data,null,retry);
    }

    /**
     *
     * @param url app设置的回调地址
     * @param data 请求数据
     * @param timeout 超时时间
     * @param retry 重试次数
     * @return
     */
    public void postNotify(final String url, final Map<String,Object> data,final Integer timeout,final int retry){
        long start = System.currentTimeMillis();
        try{
            HttpPost post = new HttpPost(url);
            data.put("action","event_notify");
            post.setHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON);
            StringEntity se = new StringEntity(JSONUtil2.objectToJson(data));
            post.setEntity(se);
            post.setHeader("accept",ACCEPT_TYPE_TEXT_PLAIN);
            client.execute(post,new FutureCallback<HttpResponse>(){

                @Override
                public void completed(HttpResponse response) {
                    boolean success = false;
                    if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                        success = true;
                    }
                    if(logger.isDebugEnabled()){
                        logger.info("url={},status={}"
                                ,url,response.getStatusLine().getStatusCode());
                        logger.info("url={},耗时:{}ms",url,(System.currentTimeMillis() - start));
                    }
                    if(!success){
                        if(retry >0){
                            postNotify(url,data,retry-1);
                        }
                    }
                }

                @Override
                public void failed(Exception e) {
                    logger.error("url={}发送事件通知失败",url,e);
                    if(retry >0){
                        postNotify(url,data,retry-1);
                    }

                }

                @Override
                public void cancelled() {
                    logger.error("url={}发送事件通知被取消",url);
                    if(retry >0){
                        postNotify(url,data,retry-1);
                    }
                }
            });
        }catch (Throwable t){
            logger.error("调用{}失败",url,t);
        }
    }

    public boolean postNotifySync(final String url, final Map<String,Object> data,final Integer timeout,int retry){
        boolean success = false;
        data.put("action","event_notify");
        do{
            try{
                HttpPost post = new HttpPost(url);
                post.setHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON);
                StringEntity se = new StringEntity(JSONUtil2.objectToJson(data));
                post.setEntity(se);
                post.setHeader("accept",ACCEPT_TYPE_TEXT_PLAIN);
                Future<HttpResponse> future=client.execute(post,null);
                HttpResponse response = future.get();
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    success = true;
                }
                if(!success && logger.isDebugEnabled()){
                    logger.error("发送事件通知失败http status={}",response.getStatusLine().getStatusCode());
                }
            }catch (Throwable t){
                logger.error("调用{}失败",url,t);
            }
            if(!success){
                --retry;
            }
        }while (!success && retry>0);
        return success;
    }
    /*public static void main(String[] args) {
        NotifyCallbackUtil a = new NotifyCallbackUtil();
        a.init();
        long s1=System.currentTimeMillis();
        Map<String,Object> data = new HashMap();
        data.put("userName","123456");
        data.put("password","123456");
        data.put("code","nc7y");
        Response res = a.postNotify("http://google.com/",data,3);
        System.out.println((System.currentTimeMillis()-s1));
    }*/
}
