package com.lsxy.area.server.service.callcenter.api;

import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2017/1/9.
 */
@Service
@Component
public class ConversationOps implements com.lsxy.area.api.callcenter.ConversationOps{

    @Override
    public boolean dismiss(String ip, String appId, String conversationId) {
        return false;
    }
}
