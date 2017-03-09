package com.lsxy.msg.supplier.factory.client;

import com.lsxy.framework.core.utils.JSONUtil;
import com.lsxy.msg.supplier.utils.HttpConnectionManager;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangxb on 2016/12/26.
 */
public class QiXinTongClient {
    private static final Logger logger = LoggerFactory.getLogger(QiXinTongClient.class);
    private static final String ERROR_STR = "{\"resultmsg\":\"系统错误\",\"resultcode\":\"10007\"}";
    /** 服务器 */
    private static final String PRODUCT_SERVER = "http://112.126.94.159";
    /** 企信通Id */
    private static final String CPID = "707";
    /** 企信通能力Id */
    private static final String TOPID = "AAAB";
    /** 企信通业务Id */
    private static final String SERVICEID = "AAAG";
    /** 自定义拓展号*/
    private static final String EXCODE = "4040";
    /** 私钥*/
    private static final String KEY = "29cd4deb7b079f38";
    /** 普通短信 */
    private static final String SEND_COMMON_MSG = "/cmpis/sendcommonmsg";
    /** 模板短信 */
    private static final String SEND_TEMPLET_MSG = "/cmpis/sendtempletmsg";

    /**发送普通短信**/
    public String sendMSM(String taskName,String msg , String mobiles){
        String data = CPID + TOPID + SERVICEID + msg + mobiles + EXCODE + KEY;
        String sign = makeMD5(data);
        Map<String,String> params = new HashMap<>();
        params.put("cpid",CPID);
        params.put("topid",TOPID);
        params.put("serviceid",SERVICEID);
        params.put("msg",msg);
        params.put("mobiles",mobiles);
        String excode = EXCODE;
//        if(StringUtils.isNotEmpty(taskName)){
//            if(taskName.length()>15){
//                excode = taskName.substring(0,15);
//            }else{
//                excode = taskName;
//            }
//        }
        params.put("excode",excode);
        params.put("sign",sign);
        return  doPost(PRODUCT_SERVER + SEND_COMMON_MSG,params,null);
    }
    /**发送模板短信**/
    public String sendTempleMSM(String taskName,String templetId , String msg ,String mobiles){
        String data = CPID + TOPID + SERVICEID + msg + mobiles + EXCODE + templetId + KEY;
        String sign = makeMD5(data);
        Map<String,String> params = new HashMap<>();
        params.put("cpid",CPID);
        params.put("topid",TOPID);
        params.put("serviceid",SERVICEID);
        params.put("msg",msg);
        params.put("mobiles",mobiles);
        String excode = EXCODE;
        if(StringUtils.isNotEmpty(taskName)){
            if(taskName.length()>15){
                excode = taskName.substring(0,15);
            }else{
                excode = taskName;
            }
        }
        params.put("excode",excode);
        params.put("templetid",templetId);
        params.put("sign",sign);
        return  doPost(PRODUCT_SERVER + SEND_TEMPLET_MSG,params,null);
    }

    /**
     * 构建http post请求
     * @param uri 请求地址
     * @param params 请求参数
     * @param headers 请求headers参数
     * @return
     */
    public static String doPost(String uri, Map<String,String> params, Map<String,String> headers ){
        try{
            HttpClient httpclient  = HttpConnectionManager.getHttpClient();
            HttpPost httppost = new HttpPost(uri);
            //请求参数以body传输
            String contentType = "application/json; charset=utf-8";
            httppost.addHeader("Content-type",contentType);
            httppost.setHeader("Accept", "application/json");
            String payload = JSONUtil.objectToJson(params);
            httppost.setEntity(new StringEntity(payload, Charset.forName("UTF-8")));
            HttpResponse response = httpclient.execute(httppost);
            int code = response.getStatusLine().getStatusCode();
            if(code!= HttpStatus.SC_OK){
                return ERROR_STR;
            }
            HttpEntity entity = response.getEntity();
            String re = EntityUtils.toString(entity, "UTF-8");
            return re;
        }catch (Exception e){
            logger.error("[QiXinTongClient][请求结果异常]：",e);
        }
        return ERROR_STR;
    }

    public static void main(String[] args) {
        System.out.println( makeMD5("11111"));
    }
    /**加密算法**/
    public static String makeMD5(String plainText) {
        try {
            logger.info("企讯通加密初始数据："+plainText);
            String re_md5 = new String();
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            re_md5 = buf.toString();
            return re_md5;
        } catch (NoSuchAlgorithmException e) {
            logger.error("企信通加密失败：",e);
            throw new RuntimeException("企信通加密失败，错误内容:["+e.getMessage()+"]");
        }
    }

}
