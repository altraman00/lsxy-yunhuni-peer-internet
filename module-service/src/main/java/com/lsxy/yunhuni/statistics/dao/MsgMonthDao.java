package com.lsxy.yunhuni.statistics.dao;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.yunhuni.api.statistics.model.MsgMonth;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by liups on 2017/3/14.
 */
public interface MsgMonthDao extends BaseDaoInterface<MsgMonth,Serializable> {
    MsgMonth findFirstByDt(Date preDate);

}
