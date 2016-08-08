package com.lsxy.app.api.gateway.rest.test;

import com.lsxy.app.api.gateway.APIGWMainClass;
import com.lsxy.framework.web.rest.RestResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

/**
 * Created by Tandy on 2016/6/28.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes= APIGWMainClass.class)
@WebIntegrationTest("server.port:0")
public class CallControllerTest {

    @Value("http://localhost:${local.server.port}/")
    private String restPrefixUrl;

    @Test
    public void testDoCall(){
        String url = restPrefixUrl + "v1/account/12345678/calltest";
        String certid = "1";

        RestResponse<String> response = APIGWRestRequest.buildSecurityRequest(certid).get(url,String.class);
        Assert.notNull(response);
        Assert.isTrue(response.getData().equals("12345678"));


        RestResponse<String> response2 = APIGWRestRequest.buildSecurityRequest(certid).post(url,"<xml><result>1</result></xml>",String.class);
        Assert.notNull(response2);
        Assert.isTrue(response2.getData().equals("12345678"));


    }






}
