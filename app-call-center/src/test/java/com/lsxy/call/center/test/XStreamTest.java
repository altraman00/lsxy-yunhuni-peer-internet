package com.lsxy.call.center.test;

import com.lsxy.call.center.api.model.EnQueue;
import com.lsxy.call.center.api.utils.EnQueueDecoder;
import com.lsxy.framework.core.utils.JSONUtil;
import com.lsxy.framework.core.utils.JSONUtil2;
import com.thoughtworks.xstream.XStream;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.concurrent.CountDownLatch;

/**
 * Created by liuwensheng on 2016/10/30.
 */
@RunWith(JUnit4.class)
public class XStreamTest {

    @Test
    public void test() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(100);
        for(int i=0;i<100;i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    EnQueue equeue = EnQueueDecoder.decode("<equeue></equeue>");
                    countDownLatch.countDown();
                }
            }).start();
        }
        countDownLatch.await();
    }

}
