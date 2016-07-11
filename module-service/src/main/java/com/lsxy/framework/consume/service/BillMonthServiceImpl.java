package com.lsxy.framework.consume.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.consume.model.BillMonth;
import com.lsxy.framework.api.consume.service.BillMonthService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.consume.dao.BillMonthDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * Created by liups on 2016/7/11.
 */
@Service
public class BillMonthServiceImpl extends AbstractService<BillMonth> implements BillMonthService {
    @Autowired
    BillMonthDao billMonthDao;

    @Override
    public BaseDaoInterface<BillMonth, Serializable> getDao() {
        return this.billMonthDao;
    }
}
