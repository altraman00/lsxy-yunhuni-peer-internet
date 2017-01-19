package com.lsxy.app.api.gateway.security.auth;

import com.lsxy.yunhuni.api.gateway.model.ApiInvokeLog;
import com.lsxy.yunhuni.api.gateway.service.ApiInvokeLogService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by Tandy on 2016/7/7.
 */
@Component
public class ASyncSaveApiLogTask {
    private static final Logger logger = LoggerFactory.getLogger(ASyncSaveApiLogTask.class);
    private AntPathMatcher antPathMatcher = new AntPathMatcher();
    private static Set<String> requestMappingPatternList = null;

    @Autowired
    private ApiInvokeLogService apiInvokeLogService;

    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    /**
     * 调用日志异步入库
     * @param appid
     * @param contentType
     * @param signature
     */
    @Async
    public void invokeApiSaveDB(String uri,String method, String appid,String payload, String contentType, String signature, String tenantId, String certId){
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
        String type = "unknown";
        Set<String> patterns = getRequestMappingPatternList();
        for(String pattern : patterns){
            if(StringUtils.isNotBlank(pattern)){
                //匹配路径
                String matchingPattern =  antPathMatcher.match(pattern, uri)?pattern:( !pattern.endsWith("/") && antPathMatcher.match(pattern + "/", uri)? pattern:null);
                if(StringUtils.isNotBlank(matchingPattern)){
                    type = matchingPattern;
                    break;
                }
            }
        }
        //由于{}里面的是变量名称，如：/v1/account/{account_id}/call/duo_callback，同样的接口可能会改变，所以变成*
        if(StringUtils.isNotBlank(type)){
            type = type.replaceAll("\\{\\w*\\}", "*");
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
    public Set<String> getRequestMappingPatternList(){
        if(requestMappingPatternList == null){
            synchronized(ASyncSaveApiLogTask.class){
                if(requestMappingPatternList == null){
                    Set<String> set = new HashSet<>();
                    Map<RequestMappingInfo, HandlerMethod> map = requestMappingHandlerMapping.getHandlerMethods();
                    for (Iterator<RequestMappingInfo> iterator = map.keySet().iterator(); iterator.hasNext();) {
                        RequestMappingInfo info = iterator.next();
                        Set<String> patterns = info.getPatternsCondition().getPatterns();
                        set.addAll(patterns);
                    }
                    requestMappingPatternList = set;
                }
            }
        }
        return requestMappingPatternList;
    }

}