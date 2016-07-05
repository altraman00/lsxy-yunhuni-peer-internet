package com.lsxy.yunhuni.recharge.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.yuhuni.api.recharge.model.ThirdPayRecord;
import com.lsxy.yuhuni.api.recharge.service.ThirdPayRecordService;
import com.lsxy.yunhuni.recharge.dao.ThirdPayRecordDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * Created by liups on 2016/7/2.
 */
@Service
public class ThirdPayRecordServiceImpl extends AbstractService<ThirdPayRecord> implements ThirdPayRecordService{
    @Autowired
    ThirdPayRecordDao thirdPayRecordDao;
    @Override
    public BaseDaoInterface<ThirdPayRecord, Serializable> getDao() {
        return this.thirdPayRecordDao;
    }
}
