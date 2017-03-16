package com.lsxy.yunhuni.statistics.dao;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.yunhuni.api.statistics.model.SubaccountMonth;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by liups on 2017/2/21.
 */
public interface SubaccountMonthDao extends BaseDaoInterface<SubaccountMonth,Serializable> {
    SubaccountMonth findFirstByDt(Date preDate);
}
