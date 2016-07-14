package com.lsxy.framework.oss.test;

import com.lsxy.framework.oss.FrameworkOSSConfig;
import com.lsxy.framework.oss.OSSService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

/**
 * Created by Tandy on 2016/7/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = FrameworkOSSConfig.class)
public class AliClientTest {
    @Autowired
    private OSSService ossService;

    @Test
    public void test001(){
        Assert.notNull(ossService);
    }
}
