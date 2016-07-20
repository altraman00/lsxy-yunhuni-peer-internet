package com.lsxy.yunhuni.api.app.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.core.exceptions.MatchMutiEntitiesException;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.model.AppOnlineAction;
import com.lsxy.yunhuni.api.exceptions.NotEnoughMoneyException;

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
     * 应用动作执行--正在支付
     * @param appId 应用ID
     * @param ivr ivr号
     * @param tenant 租户
     * @param contains IVR号是否属于可选号码池
     * @return
     */
    AppOnlineAction actionOfInPay(String appId, String ivr, Tenant tenant,boolean contains);

    /**
     * 应用动作执行--上线(应用包含ivr服务)
     * @param appId 应用ID
     * @return
     */
    AppOnlineAction actionOfOnline(String userName, String appId) throws NotEnoughMoneyException;

    /**
     * 应用没有定制IVR服务--直接上线
     * @param userName
     * @param appId
     * @return
     */
    AppOnlineAction actionOfDirectOnline(String userName, String appId);

    /**
     * 应用取消支付，重选号码
     * @param userName
     * @param appId
     * @return
     */
    AppOnlineAction resetIvr(String userName, String appId);

    /**
     * 应用下线
     * @param appId
     * @return
     */
    App offline(String appId);
}
