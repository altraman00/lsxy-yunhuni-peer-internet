package com.lsxy.app.api.gateway.security.auth;

import com.lsxy.framework.config.SystemConfig;
import com.lsxy.yunhuni.api.apicertificate.model.ApiCertificate;
import com.lsxy.yunhuni.api.apicertificate.service.ApiCertificateService;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import java.util.Date;

/**
 * Created by Tandy on 2016/7/1.
 */
@Component
public class RestAuthenticationProvider implements AuthenticationProvider {

    private static final Logger logger = LoggerFactory.getLogger(RestAuthenticationProvider.class);

    @Autowired
    private ApiCertificateService apiCertificateService;

    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        RestToken restToken = (RestToken) authentication;

        if(restToken.getTimestamp() == null){
            if(logger.isDebugEnabled()){
                logger.debug("时间戳参数为空，认证失败：{}",restToken.getPrincipal());
            }
            throw new BadCredentialsException("无效的时间戳参数");
        }
        // api key (aka username)
        String apiKey = restToken.getPrincipal();
        // hashed blob
        RestCredentials credentials = restToken.getCredentials();

        String tenantId = null;
        // get secretKey access key from api key
        String secretKey = null;
        ApiCertificate apiCertificate = apiCertificateService.findApiCertificateSecretKeyByCertId(apiKey);
        if(apiCertificate != null){
            secretKey = apiCertificate.getSecretKey();
            tenantId = apiCertificate.getTenantId();
        }


        if(logger.isDebugEnabled()){
            logger.debug("签名数据：{}",credentials.getRequestData());
            logger.debug("签名密钥：{}",secretKey);
        }

        // if that username does not exist, throw exception
        if (secretKey == null) {
            throw new BadCredentialsException("凭证标识无效:"+apiKey);
        }


        // calculate the hmac of content with secretKey key
        String hmac = calculateHMAC(secretKey, credentials.getRequestData());

        if(logger.isDebugEnabled()){
            logger.debug("数据签名：{}" ,hmac);
        }

        // check if signatures match
        if (!credentials.getSignature().equals(hmac)) {
            if(logger.isDebugEnabled()){
                logger.debug("签名不正确：{},[{}] vs [{}]",restToken.getPrincipal(),credentials.getSignature(),hmac);
            }
            throw new BadCredentialsException("签名不正确");
        }

        if(expire(restToken.getTimestamp())){
            if(logger.isDebugEnabled()){
                logger.debug("签名过期：{},{}",restToken.getPrincipal(),credentials.getSignature());
            }
            throw new BadCredentialsException("签名过期");
        }

        // this constructor create a new fully authenticated token, with the "authenticated" flag set to true
        // we use null as to indicates that the user has no authorities. you can change it if you need to set some roles.
        restToken = new RestToken(apiKey, credentials, restToken.getTimestamp(),tenantId, null);

        return restToken;
    }

    /**
     * 判断请求签名是否过期 过期时间为可配置
     * @param timestamp
     * @return
     */
    private boolean expire(Date timestamp) {
        Date now = new Date();
        long diff = now.getTime()-timestamp.getTime();
        int expiredMinute = Integer.parseInt(SystemConfig.getProperty("api.gateway.call.expired","5"));
        return (diff/1000) > expiredMinute*60;
    }


    public boolean supports(Class<?> authentication) {
        return RestToken.class.equals(authentication);
    }

    /**
     *HmacSHA256签名算法
     * @param secret    秘钥
     * @param data      签名数据
     * @return              签名后数据
     */
    String calculateHMAC(String secret, String data) {
        try {
            long start = System.currentTimeMillis();
            SecretKeySpec signingKey = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(signingKey);
            byte[] rawHmac = mac.doFinal(data.getBytes());
            String result = new String(Base64.encodeBase64(rawHmac));

            if(logger.isDebugEnabled()){
                logger.debug("[{}]签名花费时长:{}ms",secret,(System.currentTimeMillis() - start));
            }
            return result;
        } catch (GeneralSecurityException e) {
            throw new IllegalArgumentException();
        }
    }
}