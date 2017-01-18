package com.oneyun.sapi.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangxb on 2016/8/26.
 */
public class HttpClientUtils {
    private static final String contentType = "application/json; charset=utf-8";
    private static final String accept = "application/json";


    public static String doDelete(String uri,String secretKey,String certId,String appId , Map<String,Object> param) throws KeyManagementException, NoSuchAlgorithmException, IOException, InvalidKeyException {
        String payload = JsonUtils.toJson(param);
        String timestamp = DateUtils.getTimestamp();

        //处理参数和请求地址
        uri = paramToUri(uri,param);

        HttpDelete httpDelete = new HttpDelete(uri);

        //获取加密结果
        String hmac = EncryptUtil.getHmac(uri,secretKey,certId,appId,payload,contentType,httpDelete.getMethod(),timestamp);

        //获取httpclient
        HttpClient httpclient = HttpConnectionManager.getHttpClient();

        //设置header信息
        setHeader(httpDelete,contentType,accept,appId,timestamp,certId,hmac);

        //发动请求并处理
        HttpResponse response = httpclient.execute(httpDelete);
        int code = response.getStatusLine().getStatusCode();
        if( code!= HttpStatus.SC_OK){
            throw new RuntimeException("访问目标地址失败code:["+code+"]");
        }
        HttpEntity entity = response.getEntity();
        String result = EntityUtils.toString(entity, "UTF-8");
        return result;
    }
    public static String doGet(String uri,String secretKey,String certId,String appId , Map<String,Object> param) throws KeyManagementException, NoSuchAlgorithmException, IOException, InvalidKeyException {
        String payload = JsonUtils.toJson(param);
        String timestamp = DateUtils.getTimestamp();

        //处理参数和请求地址
        uri = paramToUri(uri,param);

        HttpGet httpGet = new HttpGet(uri);

        //获取加密结果
        String hmac = EncryptUtil.getHmac(uri,secretKey,certId,appId,payload,contentType,httpGet.getMethod(),timestamp);

        //获取httpclient
        HttpClient httpclient = HttpConnectionManager.getHttpClient();

        //设置header信息
        setHeader(httpGet,contentType,accept,appId,timestamp,certId,hmac);

        //发动请求并处理
        HttpResponse response = httpclient.execute(httpGet);
        int code = response.getStatusLine().getStatusCode();
        HttpEntity entity = response.getEntity();
        String result = EntityUtils.toString(entity, "UTF-8");
        if( code != HttpStatus.SC_OK){
            throw new RuntimeException("访问目标地址失败code:["+code+"]result:["+result+"]");
        }
        return result;
    }
    public static String doPost(String uri,String secretKey,String certId,String appId , Map param) throws KeyManagementException, NoSuchAlgorithmException, IOException, InvalidKeyException {
        String payload = JsonUtils.toJson(param);
        String timestamp = DateUtils.getTimestamp();

        HttpPost httppost = new HttpPost(uri);

        //获取加密结果
        String hmac = EncryptUtil.getHmac(uri,secretKey,certId,appId,payload,contentType,httppost.getMethod(),timestamp);

        //获取httpclient
        HttpClient httpclient = HttpConnectionManager.getHttpClient();

        //设置header信息
        setHeader(httppost,contentType,accept,appId,timestamp,certId,hmac);
        //设置请求参数
        httppost.setEntity(new StringEntity(payload, Charset.forName("UTF-8")));

        //发动请求并处理
        HttpResponse response = httpclient.execute(httppost);
        int code = response.getStatusLine().getStatusCode();
        HttpEntity entity = response.getEntity();
        LoggerUtil.info("POST 请求结果:"+entity.toString());
        String result = EntityUtils.toString(entity, "UTF-8");
        if( code != HttpStatus.SC_OK){
            throw new RuntimeException("访问目标地址失败code:["+code+"]result:["+result+"]");
        }
        return result;
    }
    public static String doPut(String uri,String secretKey,String certId,String appId , Map param) throws KeyManagementException, NoSuchAlgorithmException, IOException, InvalidKeyException {
        String payload = JsonUtils.toJson(param);
        String timestamp = DateUtils.getTimestamp();

        HttpPut httpPut = new HttpPut(uri);

        //获取加密结果
        String hmac = EncryptUtil.getHmac(uri,secretKey,certId,appId,payload,contentType,httpPut.getMethod(),timestamp);

        //获取httpclient
        HttpClient httpclient = HttpConnectionManager.getHttpClient();

        //设置header信息
        setHeader(httpPut,contentType,accept,appId,timestamp,certId,hmac);
        //设置请求参数
        httpPut.setEntity(new StringEntity(payload, Charset.forName("UTF-8")));

        //发动请求并处理
        HttpResponse response = httpclient.execute(httpPut);
        int code = response.getStatusLine().getStatusCode();
        HttpEntity entity = response.getEntity();
        String result = EntityUtils.toString(entity, "UTF-8");
        if( code != HttpStatus.SC_OK){
            throw new RuntimeException("访问目标地址失败code:["+code+"]result:["+result+"]");
        }
        return result;
    }

    /**
     * 解析回调参数
     * @param request
     * @return
     */
    public static Map getRequestParams(HttpServletRequest request){
        StringBuilder stringBuilder = new StringBuilder();
        InputStream inputStream = null;
        try {
            inputStream = request.getInputStream();
            if (inputStream != null) {
                byte[] b = new byte[128];
                int len = -1;
                while ((len = inputStream.read(b)) > 0) {
                    stringBuilder.append(new String(b, 0, len,"utf-8"));
                }
            } else {
                stringBuilder.append("");
            }
        } catch (IOException ex) {

        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException iox) {
                }
            }
        }
        String str = stringBuilder.toString();
        Map map = new HashMap<>();
        if(StringUtils.isNotEmpty(str)) {
            map = JsonUtils.toMap(str);
        }
        return map;
    }

    /**get和delete请求时，将参数拼接到uri后面**/
    private static String paramToUri(String uri, Map<String,Object> param) throws IOException {
        //转换为键值对
        List<NameValuePair> params = new ArrayList();
        for (Map.Entry<String,Object> en : param.entrySet()) {
            String value = en.getValue()==null ? null:en.getValue().toString();
            params.add(new BasicNameValuePair(en.getKey().toString(),value));
        }
        String str = EntityUtils.toString(new UrlEncodedFormEntity(params, Consts.UTF_8));
        if(StringUtils.isNotEmpty(str)){
            uri+="?"+str;
        }
        return uri;
    }

    /**设置指定header信息*/
    private static void setHeader(HttpRequestBase httpRequestBase, String contentType, String accept, String appId, String timestamp, String certId, String signature) {
        httpRequestBase.addHeader("Content-type",contentType);
        //请求参数以body传输
        httpRequestBase.setHeader("Accept", accept);
        //设置鉴权信息
        httpRequestBase.addHeader("AppID",appId);
        httpRequestBase.addHeader("Timestamp",timestamp);
        httpRequestBase.addHeader("CertID",certId);
        httpRequestBase.addHeader("Signature",signature);
    }

    /**获取请求ip地址*/
    public static String getRemoteHost(HttpServletRequest request){
        String ip = request.getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
            ip = request.getHeader("Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
            ip = request.getRemoteAddr();
        }
        return ip.equals("0:0:0:0:0:0:0:1")?"127.0.0.1":ip;
    }
}

