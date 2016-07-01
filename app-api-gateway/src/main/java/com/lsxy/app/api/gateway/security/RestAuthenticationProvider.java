package com.lsxy.app.api.gateway.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;

import static org.apache.coyote.http11.Constants.a;

/**
 * Created by Tandy on 2016/7/1.
 */
public class RestAuthenticationProvider implements AuthenticationProvider {

//    @Autowired
//    private cert service;

    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        RestToken restToken = (RestToken) authentication;

//        // api key (aka username)
//        String apiKey = restToken.getPrincipal();
//        // hashed blob
//        RestCredentials credentials = restToken.getCredentials();
//
//        // get secret access key from api key
//        String secret = service.loadSecretByUsername(apiKey);
//
//        // if that username does not exist, throw exception
//        if (secret == null) {
//            throw new BadCredentialsException("Invalid username or password.");
//        }
//
//        // calculate the hmac of content with secret key
//        String hmac = calculateHMAC(secret, credentials.getRequestData());
//        // check if signatures match
//        if (!credentials.getSignature().equals(hmac)) {
//            throw new BadCredentialsException("Invalid username or password.");
//        }
//
//        // this constructor create a new fully authenticated token, with the "authenticated" flag set to true
//        // we use null as to indicates that the user has no authorities. you can change it if you need to set some roles.
//        restToken = new RestToken(user, credentials, restToken.getTimestamp(), null);
//
//        return restToken;
        return null;
    }

    public boolean supports(Class<?> authentication) {
        return RestToken.class.equals(authentication);
    }

//    private String calculateHMAC(String secret, String data) {
//        try {
//            SecretKeySpec signingKey = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
//            Mac mac = Mac.getInstance("HmacSHA256");
//            mac.init(signingKey);
//            byte[] rawHmac = mac.doFinal(data.getBytes());
//            String result = new String(Base64.encodeBase64(rawHmac));
//            return result;
//        } catch (GeneralSecurityException e) {
//            throw new IllegalArgumentException();
//        }
//    }
}