package com.lsxy.area.server.util;

import com.lsxy.framework.core.utils.JSONUtil2;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
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

/**
 * Created by liuws on 2016/8/30.
 */
@Component
public class NotifyCallbackUtil {

    private static final Logger logger = LoggerFactory.getLogger(NotifyCallbackUtil.class);

    private static final String APPLICATION_JSON = "application/json;charset=utf-8";

    private static final String CONTENT_TYPE_TEXT_JSON = "text/json";

    private HttpClient client = null;

    @PostConstruct
    public void init(){
        client = HttpClientBuilder.create().build();
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

    /**
     *
     * @param url 请求地址
     * @param data 请求数据
     * 30秒超时，不重试
     * @return
     */
    public Response postNotify(String url, Object data){
        return postNotify(url,data,30,0);
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
        return postNotify(url,data,30,retry);
    }

    /**
     *
     * @param url 请求地址
     * @param data 请求数据
     * @param timeout 超时时间
     * @param retry 重试次数
     * @return
     */
    public Response postNotify(String url, Object data,int timeout,int retry){
        Response res = new Response();
        boolean res_result = false;
        String res_data = null;
        int re_times = 0;
        do{
            try{
                HttpPost post = new HttpPost(url);
                post.addHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON);
                StringEntity se = new StringEntity(JSONUtil2.objectToJson(data));
                se.setContentType(CONTENT_TYPE_TEXT_JSON);
                se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON));
                post.setEntity(se);
                HttpResponse response = client.execute(post);
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    final HttpEntity entity = response.getEntity();
                    if (entity != null) {
                        res_data = receiveResponse(response.getEntity().getContent());
                    }
                    res_result = true;
                }
            }catch (Throwable t){
                logger.error("调用{}失败",url);
                t.printStackTrace();
            }
        }while (!res_result && re_times<retry);
        res.setData(res_data);
        res.setResult(res_result);
        return res;
    }

    private String receiveResponse(InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String line = null;
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
        /*NotifyCallbackUtil a = new NotifyCallbackUtil();
                a.init();
        Map<String,Object> data = new HashedMap();
        data.put("max_duration",1);
        data.put("max_parts",3);
        Response res = a.postNotify("http://192.168.20.102:18082/v1/account/11/conf/create",data);
        System.out.println(res.getData());*/
    }
}
