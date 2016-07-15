package com.lsxy.yunhuni.api.app.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.yunhuni.api.app.model.AppOnlineAction;

/**
 * 应用上线动作接口
 * Created by liups on 2016/7/15.
 */
public interface AppOnlineActionService extends BaseService<AppOnlineAction> {

    /**
     * 获取应用当前进行中的动作
     * @param appId
     * @return
     */
    AppOnlineAction findActiveActionByAppId(String appId);
}
