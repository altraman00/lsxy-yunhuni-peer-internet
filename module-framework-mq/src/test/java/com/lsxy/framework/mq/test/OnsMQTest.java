package com.lsxy.framework.mq.test;

import com.lsxy.framework.cache.FrameworkCacheConfig;
import com.lsxy.framework.mq.FrameworkMQConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by tandy on 16/7/21.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Import(value={FrameworkMQConfig.class, FrameworkCacheConfig.class})
@EnableAutoConfiguration
@SpringApplicationConfiguration(OnsMQTest.class)
public class OnsMQTest {

    @Test
    public void test001(){

    }

}
