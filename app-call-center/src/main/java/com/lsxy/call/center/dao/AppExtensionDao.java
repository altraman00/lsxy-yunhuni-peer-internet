package com.lsxy.call.center.dao;

import com.lsxy.call.center.api.model.AppExtension;
import com.lsxy.framework.api.base.BaseDaoInterface;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhangxb on 2016/10/21.
 */
public interface AppExtensionDao extends BaseDaoInterface<AppExtension, Serializable> {
    List<AppExtension> findByAppId(String appId);

    long countByUser(String user);

    AppExtension findByTenantIdAndAppIdAndUser(String tenantId,String appId,String user);
}
