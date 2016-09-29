package com.lsxy.framework.monitor;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by tandy on 16/9/29.
 */
@Component
public class MonitorFactory implements InitializingBean{

    private static final Logger logger = LoggerFactory.getLogger(MonitorFactory.class);

    private String scanPackages = "com.lsxy";

    private List<AbstractMonitor> monitors = new ArrayList<>();

    @Autowired
    private ApplicationContext applicationContext;

    public List<AbstractMonitor> getMonitors() {
        return monitors;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Reflections reflections = new Reflections(scanPackages);
        logger.info("系统监控线程启动");
        Set<Class<? extends AbstractMonitor>> monitorClasses = reflections.getSubTypesOf(AbstractMonitor.class);
        logger.info("找到 "+monitorClasses.size()+" 个监控器,并开始监控");
        for (Class<? extends AbstractMonitor> monitorClass : monitorClasses) {
            AbstractMonitor monitor = applicationContext.getBean(monitorClass);
            monitors.add(monitor);
        }
    }

}
