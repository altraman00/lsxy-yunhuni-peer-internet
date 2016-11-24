package com.lsxy.area.server;

import com.lsxy.framework.core.utils.JSONUtil2;
import com.lsxy.framework.monitor.AbstractMonitor;
import com.lsxy.framework.rpc.api.session.SessionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by tandy on 16/9/29.
 */
@Component
public class AreaServerSessionContextMonitor extends AbstractMonitor {
    @Override
    public String getName() {
        return "sessionContext";
    }

    @Autowired
    private SessionContext sessionContext;

    @Override
    public String fetch() {
        return sessionContext.toString();
    }
}
