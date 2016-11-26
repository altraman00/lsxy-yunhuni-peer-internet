package com.lsxy.app.oc.test;

import com.lsxy.app.oc.MainClass;
import com.lsxy.app.oc.open.LoginVO;
import com.lsxy.app.oc.rest.tenant.vo.ConsumesVO;
import com.lsxy.framework.config.Constants;
import com.lsxy.framework.core.utils.JSONUtil;
import com.lsxy.framework.web.rest.RestRequest;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.framework.web.rest.UserRestToken;
import com.lsxy.yunhuni.api.consume.model.Consume;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Tandy on 2016/7/7.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MainClass.class)
@WebIntegrationTest("server.port:0")
public class ConsumerTest {
    @Value("http://localhost:${local.server.port}/")
    private String restPrefixUrl;

    static {
        //将 spring boot 的默认配置文件设置为系统配置文件
        System.setProperty("spring.config.location","classpath:"+ Constants.DEFAULT_CONFIG_FILE);
    }
    @Test
    public void testSendMsg(){
        String token = login();
        String tenantid="8a2bc67257282a250157282b75740000";
        String url = restPrefixUrl + "/tenant/tenants/"+tenantid+"/consumes?year=2016&month=9&pageNo=1&pageSize=10";

//        @RequestParam Integer year,
//        @RequestParam Integer month,
//        @RequestParam(defaultValue = "1") Integer pageNo,
//        @RequestParam(defaultValue = "10") Integer pageSize
//
        RestResponse<ConsumesVO> response = RestRequest.buildSecurityRequest(token).get(url,ConsumesVO.class);
        Assert.notNull(response);
        List<Consume> list = response.getData().getConsumes().getResult();
        Assert.notNull(list);
        Consume consume = list.get(0);
        Assert.notNull(consume);
        System.out.println(JSONUtil.objectToJson(consume));
    }

    public String login()  {
        String url = restPrefixUrl + "/auth/login";
        Map<String,Object> formParams = new HashMap<>();
        formParams.put("userName","user001");
        formParams.put("password","123456");
        LoginVO loginVO = new LoginVO();
        loginVO.setUserName("user001");
        loginVO.setPassword("123456");
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type","application/json");
        RestResponse<UserRestToken> response = RestRequest.buildRequest().post(url,JSONUtil.objectToJson(loginVO),UserRestToken.class,headers);
        return response.getData().getToken();
    }

}
