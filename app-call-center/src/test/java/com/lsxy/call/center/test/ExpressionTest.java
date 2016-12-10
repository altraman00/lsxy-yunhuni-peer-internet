package com.lsxy.call.center.test;

import com.lsxy.call.center.CallCenterMainClass;
import com.lsxy.call.center.utils.ExpressionUtils;
import com.lsxy.framework.config.Constants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * Created by liuws on 2016/11/8.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(CallCenterMainClass.class)
public class ExpressionTest {

    static {
        //将 spring boot 的默认配置文件设置为系统配置文件
        System.setProperty("spring.config.location","classpath:"+ Constants.DEFAULT_CONFIG_FILE);
    }
    @Test
    public void test() throws InterruptedException {
        int size = 1000;
        long start = System.currentTimeMillis();
        CountDownLatch countDownLatch = new CountDownLatch(size);
        for(int i=0;i<size;i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Map<String,Integer> vars = new HashMap<>();
                    vars.put("haha0",11);
                    vars.put("haha1",22);
                    System.out.println(ExpressionUtils.execSortExpression("get(\"haha0\") + get(\"haha1\");",vars));
                    countDownLatch.countDown();
                }
            }).start();
        }
        countDownLatch.await();
        System.out.println(System.currentTimeMillis() - start);
    }

    @Test
    public void test2(){
        System.out.println(ExpressionUtils.validWhereExpression("has(\"技能一\") && get(\"技能2\") == 60;"));;
    }
}
