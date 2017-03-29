package com.oneyun.sapi.utils;

import org.apache.commons.codec.binary.Base64;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
/**
 * Created by zhangxb on 2016/8/29.
 */
public class EncryptUtil {

    /**
     * 获取签名数据
     * @param method HTTP请求方法，POST GET PUT DELETE
     * @param payload Post请求时的包体进行MD5签名
     * @param contentType HTTP请求的内容类型
     * @param timestamp 时间戳为调用时的时间以yyyyMMddHHmmss格式提供 必须同请求头中的时间戳保持一致
     * @param appId 应用APP标识
     * @param uri 请求的API 的地址
     * @return 返回签名内容
     */
    public static String getSign(String method,String payload,String contentType,String timestamp,String appId,String uri){
        // 是否有post 或者 put
        boolean hasContent = "PUT".equals(method)||"POST".equals(method)?true:false;
        //j8bm4n329cHi4lSMdMJG482wfaF0POq4PmzQn7lK8XM
        String contentMd5 = hasContent ? (new Md5PasswordEncoder()).encodePassword(payload, null) : "";
        String contentType1 = hasContent ? contentType : "";
        // 组织签名数据
        StringBuilder toSign = new StringBuilder();
        toSign.append(method).append("\n")
                .append(contentMd5).append("\n")
                .append(contentType1).append("\n")
                .append(timestamp).append("\n")
                .append(appId).append("\n")
                .append(uri);
        return toSign.toString();
    }
    /**
     *HmacSHA256签名算法
     * @param secret 秘钥
     * @param data 签名数据
     * @return 签名后数据
     */
    public static String calculateHMAC(String secret, String data) throws NoSuchAlgorithmException, InvalidKeyException {
        SecretKeySpec signingKey = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(signingKey);
        byte[] rawHmac = mac.doFinal(data.getBytes());
        String result = new String(Base64.encodeBase64(rawHmac));
        return result;
    }

    /**获取业务数据加密后的结果*/
    public static String getHmac(String uri,String secretKey,String certId,String appId ,String payload,String contentType,String method,String timestamp) throws MalformedURLException, InvalidKeyException, NoSuchAlgorithmException {
        String data = EncryptUtil.getSign( method, payload, contentType, timestamp, appId, new URL(uri).getPath());
        String hmac = EncryptUtil.calculateHMAC( secretKey,  data);
        return hmac;
    }
}
