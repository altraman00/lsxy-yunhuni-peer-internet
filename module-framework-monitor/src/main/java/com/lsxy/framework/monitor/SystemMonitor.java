package com.lsxy.framework.monitor;

import org.springframework.stereotype.Component;

/**
 * Created by tandy on 16/9/29.
 * 默认提供的系统监控指标
 */
@Component
public class SystemMonitor extends AbstractMonitor {

    public String fetch(){
        return System.getProperty("systemId","");
    }

    @Override
    public String getName() {
        //此处为空表示直接使用前缀作为系统默认监控
        return "";
    }
}
