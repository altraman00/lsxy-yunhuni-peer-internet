package com.lsxy.framework.sms.service;

import com.lsxy.framework.api.sms.model.SMSSendLog;
import com.lsxy.framework.api.sms.service.SMSSendLogService;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

import java.io.StringWriter;
import java.util.Map;
import java.util.Properties;

/**
 * Created by Tandy on 2016/7/7.
 */
public abstract class AbstractSmsServiceImpl implements  SmsService{



    /**
     * 根据制定模板构建短信内容
     * @param template
     * @param params
     * @return
     */
    protected String buildSmsContent(String template, Map<String, Object> params) {
        Properties properties = new Properties();
        properties.setProperty("resource.loader", "class");
        properties.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        VelocityEngine velocityEngine = new VelocityEngine(properties);
        VelocityContext context = new VelocityContext();
        StringWriter writer = new StringWriter();
        for(String key : params.keySet()){
            context.put(key, params.get(key));
        }
        velocityEngine.mergeTemplate("/templates/" + template, "utf-8", context, writer);
        return writer.toString();
    }



}
