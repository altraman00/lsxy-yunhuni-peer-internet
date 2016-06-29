package com.lsxy.yuhuni.app.service;

import com.lsxy.framework.core.exceptions.MatchMutiEntitiesException;
import com.lsxy.framework.core.service.BaseService;
import com.lsxy.yuhuni.app.model.App;

import java.util.List;

/**
 * 应用相关接口
 * Created by liups on 2016/6/29.
 */
public interface AppService extends BaseService<App> {
    List<App> findAppByUserName(String userName) throws MatchMutiEntitiesException;
}
