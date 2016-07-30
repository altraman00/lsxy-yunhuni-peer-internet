package com.lsxy.area.agent;

import com.lsxy.framework.rpc.FrameworkRPCConfig;
import com.lsxy.framework.rpc.exceptions.RemoteServerStartException;
import com.lsxy.framework.web.web.AbstractSpringBootStarter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by tandy on 16/7/30.
 */
@SpringBootApplication
@ComponentScan
@Import(FrameworkRPCConfig.class)
@EnableScheduling
public class MainClass  extends AbstractSpringBootStarter {

    public static void main(String[] args) throws RemoteServerStartException {
        SpringApplication.run(MainClass.class);
    }

    @Override
    public String systemId() {
        return "area-agent";
    }
}
