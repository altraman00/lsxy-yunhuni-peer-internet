package com.lsxy.area.server;

import com.lsxy.framework.mq.FrameworkMQConfig;
import com.lsxy.framework.rpc.FrameworkRPCConfig;
import com.lsxy.framework.rpc.exceptions.RemoteServerStartException;
import com.lsxy.framework.web.web.AbstractSpringBootStarter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by tandy on 16/7/19.
 */
@SpringBootApplication
@ComponentScan
@Import(value={FrameworkRPCConfig.class, FrameworkMQConfig.class})
@EnableScheduling
public class AreaServerMainClass extends AbstractSpringBootStarter{

    @Override
    public String systemId() {
        return "area.server";
    }

    public static void main(String[] args) throws RemoteServerStartException {
        SpringApplication.run(AreaServerMainClass.class);

    }
}
