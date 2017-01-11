package com.lsxy.call.center.test;

import com.lsxy.call.center.CallCenterMainClass;
import com.lsxy.call.center.api.model.CallCenter;
import com.lsxy.call.center.api.model.CallCenterQueue;
import com.lsxy.call.center.api.model.EnQueue;
import com.lsxy.call.center.api.service.CallCenterQueueService;
import com.lsxy.call.center.api.service.CallCenterService;
import com.lsxy.call.center.api.service.EnQueueService;
import com.lsxy.call.center.api.utils.EnQueueDecoder;
import com.lsxy.framework.config.Constants;
import com.lsxy.framework.core.utils.UUIDGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by liuws on 2016/12/12.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(CallCenterMainClass.class)
public class EnqueueServiceTest {

    static {
        //将 spring boot 的默认配置文件设置为系统配置文件
        System.setProperty("spring.config.location","classpath:"+ Constants.DEFAULT_CONFIG_FILE);
    }
    @Autowired
    private EnQueueService enQueueService;

    @Autowired
    private CallCenterQueueService callCenterQueueService;

    @Autowired
    private CallCenterService callCenterService;

    @Test
    public void test() throws InterruptedException {
        String enqueue = "<enqueue\n" +
                "        channel=\"40288ae258f61b690158f61b86d90000\"\n" +
                "        wait_voice=\"3.wav\"\n" +
                "        ring_mode=\"4\"\n" +
                "        play_num=\"true\"\n" +
                "        pre_num_voice=\"坐席.wav\"\n" +
                "        post_num_voice=\"为您服务.wav\"\n" +
                "        data=\"your data whatever here!\">\n" +
                "    <route>\n" +
                "        <condition id=\"40288ae258f61b690158f61b94370259\"/>\n" +
                "    </route>\n" +
                "</enqueue>\n";
        EnQueue en = EnQueueDecoder.decode(enqueue);
        long start111 = System.currentTimeMillis();
        int size = 10;
        CountDownLatch countDownLatch = new CountDownLatch(size);
        for (int i = 0; i < size; i++) {
            Thread.currentThread().sleep(5);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    long start = System.currentTimeMillis();
                    enQueueService.lookupAgent("40288ac9575612a30157561c7ff50004",
                            "40288ac957e1812e0157e18a994e0000","13692206627","callid1",en,null,null);
                    System.out.println("好事：="+(System.currentTimeMillis() - start));
                    countDownLatch.countDown();
                }
            }).start();
        }
        countDownLatch.await();
        System.out.println("好诗================="+(System.currentTimeMillis()-start111));
        Thread.currentThread().join();
    }

    @Test
    public  void test2() throws InterruptedException {
        long count = callCenterQueueService.count();
        final List<String> qs = new ArrayList<>();
        int pageSize= 1000;
        long pageCount = (long)Math.ceil(count*1.0 / pageSize);
        for (int i = 0; i < pageCount; i++) {
           List<CallCenterQueue> a =callCenterQueueService.pageList(i+1,pageSize).getResult();
            for (CallCenterQueue q: a) {
                qs.add(q.getId());
            }
        }

        long start111 = System.currentTimeMillis();
        CountDownLatch countDownLatch = new CountDownLatch((int)qs.size());
        for (int i = 0; i < qs.size(); i++) {
            Thread.currentThread().sleep(5);
            final int j = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    long start = System.currentTimeMillis();
                    CallCenterQueue callCenterQueue = callCenterQueueService.findById(qs.get(j));
                    if(callCenterQueue == null){
                        return;
                    }
                    Date cur = new Date();
                    callCenterQueue.setRelevanceId("111");
                    callCenterQueue.setConversation("222222222");
                    callCenterQueue.setAgent("333333");
                    callCenterQueue.setAgentCallId("444444444");
                    callCenterQueue.setInviteTime(cur);
                    callCenterQueue.setEndTime(cur);
                    callCenterQueue.setToManualTime((callCenterQueue.getEndTime().getTime() - callCenterQueue.getStartTime().getTime()) / 1000);
                    callCenterQueue.setResult("");
                    callCenterQueueService.save(callCenterQueue);
                    System.out.println("好事：="+(System.currentTimeMillis() - start));
                    countDownLatch.countDown();
                }
            }).start();
        }
        countDownLatch.await();
        System.out.println("好诗================="+(System.currentTimeMillis()-start111));
        Thread.currentThread().join();
    }

    @Test
    public void test3() throws InterruptedException {
        long start111 = System.currentTimeMillis();
        int size = 10;
        CountDownLatch countDownLatch = new CountDownLatch(size);
        for (int i = 0; i < size; i++) {
            Thread.currentThread().sleep(1);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    long start = System.currentTimeMillis();
                    CallCenter callCenter = new CallCenter();
                    callCenter.setTenantId("11111111111111111111111111111111");
                    callCenter.setAgent("11111111111111111111111111111111");
                    callCenter.setTenantId("11111111111111111111111111111111");
                    callCenterService.save(callCenter);
                    System.out.println("好事：="+(System.currentTimeMillis() - start));
                    countDownLatch.countDown();
                }
            }).start();
        }
        countDownLatch.await();
        System.out.println("好诗================="+(System.currentTimeMillis()-start111));
        Thread.currentThread().join();
    }


    @Test
    public void test4() throws InterruptedException {
        long start  = System.currentTimeMillis();
        List<CallCenterQueue> stack = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            CallCenterQueue queue = new CallCenterQueue();
            queue.setId(UUIDGenerator.uuid());
            queue.setTenantId("11");
            queue.setAgent("222");
            queue.setAppId("333");
            queue.setEnqueue("gggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggg");
            stack.add(queue);
            callCenterQueueService.save(queue);
        }
//        callCenterQueueService.save(stack);
        System.out.println("耗时："+(System.currentTimeMillis() - start));
        Thread.currentThread().join();
    }

    @Test
    public void test5(){
        System.out.println(callCenterQueueService.findById("40288ae2590041a0015900434f8a0004"));;
        System.out.println(callCenterQueueService.findById("40288ae2590041a0015900434f8a0004"));;
        System.out.println(callCenterQueueService.findById("40288ae2590041a0015900434f8a0004"));;
    }


    @Test
    public  void test6(){
        callCenterService.incrCost("222",new BigDecimal(1));
    }
}


