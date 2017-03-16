package com.lsxy.app.backend.task;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsxy.call.center.api.model.AppExtension;
import com.lsxy.call.center.api.model.CallCenterAgent;
import com.lsxy.call.center.api.service.AppExtensionService;
import com.lsxy.call.center.api.service.CallCenterAgentService;
import com.lsxy.framework.cache.exceptions.TransactionExecFailedException;
import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.framework.config.SystemConfig;
import com.lsxy.framework.core.exceptions.api.YunhuniApiException;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 用于测试环境前期模拟分机座席自动注册，可修改Profile,将其作用的环境修改
 * Created by liups on 2016/11/25.
 */
@Profile({"development","test"})
@Component
public class CallCenterRegisterImitator {
    private static final Logger logger = LoggerFactory.getLogger(CallCenterRegisterImitator.class);
    @Reference(timeout=3000,check = false,lazy = true)
    AppExtensionService appExtensionService;

    @Reference(timeout=3000,check = false,lazy = true)
    CallCenterAgentService callCenterAgentService;

    @Autowired
    RedisCacheService redisCacheService;

    @Scheduled(cron="0 0/3 * * * ? ")
    public void scheduled_agentRegister_yyyyMMddHHmm(){
        Iterable<CallCenterAgent> list = callCenterAgentService.list();
        list.forEach(agent -> {
            try {
                callCenterAgentService.keepAlive(agent.getAppId(),agent.getSubaccountId(), agent.getName());
            } catch (Exception e) {
                logger.error("座席保持失败",e);
            }
        });
    }

}
