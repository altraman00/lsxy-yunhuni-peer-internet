package com.lsxy.framework.cache.test;

import com.lsxy.framework.cache.FrameworkCacheConfig;
import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.framework.core.test.SpringBootTestCase;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by tandy on 17/1/5.
 */

@Import(value={FrameworkCacheConfig.class})
@SpringApplicationConfiguration(CacheTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@EnableAutoConfiguration
@ComponentScan
public class CacheTest extends SpringBootTestCase {

    @Autowired
    private RedisCacheService cacheService;

    @Override
    protected String getSystemId() {
        return "";
    }

    @Test
    public void test001() throws InterruptedException {
        Assert.assertNotNull(cacheService);
        cacheService.set("a","3");
        Assert.assertTrue(cacheService.get("a").equals("3"));
        cacheService.set("a","1");
        Assert.assertTrue(cacheService.get("a").equals("1"));

        int i=0;
        while(i<10000){
            System.out.println("set a="+i);
            try {
                cacheService.set("a", i + "");
                Assert.assertTrue(cacheService.get("a").equals(i + ""));
            }catch(Exception ex){
                System.out.println("出现异常");
            }
            i++;
            Thread.sleep(1000);

        }

    }


}
