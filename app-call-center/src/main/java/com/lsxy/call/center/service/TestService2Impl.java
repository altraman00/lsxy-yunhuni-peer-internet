package com.lsxy.call.center.service;

import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.stereotype.Component;

/**
 * Created by liuws on 2016/10/18.
 */
@Service
@Component
public class TestService2Impl implements TestService2{

    public void test(){
        System.out.println("11111111111111");
    }
}
