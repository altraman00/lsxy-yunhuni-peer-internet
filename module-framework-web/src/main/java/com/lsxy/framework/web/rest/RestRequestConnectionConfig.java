package com.lsxy.framework.web.rest;

import com.lsxy.framework.config.SystemConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

/**
 * Created by Tandy on 2016/6/22.
 * Rest request 请求对象用于连接的配置对象  单例对象
 */
public class RestRequestConnectionConfig {
    private static RestRequestConnectionConfig config;

    private PoolingHttpClientConnectionManager cm;
    private HttpComponentsClientHttpRequestFactory httpFactory;

    protected RestRequestConnectionConfig(){
        //初始化连接池
        cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(Integer.parseInt(SystemConfig.getProperty("global.rest.api.client.pool.size","1000")));  //设置整个连接池最大连接数
        cm.setDefaultMaxPerRoute(cm.getMaxTotal());

        //构建httpclient
        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(cm)
                .build();

        httpFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        httpFactory.setConnectTimeout(Integer.parseInt(SystemConfig.getProperty("global.rest.api.client.pool.connection.timeout","5000")));
        httpFactory.setReadTimeout(Integer.parseInt(SystemConfig.getProperty("global.rest.api.client.pool.read.timeout","5000")));
        httpFactory.setConnectionRequestTimeout(Integer.parseInt(SystemConfig.getProperty("global.rest.api.client.pool.connection.timeout","5000")));

    }

    public PoolingHttpClientConnectionManager getCm() {
        return cm;
    }

    public void setCm(PoolingHttpClientConnectionManager cm) {
        this.cm = cm;
    }

    public HttpComponentsClientHttpRequestFactory getHttpFactory() {
        return httpFactory;
    }

    public void setHttpFactory(HttpComponentsClientHttpRequestFactory httpFactory) {
        this.httpFactory = httpFactory;
    }

    public static RestRequestConnectionConfig defaultConfig(){
        if(config == null) {
            config = new RestRequestConnectionConfig();
        }
        return config;
    }
}
