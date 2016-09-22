package com.lsxy.app.api.gateway.rest.test;

import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.StringUtil;
import com.lsxy.framework.web.rest.RestRequest;
import com.lsxy.framework.web.rest.RestRequestConnectionConfig;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.client.methods.HttpHead;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.web.util.DefaultUriTemplateHandler;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.Date;

/**
 * Created by Tandy on 2016/7/1.
 * api 网关请求对象
 */
public class APIGWRestRequest extends RestRequest{


    private static final Logger logger = LoggerFactory.getLogger(APIGWRestRequest.class);

    private String certId;

    public String getCertId() {
        return certId;
    }

    protected APIGWRestRequest(RestRequestConnectionConfig config) {
        super(config);
    }

    /**
     * 构建一个单一实例的请求对象
     * 因为每次安全请求的token有可能不一样，建议每次构建请求对象重新生成，避免多线程不安全性问题
     *
     * @return
     */
    public static APIGWRestRequest buildSecurityRequest(String certid) {
        APIGWRestRequest securityRequest = new APIGWRestRequest(RestRequestConnectionConfig.defaultConfig());
        securityRequest.setSecurityToken(certid);
        securityRequest.certId = certid;
        return securityRequest;
    }


    @Override
    protected HttpHeaders buildHttpHeaders(String url, HttpMethod httpMethod, Object params, HttpHeaders headers, Object[] uriparams) {
        if(headers == null){
            headers = new HttpHeaders();
        }

        if(uriparams != null && uriparams.length > 0){
            url= (new DefaultUriTemplateHandler()).expand(url,uriparams).toString();
        }
        try {
            URL urlx = new URL(url);
            url = urlx.getPath();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        String timestamp =  DateUtils.formatDate(new Date(),"yyyyMMddHHmmss");
        if (StringUtil.isNotEmpty(this.certId)) {
            String signature = signature(url,httpMethod.toString(),timestamp,params,MediaType.APPLICATION_XML_VALUE);
            headers.set("Authorization",this.certId + ":"+ signature);
            headers.set("Timestamp",timestamp);
            headers.setContentType(MediaType.APPLICATION_XML);
        }
        return headers;
    }


    /**
     * 签名算法
     * @param url
     * @param httpMethod
     * @param payload
     * @param contentType
     * @param timestamp
     * @return
     */
    private String signature(String url, String httpMethod,String timestamp, Object payload,String contentType) {
        Md5PasswordEncoder md5=md5 = new Md5PasswordEncoder();;
        String contentMd5 = (payload != null) ? md5.encodePassword(payload.toString(), null) : "";
        if(payload == null){
            contentType="";
        }
        StringBuilder toSign = new StringBuilder();
        toSign.append(httpMethod.toString()).append("\n")
                .append(contentMd5).append("\n")
                .append(contentType).append("\n")
                .append(timestamp).append("\n")
                .append(url);

        String secretKey = "d20693215fe07007757dc27aa715b1d2";
        if(logger.isDebugEnabled()){
            logger.debug("签名数据：{}", toSign);

            logger.debug("签名密钥：{}",secretKey);
        }
        String result = calculateHMAC(secretKey,toSign.toString());

        if(logger.isDebugEnabled()){
            logger.debug("签名结果：{}",result);
        }
        return result;
    }

    private static String calculateHMAC(String secret, String data) {
        try {
            SecretKeySpec signingKey = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(signingKey);
            byte[] rawHmac = mac.doFinal(data.getBytes());
            String result = new String(Base64.encodeBase64(rawHmac));
            return result;
        } catch (GeneralSecurityException e) {
            throw new IllegalArgumentException();
        }
    }
}
