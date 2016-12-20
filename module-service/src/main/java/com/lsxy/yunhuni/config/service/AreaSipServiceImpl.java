package com.lsxy.yunhuni.config.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.yunhuni.api.config.model.AreaSip;
import com.lsxy.yunhuni.api.config.service.AreaSipService;
import com.lsxy.yunhuni.config.dao.AreaSipDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * Created by liups on 2016/11/1.
 */
@Service
public class AreaSipServiceImpl extends AbstractService<AreaSip> implements AreaSipService {

    @Autowired
    AreaSipDao areaSipDao;

    @Override
    public BaseDaoInterface<AreaSip, Serializable> getDao() {
        return this.areaSipDao;
    }

    @Override
    public AreaSip getOneAreaSipByAreaId(String areaId) {
        return areaSipDao.findFirstByAreaId(areaId);
    }
}
