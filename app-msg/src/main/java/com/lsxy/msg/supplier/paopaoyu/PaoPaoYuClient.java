package com.lsxy.msg.supplier.paopaoyu;

import com.lsxy.framework.config.SystemConfig;
import net.sf.json.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.apache.http.HttpStatus;
import org.apache.http.conn.ConnectTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by zhangxb on 2016/11/12.
 */
public class PaoPaoYuClient {
    private static final Logger logger = LoggerFactory.getLogger(PaoPaoYuClient.class);
    private static final String ERROR_STR = "{\"result_desc\":\"系统错误\",\"result_code\":\"10007\"}";
    /** 服务器 */
    private static final String PRODUCT_SERVER = "https://120.25.227.166:9443";
    private static final String SERVER = SystemConfig.getProperty("ussd.client.http.url",PRODUCT_SERVER);
    /** 授权接口 */
    private static final String URL_OAUTH = SERVER + "/open/oauth/authorization";
    /** 消息发送接口 */
    private static final String URL_SEND_MSG = SERVER + "/open/sendMessage";
    /** 新增群发任务提交接口 */
    private static final String URL_ADD_TASK = SERVER + "/open/addTask";
    /** 查询群发任务执行结果接口 */
    private static final String URL_GET_TASK = SERVER + "/open/getTask";

    /** 申请应用时分配的app_key */
    private static final String APP_KEY = "94306e33-aa15-47a5-ab3b-d4c9f7e8e3aa";
    /** 申请应用时分配的app_secret */
    public static final String APP_SECRET = "7Pq5wnZu5JfqkGB05LhaDQ==";
    /** 授权类型 */
    private static final String GRANT_TYPE = "password";

    // 授权企业用户的用户名
    private static String USERNAME = "10001";
    // 授权企业用户的密码
    private static String PASSWORD = "123456";

    private static String accessToken = null;

    private static DateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmssSSS");

    public PaoPaoYuClient(){
        logger.info("初始化USSD CLIENT");
        Protocol myhttps = new Protocol("https", new MySSLSocketFactory(), 443);
        Protocol.registerProtocol("https", myhttps);

        // 获取token
        JSONObject oauthJson = getOauthToken();
        accessToken = oauthJson.getString("access_token");
        logger.info("获取到ussd access token : {}",accessToken);
    }



