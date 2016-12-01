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
    TenantConfig findByTypeAndKeyNameAndTenantIdAndAppId(String type, String keyName,String tenantId,String appId);
    List<TenantConfig> getPageByTypeAndKeyName(String type, String keyName);
    /** 获取租户下的某个应用的录音存储时长*/
    int getRecordingTimeByTenantIdAndAppId(String tenantId,String appId);

}
