package com.lsxy.framework.consume.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.consume.model.ConsumeMonth;
import com.lsxy.framework.api.consume.service.ConsumeMonthService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.consume.dao.ConsumeMonthDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

/**
 * 消费月统计serviceimpl
 * Created by zhangxb on 2016/7/6.
 */
public class ConsumeMonthServiceImpl extends AbstractService<ConsumeMonth> implements ConsumeMonthService{
    @Autowired
    ConsumeMonthDao consumeMonthDao;
    @Override
    public BaseDaoInterface<ConsumeMonth, Serializable> getDao() {
        return consumeMonthDao;
    }
}
