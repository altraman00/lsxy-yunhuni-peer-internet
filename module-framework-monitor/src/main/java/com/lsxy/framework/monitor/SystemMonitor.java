package com.lsxy.framework.monitor;

import com.lsxy.framework.core.utils.StringUtil;
import org.springframework.stereotype.Component;

/**
 * Created by tandy on 16/9/29.
 * 默认提供的系统监控指标
 */
@Component
public class SystemMonitor extends AbstractMonitor {

    public String fetch(){
        String version = this.getClass().getPackage().getImplementationVersion();
        if(StringUtil.isEmpty(version)){
            version = "1.2.1";
        }
        return version;
    }

    @Override
    public String getName() {
        //此处为空表示直接使用前缀作为系统默认监控
        return "";
    }
}
