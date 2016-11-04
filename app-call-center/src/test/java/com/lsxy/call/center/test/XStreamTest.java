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
                            "            waitPlayFile=\"music.wav\"\n" +
                            "            playAgentNum=\"true\"\n" +
                            "            preAgentNumPlayFile=\"坐席.wav\"\n" +
                            "            postAgentNumPlayFile=\"为您服务.wav\"\n" +
                            "            holdPlayFile=\"wait.wav\"\n" +
                            "            conversationTimeout=\"3600\"\n" +
                            "            data=\"my queue data\"\n" +
                            ">\n" +
                            "    <filter data=\"this is filter 1\">\n" +
                            "        <condition timeout=\"20\" priority=\"60\" data=\"condition 1\">\n" +
                            "            <where>\n" +
                            "                <![CDATA[\n" +
                            "                '投诉' in skills and skills['手机'] > 60.0\n" +
                            "                ]]>\n" +
                            "            </where>\n" +
                            "            <sort>\n" +
                            "                <![CDATA[\n" +
                            "                (skills['投诉'] * 0.4 + skills['手机'] * 0.6) / 2.0\n" +
                            "                ]]>\n" +
                            "            </sort>\n" +
                            "        </condition>\n" +
                            "    </filter>\n" +
                            "</enqueue>");
                    System.out.println(JSONUtil.objectToJson(equeue));
                    countDownLatch.countDown();
                }
            }).start();
        }
        countDownLatch.await();
    }

}
