package com.lsxy.area.server.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * Created by liuws on 2017/2/6.
 */
public class HttpClientHelper {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientHelper.class);

    public static final SSLContext sslContext = createSSLContext();

    public static final HostnameVerifier hostnameVerifier = createHostnameVerifier();

    private static SSLContext createSSLContext() {
        try {
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null,
                    new TrustManager[] { new X509TrustManager(){
                        @Override
                        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                        }

                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }
                    }}, null);
            return context;
        } catch (Exception e) {
            throw new RuntimeException("SSL Context 创建失败");
        }
    }

    private static HostnameVerifier createHostnameVerifier(){
        return new HostnameVerifier(){
            @Override
            public boolean verify(String s, SSLSession sslSession) {
                return true;
            }
        };
    }
}
