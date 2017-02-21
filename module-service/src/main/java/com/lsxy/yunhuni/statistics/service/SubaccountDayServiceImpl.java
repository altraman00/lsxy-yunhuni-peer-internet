package com.lsxy.yunhuni.statistics.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.yunhuni.api.statistics.model.SubaccountDay;
import com.lsxy.yunhuni.api.statistics.service.SubaccountDayService;
import com.lsxy.yunhuni.statistics.dao.SubaccountDayDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * Created by liups on 2017/2/21.
 */
@Service
public class SubaccountDayServiceImpl extends AbstractService<SubaccountDay> implements SubaccountDayService {

    @Autowired
    SubaccountDayDao subaccountDayDao;

    @Override
    public BaseDaoInterface<SubaccountDay, Serializable> getDao() {
        return this.subaccountDayDao;
    }
}
