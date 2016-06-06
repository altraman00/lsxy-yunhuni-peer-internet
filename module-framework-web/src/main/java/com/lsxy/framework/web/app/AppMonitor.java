package com.lsxy.framework.web.app;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lsxy.framework.config.SystemConfig;
import com.lsxy.framework.core.utils.StringUtil;
import com.lsxy.framework.cache.manager.RedisCacheManager;

/**
 * 应用程序监控
 * 5秒钟设置一次redis缓存
 * key值为 config.properties中设置的system.id
 *
 * @author tandy
 */
//@Component
//@Lazy(value = false)
public class AppMonitor {

    private Log logger = LogFactory.getLog(AppMonitor.class);
    private String key = SystemConfig.getProperty("system.id");

//    @Autowired
    private RedisCacheManager redisCacheManager;

    private Thread t = new Thread(new Runnable() {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(5000);
                    redisCacheManager.set("monitor_" + key, "OK", 5);
                } catch (InterruptedException e) {
                    logger.debug("系统监控线程停止：monitor_" + key);
                    break;
                }
            }
        }
    });

    public AppMonitor() {
        if (StringUtil.isEmpty(key)) {
            logger.debug("系统标识system.id未设置，无法启动监控线程。。。。。");
        } else {
            logger.debug("系统监控线程启动：monitor_" + key);
            t.start();
        }
    }

//    @PreDestroy
    public void destroy() {
        this.t.interrupt();
    }
}
