package com.lsxy.call.center.test;

import com.lsxy.call.center.CallCenterMainClass;
import com.lsxy.call.center.api.model.CallCenterAgent;
import com.lsxy.call.center.api.model.Condition;
import com.lsxy.call.center.api.service.ConditionService;
import com.lsxy.call.center.states.lock.AgentLock;
import com.lsxy.call.center.states.lock.QueueLock;
import com.lsxy.call.center.states.state.AgentState;
import com.lsxy.call.center.states.state.ExtensionState;
import com.lsxy.call.center.states.statics.ACs;
import com.lsxy.call.center.states.statics.CAs;
import com.lsxy.call.center.states.statics.CQs;
import com.lsxy.framework.cache.utils.Lua;
import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.framework.config.Constants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import java.util.List;

import java.util.concurrent.CountDownLatch;

/**
 * Created by liuws on 2016/11/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(CallCenterMainClass.class)
public class LuaTest {

    @Autowired
    private RedisCacheService redisCacheService;

    static {
        //将 spring boot 的默认配置文件设置为系统配置文件
        System.setProperty("spring.config.location","classpath:"+ Constants.DEFAULT_CONFIG_FILE);
    }

    @Autowired
    private CAs cAs;

    @Autowired
    private ConditionService conditionService;



    @Test
    public void test(){
        String script = "local acs = redis.call('ZREVRANGE',KEYS[1],0,-1)\n" +
                "local result = nil\n" +
                "for i=0,#acs do  \n" +
                "    local agent_state = redis.call('HGET',KEYS[2]..acs[i+1],'hehe')\n" +
                "\tresult = agent\n" +
                "\tbreak\n" +
                "end \n" +
                "return result";

        System.out.println(redisCacheService.eval(script,2, CAs.getKey("40288ae25865c111015865c146d1000b"), AgentState.getPrefixed()));
    }

    @Test
    public void test1() throws InterruptedException {
        List<Condition> conditions = conditionService.pageList(1,10000).getResult();
        Condition condition = conditions.get(conditions.size()-1);
        long start1 = System.currentTimeMillis();
        int size = 1;
        CountDownLatch latch = new CountDownLatch(size);
        for (int i = 0; i < size; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    long start = System.currentTimeMillis();
                    System.out.println(redisCacheService.eval(Lua.LOOKUPAGENT,4,
                            CAs.getKey(condition.getId()),AgentState.getPrefixed(),
                            ExtensionState.getPrefixed(),AgentLock.getPrefixed(),
                            ""+AgentState.REG_EXPIRE,""+System.currentTimeMillis(),
                            CallCenterAgent.STATE_IDLE,CallCenterAgent.STATE_FETCHING));
                    System.out.println(System.currentTimeMillis() -start);
                    latch.countDown();
                }
            }).start();

        }
        latch.await();
        System.out.println("总："+(System.currentTimeMillis() -start1));
    }

    @Test
    public void test3() throws InterruptedException {
        long start = System.currentTimeMillis();
        String agent = "40288ae2586b014801586b0174080043";
        String queueId = (String)redisCacheService.eval(Lua.LOKUPQUEUE,6,
                ACs.getKey(agent),AgentState.getKey(agent),
                ExtensionState.getPrefixed(),AgentLock.getKey(agent),
                QueueLock.getPrefixed(), CQs.getPrefixed(),
                ""+AgentState.REG_EXPIRE,""+System.currentTimeMillis(),
                CallCenterAgent.STATE_IDLE,CallCenterAgent.STATE_FETCHING);

        System.out.println(queueId);
        System.out.println(System.currentTimeMillis() -start);
    }

    @Test
    public void  test2() throws InterruptedException {
        System.out.println(redisCacheService.eval(Lua.LOOKUPAGENT,1,"1","1111","10"));;
        /*for (int i = 0; i < 5000; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println(redisCacheService.eval(Lua.LOOKUPAGENT,1,"1","1111","10"));;
                }
            }).start();
        }
        Thread.currentThread().join();*/
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            System.out.println(redisCacheService.eval(Lua.LOOKUPAGENT,1,"1","1111","10"));;
        }

        System.out.println("好事="+(System.currentTimeMillis()-start));
    }

    public static void main(String[] args) {
        System.out.println(System.currentTimeMillis());
    }
}
