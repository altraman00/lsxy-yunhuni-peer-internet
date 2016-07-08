package com.lsxy.app.api.gateway.rest.test;

import com.lsxy.app.api.gateway.MainClass;
import com.lsxy.app.api.gateway.security.auth.ASyncSaveApiLogTask;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Tandy on 2016/7/7.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MainClass.class)
public class ApplicationTests {
    @Autowired
    private ASyncSaveApiLogTask task;


}
