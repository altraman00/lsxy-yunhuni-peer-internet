package com.lsxy.yunhuni.config.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.yunhuni.api.config.model.TenantConfig;
import com.lsxy.yunhuni.api.config.service.TenantConfigService;
import com.lsxy.yunhuni.config.dao.TenantConfigDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * 全局配置类impl
 * Created by zhangxb on 2016/8/23.
 */
@Service
public class TenantConfigServiceImpl extends AbstractService<TenantConfig> implements TenantConfigService {
    @Autowired
    private TenantConfigDao tenantConfigDao;
    @Override
    public BaseDaoInterface<TenantConfig, Serializable> getDao() {
        return this.tenantConfigDao;
    }

    @Override
    public TenantConfig findByTypeAndKeyNameAndTenantId(String type, String keyName, String tenantId) {
        return null;
    }

    @Override
    public List<TenantConfig> getPageByTypeAndKeyName(String type, String keyName) {
        return null;
    }
}
