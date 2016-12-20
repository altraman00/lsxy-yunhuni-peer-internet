package com.lsxy.yunhuni.app.dao;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.yunhuni.api.app.model.AppOnlineAction;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liups on 2016/7/15.
 */
public interface AppOnlineActionDao extends BaseDaoInterface<AppOnlineAction, Serializable> {
    /**
     * 根据状态获取应用的动作
     * @param appId
     * @param statusAvtive
     * @return
     */
    List<AppOnlineAction> findByAppIdAndStatusOrderByCreateTimeDesc(String appId, int statusAvtive);

    /**
     * 获取最后一次上线完成的动作
     * @param appId
     * @param actionOnline
     * @return
     */
    AppOnlineAction findFirstByAppIdAndActionOrderByCreateTimeDesc(String appId, int actionOnline);
}
