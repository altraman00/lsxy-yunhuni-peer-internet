package com.lsxy.app.api.gateway.security.auth;

import com.lsxy.framework.api.gateway.model.ApiInvokeLog;
import com.lsxy.framework.api.gateway.service.ApiInvokeLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Created by Tandy on 2016/7/7.
 */
@Component
public class ASyncSaveApiLogTask {
    private static final Logger logger = LoggerFactory.getLogger(ASyncSaveApiLogTask.class);

    @Autowired
    private ApiInvokeLogService apiInvokeLogService;

    /**
     * 调用日志异步入库
     * @param appid
     * @param payload
     * @param contentType
     * @param method
     * @param signature
     * @param uri
     */
    @Async
    public void invokeApiSaveDB(String appid, String payload, String contentType, String method, String signature, String uri,String tenantId,String certId){
        ApiInvokeLog log = new ApiInvokeLog();
        log.setAppid(appid);
        log.setBody(payload);
        log.setContentType(contentType);
        log.setMethod(method);
        log.setSignature(signature);
        log.setUri(uri);
        log.setCertid(certId);
        log.setTenantId(tenantId);
        //TODO 设置api调用类型
        log.setType(uri);
        apiInvokeLogService.save(log);

        if(logger.isDebugEnabled()) {
            logger.debug("调用日志异步入库中完成:{}",log);
        }
    }

}