package com.lsxy.app.portal.test;

import com.lsxy.app.portal.MainClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import javax.annotation.security.RunAs;
import java.awt.print.Book;

/**
 * Created by Tandy on 2016/6/21.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MainClass.class)
@WebIntegrationTest("server.port:0")
public class RestClientTest {

    @Value("${local.server.port}")
    private int port;


    private RestTemplate restTemplate = new TestRestTemplate();

    @Test
    public void login() {
        restTemplate.postForObject()
        Book book = restTemplate.getForObject("http://localhost:" + port +"/login", Book.class);
    }

}
