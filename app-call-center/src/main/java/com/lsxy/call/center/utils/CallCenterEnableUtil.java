package com.lsxy.call.center.utils;

import com.lsxy.framework.api.tenant.service.TenantServiceSwitchService;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.service.AppService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by liuws on 2016/11/24.
 */
@Component
public class CallCenterEnableUtil {

    private static final Logger logger = LoggerFactory.getLogger(CallCenterEnableUtil.class);

    @Autowired
    private TenantServiceSwitchService tenantServiceSwitchService;

    @Autowired
    private AppService appService;

    public boolean enabled(String tenantId,String appId){
        try {
            App app = appService.findById(appId);
            if(app.getIsCallCenter() == null || app.getIsCallCenter() != 1){
                return false;
            }
        } catch (Throwable e) {
            logger.error("判断是否开启呼叫中心服务失败",e);
            return false;
        }
        return true;
    }
}
