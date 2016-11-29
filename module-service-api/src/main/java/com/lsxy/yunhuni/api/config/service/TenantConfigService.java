package com.lsxy.yunhuni.api.config.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.yunhuni.api.config.model.TenantConfig;

import java.util.List;

/**
 * Created by zhangxb on 2016/11/15.
 */
public interface TenantConfigService extends BaseService<TenantConfig> {
    /**
     * 获取配置对象
     */
    TenantConfig findByTypeAndKeyNameAndTenantId(String type, String keyName,String tenantId);
    List<TenantConfig> getPageByTypeAndKeyName(String type, String keyName);
}
