package com.lsxy.framework.monitor;

import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.StringUtil;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.Date;

/**
 * Created by tandy on 16/9/29.
 * 默认提供的系统监控指标
 */
@Component
public class SystemMonitor extends AbstractMonitor {

    public static final Date systemStartDt = new Date();
    /**
     * 系统监控缓存格式  version starttime
     * @return
     */
    public String fetch(){
        String version = this.getClass().getPackage().getImplementationVersion();
        if(StringUtil.isEmpty(version)){
            version = "1.2.1";
        }
        return version + " " + systemStartDt.getTime();
    }

    @Override
    public String getName() {
        //此处为空表示直接使用前缀作为系统默认监控
        return "";
    }

    public static void main(String[] args) {
        RuntimeMXBean bean = ManagementFactory.getRuntimeMXBean();
        System.out.println(bean.getStartTime());
        System.out.println(DateUtils.formatDate(new Date(bean.getStartTime())));
    }
}
