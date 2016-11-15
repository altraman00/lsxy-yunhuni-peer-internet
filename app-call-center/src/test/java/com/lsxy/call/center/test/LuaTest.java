package com.lsxy.call.center.test;

import com.lsxy.call.center.CallCenterMainClass;
import com.lsxy.call.center.states.statics.CAs;
import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.framework.config.Constants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by liuws on 2016/11/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(CallCenterMainClass.class)
public class LuaTest {

    @Autowired
    private RedisCacheService redisCacheService;

    static {
        //将 spring boot 的默认配置文件设置为系统配置文件
        System.setProperty("spring.config.location","classpath:"+ Constants.DEFAULT_CONFIG_FILE);
    }


    @Test
    public void test(){
        String script = "local haha = redis.call('ZREVRANGE',KEYS[1],0,-1)\n" +
                "local num = 0\n" +
                "for var=1,#haha do  \n" +
                "    num=num+1\n" +
                "end \n" +
                "return num";

        System.out.println(redisCacheService.eval(script,1, CAs.getKey("40288ae25865c111015865c146d1000b")));
    }
}
