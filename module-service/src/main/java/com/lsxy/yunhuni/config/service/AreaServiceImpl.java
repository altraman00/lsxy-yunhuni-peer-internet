package com.lsxy.yunhuni.config.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.yunhuni.api.config.model.Area;
import com.lsxy.yunhuni.api.config.service.AreaService;
import com.lsxy.yunhuni.config.dao.AreaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * Created by liups on 2016/8/24.
 */
@Service
public class AreaServiceImpl extends AbstractService<Area> implements AreaService {
    @Autowired
    AreaDao areaDao;

    @Override
    public BaseDaoInterface<Area, Serializable> getDao() {
        return this.areaDao;
    }

}
