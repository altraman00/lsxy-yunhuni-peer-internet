package com.lsxy.call.center.test;

import com.lsxy.call.center.CallCenterMainClass;
import com.lsxy.framework.cache.exceptions.TransactionExecFailedException;
import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.framework.config.Constants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by liuws on 2016/11/11.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(CallCenterMainClass.class)
public class RedisTest {

    @Autowired
    private RedisCacheService redisCacheService;

    static {
        //将 spring boot 的默认配置文件设置为系统配置文件
        System.setProperty("spring.config.location","classpath:"+ Constants.DEFAULT_CONFIG_FILE);
    }
    @Test
    public void test(){
        redisCacheService.zadd("callcenter.acs_40288ae2585219bd01585219e7010001","fwegw222",11);
        Double score = redisCacheService
                .zScore("callcenter.acs_40288ae2585219bd01585219e7010001",
                        "40288ae2585219bd01585219e7f2000c");

        System.out.println("============================");
        System.out.println(score);
    }

    @Test
    public void test2() throws TransactionExecFailedException {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            redisCacheService.setTransactionFlag(start + "test2221" + i,"test2221",300);
        }
        System.out.println(System.currentTimeMillis() - start);
        System.out.println("=======================");
    }
}
