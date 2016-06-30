package com.lsxy.app.portal.test;

import com.lsxy.app.portal.MainClass;
import com.lsxy.framework.api.tenant.model.Account;
import com.lsxy.framework.web.rest.RestRequest;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.framework.web.rest.UserRestToken;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Tandy on 2016/6/21.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MainClass.class)
@WebIntegrationTest("server.port:0")
public class RestClientTest {

    @Value("http://localhost:${local.server.port}/")
    private String restPrefixUrl;


    private RestTemplate restTemplate = new TestRestTemplate();

//    @Test
    public void login() throws UnsupportedEncodingException {
        String url = restPrefixUrl + "/login";
        Map<String,Object> formParams = new HashMap<>();
        formParams.put("username","tanchang");
        formParams.put("password","123");

        RestResponse<?> x = RestRequest.buildRequest().post(url,formParams,UserRestToken.class);

//        MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<>();
//        formParams.keySet().stream().forEach(key -> requestEntity.add(key, MapUtils.getString(formParams, key, "")));
//        String restResponse = restTemplate.postForObject(url,requestEntity,String.class);
    }

    @Test
    public void account(){
        String url = restPrefixUrl + "/rest/account/";
        String token = "1234";
        RestResponse<Account> x = RestRequest.buildSecurityRequest(token).get(url, Account.class);
        System.out.println(x);

    }

}
