package com.lsxy.yunhuni.config.dao;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.yunhuni.api.config.model.TenantConfig;

import java.io.Serializable;

/**
 * 配置类DAO
 * Created by zhangxb on 2016/8/23.
 */
public interface TenantConfigDao extends BaseDaoInterface<TenantConfig, Serializable> {
    TenantConfig findByTypeAndKeyNameAndTenantIdAndAppId(String type, String name,String tenantId,String appId);
}
