package com.lsxy.call.center.states.state;

import com.lsxy.framework.cache.manager.RedisCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 分机状态管理
 * Created by liuws on 2016/11/11.
 */
@Component
public class ExtensionState {
    private static final Logger logger = LoggerFactory.getLogger(ExtensionState.class);

    private static final String STATE_PREFIXED_KEY = "callcenter.extension.state_";

    @Autowired
    private RedisCacheService redisCacheService;

    private String getKey(String extensionId){
        return STATE_PREFIXED_KEY + extensionId;
    }
}
