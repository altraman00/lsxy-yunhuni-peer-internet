package com.lsxy.yunhuni.statistics.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.yunhuni.api.statistics.model.SubaccountMonth;
import com.lsxy.yunhuni.api.statistics.service.SubaccountMonthService;
import com.lsxy.yunhuni.statistics.dao.SubaccountMonthDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * Created by liups on 2017/2/21.
 */
@Service
public class SubaccountMonthServiceImpl extends AbstractService<SubaccountMonth> implements SubaccountMonthService {
    @Autowired
    SubaccountMonthDao subaccountMonthDao;
    @Override
    public BaseDaoInterface<SubaccountMonth, Serializable> getDao() {
        return this.subaccountMonthDao;
    }
}
