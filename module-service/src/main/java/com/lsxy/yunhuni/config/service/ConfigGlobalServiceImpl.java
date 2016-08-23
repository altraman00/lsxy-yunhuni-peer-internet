package com.lsxy.yunhuni.config.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.yunhuni.api.config.model.ConfigGlobal;
import com.lsxy.yunhuni.api.config.service.ConfigGlobalService;
import com.lsxy.yunhuni.config.dao.ConfigGlobalDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * 全局配置类impl
 * Created by zhangxb on 2016/8/23.
 */
@Service
public class ConfigGlobalServiceImpl extends AbstractService<ConfigGlobal> implements ConfigGlobalService {
    @Autowired
    private ConfigGlobalDao configGlobalDao;
    @Override
    public BaseDaoInterface<ConfigGlobal, Serializable> getDao() {
        return this.configGlobalDao;
    }

    @Override
    public ConfigGlobal findByTypeAndName(String type, String name) {
        return configGlobalDao.findByTypeAndName(type,name);
    }
}
