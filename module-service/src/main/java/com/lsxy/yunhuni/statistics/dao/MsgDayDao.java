package com.lsxy.yunhuni.statistics.dao;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.yunhuni.api.statistics.model.MsgDay;
import com.lsxy.yunhuni.api.statistics.model.SubaccountDay;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by liups on 2017/3/14.
 */
public interface MsgDayDao extends BaseDaoInterface<MsgDay,Serializable> {
    SubaccountDay findFirstByDt(Date preDate);
}
