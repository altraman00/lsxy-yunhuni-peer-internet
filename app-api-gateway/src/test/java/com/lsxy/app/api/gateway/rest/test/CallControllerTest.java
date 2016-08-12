package com.lsxy.app.api.gateway.rest.test;

import com.lsxy.app.api.gateway.APIGWMainClass;
import com.lsxy.app.api.gateway.StasticsCounter;
import com.lsxy.framework.config.Constants;
import com.lsxy.framework.web.rest.RestRequest;
import com.lsxy.framework.web.rest.RestResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    private static final Logger logger = LoggerFactory.getLogger(CallControllerTest.class);
    static {
//将 spring boot 的默认配置文件设置为系统配置文件
        System.setProperty("spring.config.location","classpath:"+ Constants.DEFAULT_CONFIG_FILE);
    }

    @Value("http://localhost:${local.server.port}/")
    private String restPrefixUrl;

    @Autowired
    private StasticsCounter sc;

    @Before
    public void init(){

    }

//    @Test
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

    @Test
    public void testDoPresure(){
        for(int i=0;i<100;i++){
            Thread thread = new Thread(new RunTask());
            thread.start();

            if(logger.isDebugEnabled()){
                logger.debug("启动测试线程");
            }
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    class RunTask implements  Runnable{
        private int count = 100;
        private String url = restPrefixUrl + "test/mq/presure";
        @Override
        public void run() {
            while(true) {
                if (logger.isDebugEnabled()) {
                    logger.debug("开始发起请求");
                }
                RestRequest request = RestRequest.buildRequest();
                RestResponse<String> response = request.get(url, String.class);

                if (logger.isDebugEnabled()) {
                    logger.debug("请求完毕");
                }
                Assert.notNull(response);
                Assert.isTrue(response.getData().equals("OK"));
            }
        }
    }






}
