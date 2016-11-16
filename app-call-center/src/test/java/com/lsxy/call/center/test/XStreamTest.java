package com.lsxy.call.center.test;

import com.lsxy.call.center.api.model.EnQueue;
import com.lsxy.call.center.api.utils.EnQueueDecoder;
import com.lsxy.framework.core.utils.JSONUtil;
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
        CountDownLatch countDownLatch = new CountDownLatch(1);
        for(int i=0;i<1;i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    EnQueue equeue = EnQueueDecoder.decode("<enqueue\n" +
                            "        channel=\"channel1\"\n" +
                            "        data=\"your or data whatever here!\"\n" +
                            ">\n" +
                            "    <route>\n" +
                            "        <condition id=\"condition1\"/>\n" +
                            "    </route>\n" +
                            "</enqueue>");
                    System.out.println(JSONUtil.objectToJson(equeue));
                    countDownLatch.countDown();
                }
            }).start();
        }
        countDownLatch.await();
    }

}
