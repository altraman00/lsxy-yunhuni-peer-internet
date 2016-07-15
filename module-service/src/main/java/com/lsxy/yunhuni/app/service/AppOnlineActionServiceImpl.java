package com.lsxy.yunhuni.app.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.model.AppOnlineAction;
import com.lsxy.yunhuni.api.app.service.AppOnlineActionService;
import com.lsxy.yunhuni.app.dao.AppOnlineActionDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

/**
 * Created by liups on 2016/7/15.
 */
public class AppOnlineActionServiceImpl extends AbstractService<AppOnlineAction> implements AppOnlineActionService {
    @Autowired
    AppOnlineActionDao appOnlineActionDao;

    @Override
    public BaseDaoInterface<AppOnlineAction, Serializable> getDao() {
        return this.appOnlineActionDao;
    }

    @Override
    public AppOnlineAction findActiveActionByAppId(String appId) {
        App app = new App();
        app.setId(appId);
        AppOnlineAction action = appOnlineActionDao.findByAppAndStatus(app, AppOnlineAction.STATUS_AVTIVE);
        return action;
    }
}
