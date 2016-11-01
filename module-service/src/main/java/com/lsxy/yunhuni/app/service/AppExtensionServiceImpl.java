package com.lsxy.yunhuni.app.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.yunhuni.api.app.model.AppExtension;
import com.lsxy.yunhuni.api.app.service.AppExtensionService;
import com.lsxy.yunhuni.app.dao.AppExtensionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhangxb on 2016/10/21.
 */
@Service
public class AppExtensionServiceImpl extends AbstractService<AppExtension> implements AppExtensionService {
    @Autowired
    AppExtensionDao appExtensionDao;
    @Override
    public BaseDaoInterface<AppExtension, Serializable> getDao() {
        return this.appExtensionDao;
    }

    @Override
    public List<AppExtension> findByAppId(String appId) {
        return appExtensionDao.findByAppId(appId);
    }
}
