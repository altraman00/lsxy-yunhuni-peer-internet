package com.lsxy.area.server;

import com.lsxy.framework.rpc.api.server.ServerSessionContext;
import com.lsxy.framework.rpc.api.session.SessionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by tandy on 16/9/19.
 */
@Configuration
public class AreaServerSessionContextConfig {
    @Autowired
    private AreaServerSelectSessionPolicy areaServerSelectSessionPolicy;

    @Bean(name = "sessionContext")
    public SessionContext getSessionContext(){
        SessionContext sessionContext = new ServerSessionContext();
        areaServerSelectSessionPolicy.setSessionContext(sessionContext);
        sessionContext.setSelectSessionPolicy(areaServerSelectSessionPolicy);
        return sessionContext;
    }
}
