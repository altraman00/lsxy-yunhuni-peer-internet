package test;

import com.lsxy.framework.web.rest.RestRequest;
import com.lsxy.framework.web.rest.RestResponse;
import gateway.MainClass;
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
@SpringApplicationConfiguration(classes= MainClass.class)
@WebIntegrationTest("server.port:0")
public class CallControllerTest {

    @Value("http://localhost:${local.server.port}/")
    private String restPrefixUrl;

    @Test
    public void testDoCall(){
        String url = restPrefixUrl + "/v1/account/12345678/call";
        RestResponse<String> response = RestRequest.buildRequest().get(url,String.class);
        Assert.notNull(response);
        Assert.isTrue(response.getData().equals("12345678"));
    }

    @Test
    public void testGetCall(){
        String url = restPrefixUrl + "/v1/account/12345678/call/88888888";
        RestResponse<String> response = RestRequest.buildRequest().get(url,String.class);
        Assert.notNull(response);
        Assert.isTrue(response.getData().equals("88888888"));
    }


}
