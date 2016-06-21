package com.lsxy.app.portal.test;

import com.lsxy.app.portal.MainClass;
import com.lsxy.framework.web.rest.RestResponse;
import org.apache.commons.collections.MapUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.security.RunAs;
import java.awt.print.Book;
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

    @Test
    public void login() {
        String url = restPrefixUrl + "/login";
        Map<String,String> formParams = new HashMap<>();
        formParams.put("username","tanchang");
        formParams.put("password","123");

        MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<>();
        formParams.keySet().stream().forEach(key -> requestEntity.add(key, MapUtils.getString(formParams, key, "")));
        String restResponse = restTemplate.postForObject(url,requestEntity,String.class);
        System.out.println(restResponse);
    }

}
