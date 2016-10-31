package com.lsxy.yunhuni.api.app.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.model.AppOnlineAction;

/**
 * 应用上线动作接口
 * Created by liups on 2016/7/15.
 */
public interface AppOnlineActionService extends BaseService<AppOnlineAction> {

    /**
     * 获取应用当前进行中的动作(正常情况下，只会有0-1个有效的上线动作对象)
     * @param appId
     * @return
     */
    AppOnlineAction findActiveActionByAppId(String appId);

    /**
     * 应用动作执行--选号
     * @param appId 应用
     */
    void actionOfSelectNum(String appId);


    /**
     * 应用动作执行--上线(应用包含ivr服务)
     * @param appId 应用ID
     * @return
     */
    AppOnlineAction actionOfOnline(Tenant tenant, String appId,String nums);


    /**
     * 应用下线
     * @param appId
     * @return
     */
    App offline(String appId);

    /**
     * 重置应用上线步骤
     * @param appId
     */
    void resetAppOnlineAction(String appId);

    /**
     * 获取上一次上线的号码
     * @param appId
     * @return
     */
    String findLastOnlineNums(String appId);
}
