package com.lsxy.framework.consume.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.consume.model.ConsumeHour;
import com.lsxy.framework.api.consume.service.ConsumeHourService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.consume.dao.ConsumeHourDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * 消费小时统计serviceimpl
 * Created by zhangxb on 2016/7/6.
 */
@Service
public class ConsumeHourServiceImpl extends AbstractService<ConsumeHour> implements ConsumeHourService{
    @Autowired
    ConsumeHourDao consumeHourDao;
    @Override
    public BaseDaoInterface<ConsumeHour, Serializable> getDao() {
        return consumeHourDao;
    }
}
