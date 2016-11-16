package com.lsxy.yunhuni.api.config.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.yunhuni.api.config.model.TenantConfig;

/**
 * Created by zhangxb on 2016/11/15.
 */
public interface TenantConfigService extends BaseService<TenantConfig> {
    /**
     * 获取配置对象
     * @param type 类型
     * @param name 名字
     * @return
     */
    TenantConfig findByTypeAndNameAndTenantId(String type, String name,String tenantId);
}
