package com.lsxy.framework.monitor.test;

import com.lsxy.framework.core.test.SpringBootTestCase;
import com.lsxy.framework.monitor.FrameworkMonitorConfig;
import com.lsxy.framework.monitor.MonitorFactory;
import com.lsxy.framework.monitor.SystemMonitor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

/**
 * Created by tandy on 16/9/29.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(value={FrameworkMonitorConfig.class,MonitorTest.class})
public class MonitorTest extends SpringBootTestCase{

    @Autowired
    private MonitorFactory monitorFactory;

    @Test
    public void testRunMonitor(){
        Assert.notNull(monitorFactory);
        Assert.isTrue(monitorFactory.getMonitors().size()>0);
        Assert.isTrue(monitorFactory.getMonitors().get(0) instanceof SystemMonitor);
    }

    @Override
    protected String getSystemId() {
        return "app.monitor.test";
    }
}
