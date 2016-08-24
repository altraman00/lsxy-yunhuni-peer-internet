package com.lsxy.app.api.gateway.rest;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tandy on 16/8/7.
 */
@Component
public class AsyncRequestContext {

    private Map<String,DeferredResult> deferedResultMaps = new HashMap<>();

    public void register(String requestId,DeferredResult result){
        this.deferedResultMaps.put(requestId,result);
    }

    public DeferredResult get(String requestId){
        return this.deferedResultMaps.get(requestId);
    }

    public void remove(String requestid) {
        deferedResultMaps.remove(requestid);
    }
}
