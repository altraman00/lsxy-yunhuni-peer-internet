package com.lsxy.framework.api.test;

/**
 * Created by tandy on 16/8/16.
 */
public interface TestService {
    public String sayHi(String name);

    public void rpcPresureTest(int threads,int count);


    /**
     * 压力测试使用
     * @param threads
     */
    public void testPresure(int threads);
}
