package com.lsxy.yunhuni.statistics.dao;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.yunhuni.api.statistics.model.SubaccountDay;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by liups on 2017/2/21.
 */
public interface SubaccountDayDao extends BaseDaoInterface<SubaccountDay,Serializable> {
    SubaccountDay findFirstByDt(Date date);
}
