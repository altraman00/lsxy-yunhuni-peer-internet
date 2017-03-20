package com.lsxy.framework.core.utils;

import com.lsxy.framework.config.SystemConfig;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.Date;

/**
 * Created by zhangxb on 2016/8/29.
 */
public class OssTempUriUtils {
    private static final String HMAC_SHA1 = "HmacSHA1";
    private static final String HMAC_SHA256 = "HmacSHA1";
    /**
     *HmacSHA256签名算法
     * @param secret 秘钥
     * @param data 签名数据
     * @return 签名后数据
     */
    public static String calculateHMAC(String secret, String data) {

        try {
            SecretKeySpec signingKey = new SecretKeySpec(secret.getBytes(), HMAC_SHA1);
            Mac mac = Mac.getInstance(HMAC_SHA1);
            mac.init(signingKey);
            byte[] rawHmac = mac.doFinal(data.getBytes());
            String result = new String(Base64.encodeBase64(rawHmac));
            return result;
        } catch (GeneralSecurityException e) {
            throw new IllegalArgumentException();
        }
    }
    /**
     * 获取oss的临时下载链接
     * @param accessId
     * @param accessKey
     * @param host
     * @param verb
     * @param resource
     * @param expire
     * @return
     */
    public static String getOssTempUri(String accessId,String accessKey,String host,String verb,String resource,long expire){
        String md5 = "";
        String contentType = "";
        //验证时间
        long signDateTime = new Date().getTime()/1000;
        String expireTime = (signDateTime+expire)+"";

        StringBuilder toSign = new StringBuilder();
        toSign.append(verb).append("\n")
                .append(md5).append("\n")
                .append(contentType).append("\n")
                .append(expireTime).append("\n")
                .append("")
                .append(resource);

        String sign = calculateHMAC(accessKey,toSign.toString());

        String[] splited = resource.split("/");
        String bucket = "";
        String object_name = "";
        if (splited.length == 3)
        {
            bucket = splited[1];
            object_name = splited[2];
        }
        else if (splited.length > 3)
        {
            bucket = splited[1];
            for (int i = 2; i < splited.length; ++i)
            {
                if (i != 2)
                {
                    object_name += "/";
                }
                object_name += splited[i];
            }
        }
        System.out.println(sign);
        String url =  "http://"+bucket + "." + host + "/" + object_name + "?OSSAccessKeyId=" + accessId + "&Expires=" + expireTime + "&Signature=" + OSSEncodeURI.encodeURIComponent(sign);
        return url;
    }
//    public static void main(String[] args){
//        String accessId= "nfgEUCKyOdVMVbqQ";
//        String accessKey = "HhmxAMZ2jCrE0fTa2kh9CLXF9JPcOW";
//        String host = "oss-cn-beijing.aliyuncs.com";
//        String verb = "GET";
//        String resource = "/yunhuni-development/tenant_res/40288aca574060400157406339080002/realname_auth/20160919/f4d2f47874cc9af7e123ebca7810bf89.jpg";
//        long expire = 20;
//        String re = getOssTempUri(accessId,accessKey,host,verb,resource,expire);
//        System.out.println(re);
//    }

    public static String getOssTempUri(String resource){
        String host = SystemConfig.getProperty("global.oss.aliyun.endpoint.internet","http://oss-cn-beijing.aliyuncs.com");
        String accessId = SystemConfig.getProperty("global.aliyun.key","nfgEUCKyOdVMVbqQ");
        String accessKey = SystemConfig.getProperty("global.aliyun.secret","HhmxAMZ2jCrE0fTa2kh9CLXF9JPcOW");
        String resource1 = SystemConfig.getProperty("global.oss.aliyun.bucket");
        try {
            URL url = new URL(host);
            host = url.getHost();
        }catch (Exception e){}
        resource = "/"+resource1+"/"+resource;
        String result = OssTempUriUtils.getOssTempUri( accessId, accessKey, host, "GET",resource ,60);
        return result;
    }

}