    public static String doPost(String url, Map<String, Object> params){
        HttpClientParams clientParams = new HttpClientParams();
        // 忽略cookie 避免 Cookie rejected 警告
        clientParams.setCookiePolicy(CookiePolicy.IGNORE_COOKIES);
        HttpClient client = new HttpClient(clientParams);

        PostMethod method = new PostMethod(url);

        // 设置Http Post数据
        if (params != null) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                method.setParameter(entry.getKey(), String.valueOf(entry.getValue()));
            }
        }
        method.getParams().setContentCharset("UTF-8");
        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false));


        try {
            String token = null;
            List<Header> headers = new ArrayList<Header>();
            headers.add(new Header("Authorization", "OAuth2 " + token));
            headers.add(new Header("API-RemoteIP", InetAddress.getLocalHost().getHostAddress()));
            client.getHostConfiguration().getParams().setParameter("http.default-headers", headers);

            int code = client.executeMethod(method);

            if (method.getStatusCode() == HttpStatus.SC_OK) {
                String re = method.getResponseBodyAsString();
                logger.info("[PapPaoYuClient][请求结果]："+re);
                return re;
            } else {
                logger.info("[PapPaoYuClient][请求失败]："+code);
            }
        } catch (IOException e) {
            logger.error("[PapPaoYuClient][请求异常]" + url + "时，发生异常！",e);
        } finally {
            method.releaseConnection();
        }
        return ERROR_STR;
    }
    /**
     * 获取账号Token
     *
     * @throws IOException
     */
    private static JSONObject getOauthToken()  {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("client_id", APP_KEY);
        params.put("client_secret", APP_SECRET);
        params.put("grant_type", GRANT_TYPE);
        params.put("username", USERNAME);
        params.put("password", PASSWORD);

        String resp = doPost(URL_OAUTH, params);
        //失败：{"error":"1000000046","error_description":"The user name or user password mistake"}
        //成功：{"expires_in":8640000000,"refresh_token":"dcb3fe5c6cd5e1e1c6280dd67c6e141","access_token":"db485c4965cd1a922efac2e4fd354c"}
        logger.info("获取token参数："+resp);
        if (resp != null) {
            JSONObject json = JSONObject.fromObject(resp);
            return json;
        }
        return null;
    }

    /**
     * 发送消息
     */
    public String send(String destPhone ,String tempId ,String tempArgs,String sendType)  {
        String time = fmt.format(new Date());
        String serialId = APP_KEY + destPhone +fmt.format(new Date())+time;
        String sign = DigestUtils.md5Hex(serialId + "|" + APP_SECRET + "|" + tempId + "|" + destPhone);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("oauth_token", accessToken);
        params.put("serialId", serialId);
        params.put("templateId", tempId);
        params.put("phone", destPhone);
        params.put("params", tempArgs);
        params.put("sign", sign);
        params.put("sendType",sendType);
        String resp = doPost(URL_SEND_MSG, params);
        return resp;
    }
    /**
     * 新增群发任务提交接口
     */
    public String addTask(String taskName,String templateId,String args,String phoneList,String sendTime,String sendType)  {
        String serialId = APP_KEY + fmt.format(new Date());
        String sign = DigestUtils.md5Hex(serialId + "|" + APP_SECRET+"|"+templateId+"|"+sendTime);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("oauth_token", accessToken);
        params.put("serialId", serialId);
        params.put("taskName", taskName);
        params.put("templateId", templateId);
        params.put("args", args);
        params.put("phoneList", phoneList);
        params.put("sendTime", sendTime);
        params.put("sign", sign);
        params.put("sendType",sendType);
        String resp = doPost(URL_ADD_TASK, params);
        return resp;
    }
    /**
     * 查询群发任务执行结果接口
     */
    public String getTask(String taskId) {
        String serialId = APP_KEY + fmt.format(new Date());
        String sign = DigestUtils.md5Hex(serialId + "|" + APP_SECRET+"|"+taskId);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("oauth_token", accessToken);
        params.put("serialId", serialId);
        params.put("taskId", taskId);
        params.put("sign", sign);
        String resp = doPost(URL_GET_TASK, params);
        return resp;
    }

    private static class MySSLSocketFactory implements ProtocolSocketFactory {
        private SSLContext sslcontext = null;

        private SSLContext createSSLContext() {
            SSLContext sslcontext = null;
            try {
                sslcontext = SSLContext.getInstance("SSL");
                sslcontext.init(null, new TrustManager[] { new TrustAnyTrustManager() }, new java.security.SecureRandom());
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (KeyManagementException e) {
                e.printStackTrace();
            }
            return sslcontext;
        }

        private SSLContext getSSLContext() {
            if (this.sslcontext == null) {
                this.sslcontext = createSSLContext();
            }
            return this.sslcontext;
        }

        public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
            return getSSLContext().getSocketFactory().createSocket(socket, host, port, autoClose);
        }

        public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
            return getSSLContext().getSocketFactory().createSocket(host, port);
        }

        public Socket createSocket(String host, int port, InetAddress clientHost, int clientPort) throws IOException, UnknownHostException {
            return getSSLContext().getSocketFactory().createSocket(host, port, clientHost, clientPort);
        }

        public Socket createSocket(String host, int port, InetAddress localAddress, int localPort, HttpConnectionParams params) throws IOException,
                UnknownHostException, ConnectTimeoutException {
            if (params == null) {
                throw new IllegalArgumentException("Parameters may not be null");
            }
            int timeout = params.getConnectionTimeout();
            SocketFactory socketfactory = getSSLContext().getSocketFactory();
            if (timeout == 0) {
                return socketfactory.createSocket(host, port, localAddress, localPort);
            } else {
                Socket socket = socketfactory.createSocket();
                SocketAddress localaddr = new InetSocketAddress(localAddress, localPort);
                SocketAddress remoteaddr = new InetSocketAddress(host, port);
                socket.bind(localaddr);
                socket.connect(remoteaddr, timeout);
                return socket;
            }
        }

        private static class TrustAnyTrustManager implements X509TrustManager {
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[] {};
            }
        }
    }
}
