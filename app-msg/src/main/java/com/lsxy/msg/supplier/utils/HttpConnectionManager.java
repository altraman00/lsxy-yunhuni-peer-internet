package com.lsxy.msg.supplier.utils;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * Created by zhangxb on 2017/1/9.
 */
public class HttpConnectionManager {
    private static final int maxTotalPool = 200;// 最大连接数
    private static final int maxConPerRoute = 200;// 每个路由最大连接数
    private static final int socketTimeout = 2000;
    private static final int connectionRequestTimeout = 3000;
    private static final int connectTimeout = 1000;

    private static PoolingHttpClientConnectionManager cm = null;

    static {
        LayeredConnectionSocketFactory sslsf = null;
        try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            X509TrustManager tm = new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {
                }
                @Override
                public void checkServerTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {
                }
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            ctx.init(null, new TrustManager[]{tm}, null);
            sslsf = new SSLConnectionSocketFactory(ctx);
        } catch (KeyManagementException e) {
        } catch (NoSuchAlgorithmException e){
        }
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create()
                .register("https", sslsf)
                .register("http", new PlainConnectionSocketFactory())
                .build();
        cm =new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        cm.setMaxTotal(maxTotalPool);
        cm.setDefaultMaxPerRoute(maxConPerRoute);
    }

    public static CloseableHttpClient getHttpClient() {
        //设置超时等参数
        RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(connectionRequestTimeout)
                .setConnectTimeout(connectTimeout).setSocketTimeout(socketTimeout).build();
        //创建htt
        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(cm).setDefaultRequestConfig(requestConfig).build();

        /*CloseableHttpClient httpClient = HttpClients.createDefault();//如果不采用连接池就是这种方式获取连接*/
        return httpClient;
    }
}
