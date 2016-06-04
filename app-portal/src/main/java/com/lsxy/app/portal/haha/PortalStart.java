package com.lsxy.app.portal.haha;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Tandy on 2016/6/4.
 */
@SpringBootApplication
//@EnableAutoConfiguration
//@RestController
@Controller
public class PortalStart {

    @ResponseBody
    @RequestMapping("/home")
    String home(){
        return "hello";
    }

    public static void main(String[] args) {
        SpringApplication.run(PortalStart.class,args);
    }
}
