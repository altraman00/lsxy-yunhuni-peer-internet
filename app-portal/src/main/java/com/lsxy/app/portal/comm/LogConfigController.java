package com.lsxy.app.portal.comm;

import com.lsxy.framework.config.SystemConfig;
import com.lsxy.framework.web.rest.RestResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
//import org.apache.log4j.Level;
//import org.apache.log4j.LogManager;
//import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.lsxy.framework.web.rest.RestResponse.failed;

/**
 * Created by Tandy on 2016/6/6.
 * 可动态设置log4j的日志级别
 */
@RestController
@RequestMapping("/config")
public class LogConfigController {
//    private static final Log logger = LogFactory.getLog(LogConfigController.class);
//    @RequestMapping("/log/{package}/{level}")
//    public RestResponse configLog(@PathVariable("package") String p, @PathVariable("level") String l) {
//        String regularLevel = "INFO,DEBUG,WARN,ERROR,FATAL,ALL,OFF";
//        String x = l.toUpperCase();
//        if(regularLevel.indexOf(x) < 0){
//            return failed("00000001","无效的日志级别");
//        }
//
//        Level level = Level.toLevel(l);
//        Logger logger = LogManager.getLogger(p);
//        logger.setLevel(level);
//
//        return RestResponse.success(null);
//    }
//
//    @RequestMapping("/test001")
//    public  RestResponse testconfig(){
//        logger.debug("调试输出");
//        RestResponse result = null;
//       if(SystemConfig.isDevelopModel()){
//           String prefix = SystemConfig.getProperty("global.resPrefixUrl");
//           result = RestResponse.success(prefix);
//       }
//
//        result = RestResponse.failed("1234","asdf");
//        return result;
//    }
}
