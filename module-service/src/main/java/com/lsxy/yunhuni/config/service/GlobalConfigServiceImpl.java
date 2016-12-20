package com.lsxy.yunhuni.config.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.yunhuni.api.config.model.GlobalConfig;
import com.lsxy.yunhuni.api.config.service.GlobalConfigService;
import com.lsxy.yunhuni.config.dao.GlobalConfigDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * 全局配置类impl
 * Created by zhangxb on 2016/8/23.
 */
@Service
public class GlobalConfigServiceImpl extends AbstractService<GlobalConfig> implements GlobalConfigService {
    @Autowired
    private GlobalConfigDao configGlobalDao;
    @Override
    public BaseDaoInterface<GlobalConfig, Serializable> getDao() {
        return this.configGlobalDao;
    }

    @Override
    public GlobalConfig findByTypeAndName(String type, String name) {
        return configGlobalDao.findByTypeAndName(type,name);
    }

    @Override
    public GlobalConfig findByTypeAndKeyName(String type, String keyName) {
        return configGlobalDao.findByTypeAndKeyName(type,keyName);
    }
}
