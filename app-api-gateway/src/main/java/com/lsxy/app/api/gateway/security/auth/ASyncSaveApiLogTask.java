package com.lsxy.app.api.gateway.security.auth;

import com.lsxy.yunhuni.api.gateway.model.ApiInvokeLog;
import com.lsxy.yunhuni.api.gateway.service.ApiInvokeLogService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.PathMatcher;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by Tandy on 2016/7/7.
 */
@Component
public class ASyncSaveApiLogTask {
    private static final Logger logger = LoggerFactory.getLogger(ASyncSaveApiLogTask.class);

    private static List<RequestMappingInfo> RequestMappingInfoList = null;

    @Autowired
    private ApiInvokeLogService apiInvokeLogService;

    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    /**
     * 调用日志异步入库
     * @param req
     * @param appid
     * @param contentType
     * @param signature
     */
    @Async
    public void invokeApiSaveDB(HttpServletRequest req, String appid,String payload, String contentType, String signature, String tenantId, String certId){
        ApiInvokeLog log = new ApiInvokeLog();
        log.setAppid(appid);
        log.setBody(payload);
        log.setContentType(contentType);
        log.setMethod(req.getMethod());
        log.setSignature(signature);
        log.setUri(req.getRequestURI());
        log.setCertid(certId);
        log.setTenantId(tenantId);
        //TODO 设置api调用类型
        String type = "unknown";
        List<RequestMappingInfo> mappingInfoList = getRequestMappingInfoList();
        for(RequestMappingInfo mappingInfo : mappingInfoList){
            RequestMappingInfo info = mappingInfo.getMatchingCondition(req);
            if(info != null){
                PatternsRequestCondition patternsCondition = info.getPatternsCondition();
                if(patternsCondition !=null){
                    Set<String> patterns = patternsCondition.getPatterns();
                    for(String s:patterns){
                        if(StringUtils.isNotBlank(s)){
                            type = s;
                            break;
                        }
                    }
                    break;
                }
            }
        }

        log.setType(type);
        apiInvokeLogService.save(log);

        if(logger.isDebugEnabled()) {
            logger.debug("调用日志异步入库中完成:{}",log);
        }
    }

    /**
     * 获取所有RequestMapping的信息
     * @return
     */
    public List<RequestMappingInfo> getRequestMappingInfoList(){
        if(RequestMappingInfoList != null){
            return RequestMappingInfoList;
        }else{
            synchronized(ASyncSaveApiLogTask.class){
                if(RequestMappingInfoList != null){
                    return RequestMappingInfoList;
                }else{
                    RequestMappingInfoList = new ArrayList<>();
                    Map<RequestMappingInfo, HandlerMethod> map = requestMappingHandlerMapping.getHandlerMethods();
                    for (Iterator<RequestMappingInfo> iterator = map.keySet().iterator(); iterator.hasNext();) {
                        RequestMappingInfo info = iterator.next();
                        RequestMappingInfoList.add(info);
                    }
                    return RequestMappingInfoList;
                }
            }
        }
    }

}