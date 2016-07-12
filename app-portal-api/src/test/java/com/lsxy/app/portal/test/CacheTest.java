package com.lsxy.app.portal.test;

import com.lsxy.app.portal.MainClass;
import com.lsxy.framework.web.rest.RestRequest;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.framework.web.rest.UserRestToken;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tandy on 2016/7/7.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MainClass.class)
@WebIntegrationTest("server.port:0")
public class CacheTest {
    @Value("http://localhost:${local.server.port}/")
    private String restPrefixUrl;


    @Test
    public void testCache(){
        String token = login();
        String url = restPrefixUrl + "rest/cache/get/1";
        RestResponse response = RestRequest.buildSecurityRequest(token).get(url,String.class);
        Assert.assertTrue(response.getData().equals("1"));

    }

    public String login()  {
        String url = restPrefixUrl + "/login";
        Map<String,Object> formParams = new HashMap<>();
        formParams.put("username","user001");
        formParams.put("password","password");

        RestResponse<UserRestToken> response = RestRequest.buildRequest().post(url,formParams,UserRestToken.class);
        return response.getData().getToken();
    }

}
