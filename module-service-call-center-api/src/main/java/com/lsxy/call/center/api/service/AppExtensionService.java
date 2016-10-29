package com.lsxy.call.center.api.service;

import com.lsxy.call.center.api.model.AppExtension;
import com.lsxy.framework.api.base.BaseService;

import java.util.List;

/**
 * Created by zhangxb on 2016/10/21.
 */
public interface AppExtensionService extends BaseService<AppExtension> {

    public boolean register(AppExtension appExtension);

    public boolean login(String tenantId,String appId,String user,String pass);

    public boolean updateRegisterStatus(String id,Integer lastRegisterStatus);

    public boolean updateLastAction(String id,Integer lastAction);

    public boolean updateAgent(String id,String agent,Integer enable);
    /**
     * 根据应用id获取对于的分机
     * @param appId
     * @return
     */
    List<AppExtension> findByAppId(String appId);


}
