package com.lsxy.area.agent;

import java.util.concurrent.TimeUnit;

/**
 * Created by tandy on 16/8/6.
 */
public class TestClass {


    private Request request = new Request();

    public void test001(){
        OtherThread002 ot = new OtherThread002(this.request);
        Thread xx = new Thread(ot);
        xx.start();
    }

    private void test002(){
        OtherThread001 ot = new OtherThread001(this.request);
        Thread xx = new Thread(ot);
        xx.start();
    }

    public static void main(String[] args) {
        TestClass tc = new TestClass();
        tc.test001();
        tc.test002();;
    }


    class Request{

    }



    class OtherThread002 implements  Runnable {
        public OtherThread002(Request request){
            this.request = request;
        }
        private Request request;

        @Override
        public void run() {
            synchronized (request){
                System.out.println("开始阻塞");
                try {
                    request.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ;
                System.out.println("阻塞解除,继续");
            }
        }
    }

    class OtherThread001 implements  Runnable{

        private Request request;

        public OtherThread001(Request request){
            this.request = request;
        }

        @Override
        public void run() {
            int i = 0;
            while(i++<10){
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("睡觉中。。。。");
            }
            System.out.println("该醒啦。。。。");
            synchronized (request){
                request.notify();
            }
            System.out.println("已经通知,本线程结束");
        }

    }
}
