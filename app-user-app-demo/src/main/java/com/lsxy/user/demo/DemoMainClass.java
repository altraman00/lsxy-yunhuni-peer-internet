package com.lsxy.user.demo;

import com.lsxy.framework.web.web.AbstractSpringBootStarter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.annotation.PostConstruct;

import static com.lsxy.framework.cache.FrameworkCacheConfig.logger;

/**
 * Created by tandy on 16/8/8.
 */
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@EnableAsync
@EnableWebMvc
public class DemoMainClass extends AbstractSpringBootStarter{
    @Override
    public String systemId() {
        return "user-demo";
    }

    @PostConstruct
    public void startCallTask(){

        int threadCount = 100;
        int execCountPerThread = 100;

        while(threadCount -- >0){
            MakeCallTestTask task = new MakeCallTestTask(execCountPerThread);
            Thread t = new Thread(task);
            t.start();
        }
        if(logger.isDebugEnabled()){
            logger.debug("============线程启动完毕=============");
        }
    }

    public static void main(String[] args) throws Exception {

        SpringApplication.run(DemoMainClass.class, args);
    }
}
